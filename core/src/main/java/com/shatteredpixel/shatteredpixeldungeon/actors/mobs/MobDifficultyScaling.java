/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Difficulty;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;

/**
 * Scales mob stats based on difficulty setting.
 * Applied at mob creation time via {@link #scaleMob(Mob)}.
 */
public class MobDifficultyScaling {

	private static final int LEVELS_PER_DEPTH = 2;
	private static final float MAX_OVER_LEVEL_BONUS = 1.0f;
	private static final float OVER_LEVEL_HP_PER_DELTA = 0.10f;
	private static final float UNDER_LEVEL_HP_PER_DELTA = 0.05f;
	private static final float MIN_HP_FRACTION = 0.50f;

	/**
	 * Apply difficulty-based scaling to a mob.
	 * Scales HP based on two axes:
	 * 1. Hero level vs expected depth level (level-delta scaling)
	 * 2. Difficulty multiplier from the run's selected difficulty
	 *
	 * Safe to call before mob is added to the level or has any state set.
	 * Returns early if hero is null or mob has no HP.
	 */
	public static void scaleMob(Mob mob) {
		if (Dungeon.hero == null) return;
		if (mob.HT <= 0) return;

		int heroLevel = Dungeon.hero.lvl;
		int depth = Dungeon.depth;
		int expectedLevel = depth * LEVELS_PER_DEPTH;
		int delta = heroLevel - expectedLevel;

		// Step 1: level-delta adjustment
		float levelMultiplier;
		if (delta > 0) {
			// Hero is over-levelled; make mobs tougher.
			float bonus = Math.min(delta * OVER_LEVEL_HP_PER_DELTA, MAX_OVER_LEVEL_BONUS);
			levelMultiplier = 1.0f + bonus;
		} else if (delta < 0) {
			// Hero is under-levelled; give them a slight reprieve.
			float reduction = Math.min((-delta) * UNDER_LEVEL_HP_PER_DELTA,
					1.0f - MIN_HP_FRACTION);
			levelMultiplier = 1.0f - reduction;
		} else {
			levelMultiplier = 1.0f;
		}

		// Step 2: difficulty multiplier
		Difficulty diff = (Dungeon.difficulty != null) ? Dungeon.difficulty : Difficulty.NORMAL;
		float finalMultiplier = levelMultiplier * diff.hpMultiplier;

		// Apply
		int newHT = Math.max(1, Math.round(mob.HT * finalMultiplier));
		mob.HT = newHT;
		mob.HP = newHT;
	}
}
