/*
 * Copyright (c) 2018, NotFoxtrot <https://github.com/NotFoxtrot>
 * Copyright (c) 2018 Abex
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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.Getter;
import net.runelite.api.annotations.Varbit;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.plugins.timetracking.Tab;

@Singleton
class FarmingWorld
{
	@SuppressWarnings("PMD.ImmutableField")
	private Multimap<Integer, FarmingRegion> regions = HashMultimap.create();

	@Getter
	@SuppressWarnings("PMD.ImmutableField")
	private Map<Tab, Set<FarmingPatch>> tabs = new HashMap<>();

	private final Comparator<FarmingPatch> tabSorter = Comparator
		.comparing(FarmingPatch::getImplementation)
		.thenComparing((FarmingPatch p) -> p.getRegion().getName())
		.thenComparing(FarmingPatch::getName);

	@Getter
	private final FarmingRegion farmingGuildRegion;

	FarmingWorld()
	{
		// Some of these patches get updated in multiple regions.
		// It may be worth it to add a specialization for these patches
		add(new FarmingRegion("Al Kharid", 13106, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_CACTUS_TRANSMIT, PatchImplementation.CACTUS, NpcID.FARMING_GARDENER_CACTUS)
		), 13362, 13105);

		add(new FarmingRegion("Aldarin", 5421, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_HOPS_5_TRANSMIT, PatchImplementation.HOPS, NpcID.FARMING_GARDENER_HOPS_5)
		), 5165, 5166, 5422, 5677, 5678);

		add(new FarmingRegion("Anglers' Retreat", 9770, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_HARDWOOD_5_TRANSMIT, PatchImplementation.HARDWOOD_TREE, NpcID.FARMING_GARDENER_HARDWOOD_TREE_5)
		));

		add(new FarmingRegion("Ardougne", 10290, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_BUSH_4_TRANSMIT, PatchImplementation.BUSH, NpcID.FARMING_GARDENER_BUSH_4)
		), 10546);
		add(new FarmingRegion("Ardougne", 10548, false,
			new FarmingPatch("North", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_VEG_5_TRANSMIT, PatchImplementation.ALLOTMENT, NpcID.KRAGEN, 0),
			new FarmingPatch("South", VarbitID.FARMING_TRANSMIT_B, VarbitID.FARMING_COMPOST_VARBIT_VEG_6_TRANSMIT, PatchImplementation.ALLOTMENT, NpcID.KRAGEN, 1),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_C, VarbitID.FARMING_COMPOST_VARBIT_FLOWER_3_TRANSMIT, PatchImplementation.FLOWER),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_D, VarbitID.FARMING_COMPOST_VARBIT_HERB_3_TRANSMIT, PatchImplementation.HERB),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_E, -1, PatchImplementation.COMPOST)
		));

		add(new FarmingRegion("Auburnvale", 5427, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_TREE_COMPOST_7_TRANSMIT, PatchImplementation.TREE, NpcID.FARMING_GARDENER_TREE_7),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_B, VarbitID.FARMING_COMPOST_VARBIT_BELLADONNA_2_TRANSMIT, PatchImplementation.BELLADONNA)
		), 5428, 5684);

		add(new FarmingRegion("Avium Savannah", 6702, true,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_HARDWOOD_4_TRANSMIT, PatchImplementation.HARDWOOD_TREE, NpcID.FROG_QUEST_MARCELLUS)
		), 6446);

		add(new FarmingRegion("Brimhaven", 11058, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_FRUIT_TREE_3_TRANSMIT, PatchImplementation.FRUIT_TREE, NpcID.GARTH),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_B, VarbitID.FARMING_COMPOST_VARBIT_SPIRIT_TREE_3_TRANSMIT, PatchImplementation.SPIRIT_TREE, NpcID.FARMING_GARDENER_SPIRIT_TREE_3)
		), 11057);

		add(new FarmingRegion("Catherby", 11062, false,
			new FarmingPatch("North", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_VEG_3_TRANSMIT, PatchImplementation.ALLOTMENT, NpcID.DANTAERA, 0),
			new FarmingPatch("South", VarbitID.FARMING_TRANSMIT_B, VarbitID.FARMING_COMPOST_VARBIT_VEG_4_TRANSMIT, PatchImplementation.ALLOTMENT, NpcID.DANTAERA, 1),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_C, VarbitID.FARMING_COMPOST_VARBIT_FLOWER_2_TRANSMIT, PatchImplementation.FLOWER),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_D, VarbitID.FARMING_COMPOST_VARBIT_HERB_2_TRANSMIT, PatchImplementation.HERB),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_E, -1, PatchImplementation.COMPOST)
		)
		{
			@Override
			public boolean isInBounds(WorldPoint loc)
			{
				if (loc.getX() >= 2816 && loc.getY() < 3456)
				{
					//Upstairs sends different varbits
					return loc.getX() < 2840 && loc.getY() >= 3440 && loc.getPlane() == 0;
				}
				return true;
			}
		}, 11061, 11318, 11317);
		add(new FarmingRegion("Catherby", 11317, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_FRUIT_TREE_4_TRANSMIT, PatchImplementation.FRUIT_TREE, NpcID.FARMING_GARDENER_FRUIT_4)
		)
		{
			//The fruit tree patch is always sent when upstairs in 11317
			@Override
			public boolean isInBounds(WorldPoint loc)
			{
				return loc.getX() >= 2840 || loc.getY() < 3440 || loc.getPlane() == 1;
			}
		});

		add(new FarmingRegion("Civitas illa Fortis", 6192, false,
			new FarmingPatch("North", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_VEG_16_TRANSMIT, PatchImplementation.ALLOTMENT, NpcID.FORTIS_GARDENER, 0),
			new FarmingPatch("South", VarbitID.FARMING_TRANSMIT_B, VarbitID.FARMING_COMPOST_VARBIT_VEG_17_TRANSMIT, PatchImplementation.ALLOTMENT, NpcID.FORTIS_GARDENER, 1),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_C, VarbitID.FARMING_COMPOST_VARBIT_FLOWER_8_TRANSMIT, PatchImplementation.FLOWER),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_D, VarbitID.FARMING_COMPOST_VARBIT_HERB_8_TRANSMIT, PatchImplementation.HERB),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_E, -1, PatchImplementation.COMPOST)
		), 6447, 6448, 6449, 6191, 6193);

		add(new FarmingRegion("Champions' Guild", 12596, true,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_BUSH_1_TRANSMIT, PatchImplementation.BUSH, NpcID.FARMING_GARDENER_BUSH_1)
		));

		add(new FarmingRegion("Draynor Manor", 12340, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_BELLADONNA_TRANSMIT, PatchImplementation.BELLADONNA)
		));

		add(new FarmingRegion("Entrana", 11060, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_HOPS_2_TRANSMIT, PatchImplementation.HOPS, NpcID.FRANCIS)
		), 11316);

		add(new FarmingRegion("Etceteria", 10300, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_BUSH_3_TRANSMIT, PatchImplementation.BUSH, NpcID.FARMING_GARDENER_BUSH_3),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_B, -1, PatchImplementation.SPIRIT_TREE, NpcID.FARMING_GARDENER_SPIRIT_TREE_2)
		));

		add(new FarmingRegion("Falador", 11828, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_TREE_2_TRANSMIT, PatchImplementation.TREE, NpcID.FARMING_GARDENER_TREE_2)
		), 12084);
		add(new FarmingRegion("Falador", 12083, false,
			new FarmingPatch("North West", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_VEG_1_TRANSMIT, PatchImplementation.ALLOTMENT, NpcID.ELSTAN, 0),
			new FarmingPatch("South East", VarbitID.FARMING_TRANSMIT_B, VarbitID.FARMING_COMPOST_VARBIT_VEG_2_TRANSMIT, PatchImplementation.ALLOTMENT, NpcID.ELSTAN, 1),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_C, VarbitID.FARMING_COMPOST_VARBIT_FLOWER_1_TRANSMIT, PatchImplementation.FLOWER),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_D, VarbitID.FARMING_COMPOST_VARBIT_HERB_1_TRANSMIT, PatchImplementation.HERB),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_E, -1, PatchImplementation.COMPOST)
		)
		{
			@Override
			public boolean isInBounds(WorldPoint loc)
			{
				//Not on region boundary due to Port Sarim Spirit Tree patch
				return loc.getY() >= 3272;
			}
		});

		add(new FarmingRegion("Fossil Island", 14651, false,
			new FarmingPatch("East", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_HARDWOOD_1_TRANSMIT, PatchImplementation.HARDWOOD_TREE, NpcID.FOSSIL_SQUIRREL_GARDENER1),
			new FarmingPatch("Middle", VarbitID.FARMING_TRANSMIT_B, VarbitID.FARMING_COMPOST_VARBIT_HARDWOOD_2_TRANSMIT, PatchImplementation.HARDWOOD_TREE, NpcID.FOSSIL_SQUIRREL_GARDENER2),
			new FarmingPatch("West", VarbitID.FARMING_TRANSMIT_C, VarbitID.FARMING_COMPOST_VARBIT_HARDWOOD_3_TRANSMIT, PatchImplementation.HARDWOOD_TREE, NpcID.FOSSIL_SQUIRREL_GARDENER3)
		)
		{
			@Override
			public boolean isInBounds(WorldPoint loc)
			{
				//Hardwood tree varbits are sent anywhere on plane 0 of fossil island.
				//Varbits get sent 1 tick earlier than expected when climbing certain ladders and stairs

				//Stairs to house on the hill
				if (loc.getX() == 3753 && loc.getY() >= 3868 && loc.getY() <= 3870)
				{
					return false;
				}

				//East and west ladders to rope bridge
				if ((loc.getX() == 3729 || loc.getX() == 3728 || loc.getX() == 3747 || loc.getX() == 3746)
					&& loc.getY() <= 3832 && loc.getY() >= 3830)
				{
					return false;
				}

				return loc.getPlane() == 0;
			}
		}, 14907, 14908, 15164, 14652, 14906, 14650, 15162, 15163);
		add(new FarmingRegion("Seaweed", 15008, false,
			new FarmingPatch("North", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_SEAWEED_1_TRANSMIT, PatchImplementation.SEAWEED, NpcID.FOSSIL_GARDENER_UNDERWATER, 0),
			new FarmingPatch("South", VarbitID.FARMING_TRANSMIT_B, VarbitID.FARMING_COMPOST_VARBIT_SEAWEED_2_TRANSMIT, PatchImplementation.SEAWEED, NpcID.FOSSIL_GARDENER_UNDERWATER, 1)
		));

		add(new FarmingRegion("Gnome Stronghold", 9781, true,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_TREE_COMPOST_5_TRANSMIT, PatchImplementation.TREE, NpcID.FARMING_GARDENER_TREE_GNOME),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_B, VarbitID.FARMING_COMPOST_VARBIT_FRUIT_TREE_1_TRANSMIT, PatchImplementation.FRUIT_TREE, NpcID.FARMING_GARDENER_FRUIT_1)
		), 9782, 9526, 9525);

		add(new FarmingRegion("Great Conch", 12581, true,
			new FarmingPatch("East", VarbitID.FARMING_TRANSMIT_A, -1, PatchImplementation.CORAL, NpcID.TORTUGAN_CORAL_FARMER, 0),
			new FarmingPatch("West", VarbitID.FARMING_TRANSMIT_B, -1, PatchImplementation.CORAL, NpcID.TORTUGAN_CORAL_FARMER, 1),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_C, -1, PatchImplementation.CALQUAT, NpcID.FARMING_GARDENER_CALQUAT_3)
		),  12325, 12326, 12327, 12580, 12581, 12582, 12583, 12836, 12837, 12838, 12839, 13092, 13093, 13194);

		add(new FarmingRegion("Harmony", 15148, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_VEG_VARBIT_9, PatchImplementation.ALLOTMENT),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_B, VarbitID.FARMING_COMPOST_VARBIT_HERB_5_TRANSMIT, PatchImplementation.HERB)
		));

		add(new FarmingRegion("Kastori", 5423, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_CALQUAT_TREE_2_TRANSMIT, PatchImplementation.CALQUAT, NpcID.FARMING_GARDENER_CALQUAT_2),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_B, VarbitID.FARMING_COMPOST_VARBIT_FRUIT_TREE_7_TRANSMIT, PatchImplementation.FRUIT_TREE, NpcID.FARMING_GARDENER_FRUIT_7),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_C, VarbitID.FARMING_COMPOST_VARBIT_FLOWER_9_TRANSMIT, PatchImplementation.FLOWER)
		), 5167, 5424);

		add(new FarmingRegion("Kourend", 6967, false,
			new FarmingPatch("North East", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_VEG_10_TRANSMIT, PatchImplementation.ALLOTMENT, NpcID.HOSIDIUS_ALLOTMENT_GARDENER, 0),
			new FarmingPatch("South West", VarbitID.FARMING_TRANSMIT_B, VarbitID.FARMING_COMPOST_VARBIT_VEG_11_TRANSMIT, PatchImplementation.ALLOTMENT, NpcID.HOSIDIUS_ALLOTMENT_GARDENER, 1),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_C, VarbitID.FARMING_COMPOST_VARBIT_FLOWER_5_TRANSMIT, PatchImplementation.FLOWER),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_D, VarbitID.FARMING_COMPOST_VARBIT_HERB_6_TRANSMIT, PatchImplementation.HERB),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_E, -1, PatchImplementation.COMPOST),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_F, VarbitID.FARMING_COMPOST_VARBIT_SPIRIT_TREE_4_TRANSMIT, PatchImplementation.SPIRIT_TREE, NpcID.FARMING_GARDENER_SPIRIT_TREE_4)
		), 6711);
		add(new FarmingRegion("Kourend", 7223, false,
			new FarmingPatch("East 1", VarbitID.FARMING_TRANSMIT_A1, -1, PatchImplementation.GRAPES),
			new FarmingPatch("East 2", VarbitID.FARMING_TRANSMIT_A2, -1, PatchImplementation.GRAPES),
			new FarmingPatch("East 3", VarbitID.FARMING_TRANSMIT_B1, -1, PatchImplementation.GRAPES),
			new FarmingPatch("East 4", VarbitID.FARMING_TRANSMIT_B2, -1, PatchImplementation.GRAPES),
			new FarmingPatch("East 5", VarbitID.FARMING_TRANSMIT_C1, -1, PatchImplementation.GRAPES),
			new FarmingPatch("East 6", VarbitID.FARMING_TRANSMIT_C2, -1, PatchImplementation.GRAPES),
			new FarmingPatch("West 1", VarbitID.FARMING_TRANSMIT_D1, -1, PatchImplementation.GRAPES),
			new FarmingPatch("West 2", VarbitID.FARMING_TRANSMIT_D2, -1, PatchImplementation.GRAPES),
			new FarmingPatch("West 3", VarbitID.FARMING_TRANSMIT_E1, -1, PatchImplementation.GRAPES),
			new FarmingPatch("West 4", VarbitID.FARMING_TRANSMIT_E2, -1, PatchImplementation.GRAPES),
			new FarmingPatch("West 5", VarbitID.FARMING_TRANSMIT_F1, -1, PatchImplementation.GRAPES),
			new FarmingPatch("West 6", VarbitID.FARMING_TRANSMIT_F2, -1, PatchImplementation.GRAPES)
		));

		add(new FarmingRegion("Lletya", 9265, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_FRUIT_TREE_5_TRANSMIT, PatchImplementation.FRUIT_TREE, NpcID.FARMING_GARDENER_FRUIT_TREE_5)
		), 11103);

		add(new FarmingRegion("Lumbridge", 12851, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_HOPS_3_TRANSMIT, PatchImplementation.HOPS, NpcID.FARMING_GARDENER_HOPS_3)
		));
		add(new FarmingRegion("Lumbridge", 12594, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_TREE_4_TRANSMIT, PatchImplementation.TREE, NpcID.FARMING_GARDENER_TREE_4)
		), 12850);

		add(new FarmingRegion("Morytania", 13622, false,
			new FarmingPatch("Mushroom", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_MUSHROOM_TRANSMIT, PatchImplementation.MUSHROOM)
		), 13878);
		add(new FarmingRegion("Morytania", 14391, false,
			new FarmingPatch("North West", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_VEG_7_TRANSMIT, PatchImplementation.ALLOTMENT, NpcID.LYRA, 0),
			new FarmingPatch("South East", VarbitID.FARMING_TRANSMIT_B, VarbitID.FARMING_COMPOST_VARBIT_VEG_8_TRANSMIT, PatchImplementation.ALLOTMENT, NpcID.LYRA, 1),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_C, VarbitID.FARMING_COMPOST_VARBIT_FLOWER_4_TRANSMIT, PatchImplementation.FLOWER),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_D, VarbitID.FARMING_COMPOST_VARBIT_HERB_4_TRANSMIT, PatchImplementation.HERB),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_E, -1, PatchImplementation.COMPOST)
		), 14390);

		add(new FarmingRegion("Port Sarim", 12082, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_SPIRIT_TREE_1_TRANSMIT, PatchImplementation.SPIRIT_TREE, NpcID.FARMING_GARDENER_SPIRIT_TREE_1)
		)
		{
			@Override
			public boolean isInBounds(WorldPoint loc)
			{
				return loc.getY() < 3272;
			}
		}, 12083);

		add(new FarmingRegion("Rimmington", 11570, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_BUSH_2_TRANSMIT, PatchImplementation.BUSH, NpcID.FARMING_GARDENER_BUSH_2)
		), 11826);

		add(new FarmingRegion("Seers' Village", 10551, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_HOPS_4_TRANSMIT, PatchImplementation.HOPS, NpcID.FARMING_GARDENER_HOPS_4)
		), 10550);

		add(new FarmingRegion("Tai Bwo Wannai", 11056, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_CALQUAT_TREE_TRANSMIT, PatchImplementation.CALQUAT, NpcID.FARMING_GARDENER_CALQUAT)
		));

		add(new FarmingRegion("Taverley", 11573, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_TREE_1_TRANSMIT, PatchImplementation.TREE, NpcID.FARMING_GARDENER_TREE_1)
		), 11829);

		add(new FarmingRegion("Tree Gnome Village", 9777, true,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_FRUIT_TREE_2_TRANSMIT, PatchImplementation.FRUIT_TREE, NpcID.FARMING_GARDENER_FRUIT_2)
		), 10033);

		add(new FarmingRegion("Troll Stronghold", 11321, true,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.MYARM_HERBPATCH_COMPOST_TRANSMIT, PatchImplementation.HERB)
		));

		add(new FarmingRegion("Varrock", 12854, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_TREE_3_TRANSMIT, PatchImplementation.TREE, NpcID.FARMING_GARDENER_TREE_3_02)
		), 12853);

		add(new FarmingRegion("Yanille", 10288, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_HOPS_1_TRANSMIT, PatchImplementation.HOPS, NpcID.FARMING_GARDENER_HOPS_1)
		));

		add(new FarmingRegion("Weiss", 11325, false,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.MY2ARM_HERBPATCH_COMPOST_TRANSMIT, PatchImplementation.HERB)
		));

		add(new FarmingRegion("Farming Guild", 5021, true,
			new FarmingPatch("Hespori", VarbitID.FARMING_TRANSMIT_J, -1, PatchImplementation.HESPORI)
		));

		//Full 3x3 region area centered on farming guild
		add(farmingGuildRegion = new FarmingRegion("Farming Guild", 4922, true,
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_G, VarbitID.FARMING_TREE_COMPOST_6_TRANSMIT, PatchImplementation.TREE, NpcID.FARMING_GARDENER_FARMGUILD_T2),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_E, VarbitID.FARMING_COMPOST_VARBIT_HERB_7_TRANSMIT, PatchImplementation.HERB),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_B, VarbitID.FARMING_COMPOST_VARBIT_BUSH_5_TRANSMIT, PatchImplementation.BUSH, NpcID.FARMING_GARDENER_FARMGUILD_T1, 3),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_H, VarbitID.FARMING_COMPOST_VARBIT_FLOWER_6_TRANSMIT, PatchImplementation.FLOWER),
			new FarmingPatch("North", VarbitID.FARMING_TRANSMIT_C, VarbitID.FARMING_COMPOST_VARBIT_VEG_12_TRANSMIT, PatchImplementation.ALLOTMENT, NpcID.FARMING_GARDENER_FARMGUILD_T1, 1),
			new FarmingPatch("South", VarbitID.FARMING_TRANSMIT_D, VarbitID.FARMING_COMPOST_VARBIT_VEG_13_TRANSMIT, PatchImplementation.ALLOTMENT, NpcID.FARMING_GARDENER_FARMGUILD_T1, 2),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_N, -1, PatchImplementation.BIG_COMPOST),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_F, VarbitID.FARMING_COMPOST_VARBIT_CACTUS_2_TRANSMIT, PatchImplementation.CACTUS, NpcID.FARMING_GARDENER_FARMGUILD_T1, 0),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_SPIRIT_TREE_5_TRANSMIT, PatchImplementation.SPIRIT_TREE, NpcID.FARMING_GARDENER_SPIRIT_TREE_5),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_K, VarbitID.FARMING_COMPOST_VARBIT_FRUIT_TREE_6_TRANSMIT, PatchImplementation.FRUIT_TREE, NpcID.FARMING_GARDENER_FARMGUILD_T3),
			new FarmingPatch("Anima", VarbitID.FARMING_TRANSMIT_M, -1, PatchImplementation.ANIMA),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_L, VarbitID.FARMING_CELASTRUS_COMPOST_1_TRANSMIT, PatchImplementation.CELASTRUS, NpcID.FARMING_GARDENER_FARMGUILD_CELASTRUS),
			new FarmingPatch("", VarbitID.FARMING_TRANSMIT_I, VarbitID.FARMING_REDWOOD_COMPOST_TRANSMIT, PatchImplementation.REDWOOD, NpcID.FARMING_GARDENER_FARMGUILD_REDWOOD)
		), 5177, 5178, 5179, 4921, 4923, 4665, 4666, 4667);

		//All of Prifddinas, and all of Prifddinas Underground
		add(new FarmingRegion("Prifddinas", 13151, false,
				new FarmingPatch("North", VarbitID.FARMING_TRANSMIT_A, VarbitID.FARMING_COMPOST_VARBIT_VEG_14_TRANSMIT, PatchImplementation.ALLOTMENT, NpcID.PRIF_GARDENER, 0),
				new FarmingPatch("South", VarbitID.FARMING_TRANSMIT_B, VarbitID.FARMING_COMPOST_VARBIT_VEG_15_TRANSMIT, PatchImplementation.ALLOTMENT, NpcID.PRIF_GARDENER, 1),
				new FarmingPatch("", VarbitID.FARMING_TRANSMIT_C, VarbitID.FARMING_COMPOST_VARBIT_FLOWER_7_TRANSMIT, PatchImplementation.FLOWER),
				new FarmingPatch("", VarbitID.FARMING_TRANSMIT_E, VarbitID.FARMING_CRYSTAL_TREE_COMPOST_1_TRANSMIT, PatchImplementation.CRYSTAL_TREE),
				new FarmingPatch("", VarbitID.FARMING_TRANSMIT_D, -1, PatchImplementation.COMPOST)
			), 12895, 12894, 13150,
			/* Underground */ 12994, 12993, 12737, 12738, 12126, 12127, 13250);

		// Finalize
		this.regions = Multimaps.unmodifiableMultimap(this.regions);
		Map<Tab, Set<FarmingPatch>> umtabs = new TreeMap<>();
		for (Map.Entry<Tab, Set<FarmingPatch>> e : tabs.entrySet())
		{
			umtabs.put(e.getKey(), Collections.unmodifiableSet(e.getValue()));
		}
		this.tabs = Collections.unmodifiableMap(umtabs);
	}

	private void add(FarmingRegion r, int... extraRegions)
	{
		regions.put(r.getRegionID(), r);
		for (int er : extraRegions)
		{
			regions.put(er, r);
		}
		for (FarmingPatch p : r.getPatches())
		{
			tabs
				.computeIfAbsent(p.getImplementation().getTab(), k -> new TreeSet<>(tabSorter))
				.add(p);
		}
	}

	Collection<FarmingRegion> getRegionsForLocation(WorldPoint location)
	{
		return this.regions.get(location.getRegionID()).stream()
			.filter(region -> region.isInBounds(location))
			.collect(Collectors.toSet());
	}

	FarmingPatch getPatchForCompostVarbit(int varbitId)
	{
		// TODO: Lookup performance
		for (FarmingRegion region : regions.values())
		{
			for (FarmingPatch patch : region.getPatches())
			{
				if (patch.getCompostVarbit() == varbitId)
				{
					return patch;
				}
			}
		}
		return null;
	}
}
