/*
 * Copyright (c) 2022 LlemonDuck
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.timetracking.farming;

import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.timetracking.TimeTrackingConfig;

@Singleton
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CompostTracker
{

	private final FarmingWorld farmingWorld;
	private final ConfigManager configManager;

	private static String configKey(FarmingPatch fp)
	{
		return fp.configKey() + "." + TimeTrackingConfig.COMPOST;
	}

	public void setCompostState(FarmingPatch fp, CompostState state)
	{
		log.debug("Storing compost state [{}] for patch [{}]", state, fp);
		if (state == null)
		{
			configManager.unsetRSProfileConfiguration(TimeTrackingConfig.CONFIG_GROUP, configKey(fp));
		}
		else
		{
			configManager.setRSProfileConfiguration(TimeTrackingConfig.CONFIG_GROUP, configKey(fp), state);
		}
	}

	public CompostState getCompostState(FarmingPatch fp)
	{
		return configManager.getRSProfileConfiguration(
			TimeTrackingConfig.CONFIG_GROUP,
			configKey(fp),
			CompostState.class
		);
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (event.getVarbitId() == -1)
		{
			return;
		}
		// TODO: Do we care about triggering this on login?
		// TODO: Consider getting rid of the configs, blagh, can't easily get rid of it because no off thread access from swing -> read varbit
		// From a varbit, obtain a related FarmingPatch, returning on no match
		// Update the compost state by setCompostState
		var farmingPatch = farmingWorld.getPatchForCompostVarbit(event.getVarbitId());

		if (farmingPatch == null)
		{
			return;
		}
		var compostStateVal = event.getValue();

		CompostState compostState = null;
		switch (compostStateVal)
		{
			case 1:
				compostState = CompostState.COMPOST;
				break;
			case 2:
				compostState = CompostState.SUPERCOMPOST;
				break;
			case 3:
				compostState = CompostState.ULTRACOMPOST;
				break;
		}
		log.info("Compost state is now {} for patch {} - {}", compostState, farmingPatch.getRegion().getName(), farmingPatch.getName());
		setCompostState(farmingPatch, compostState);
	}
}
