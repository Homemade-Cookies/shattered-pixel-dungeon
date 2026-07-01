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

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public enum Difficulty {

	EASY   (0.75f, 0.75f, false),
	NORMAL (1.00f, 1.00f, true),
	HARD   (1.25f, 1.25f, true),
	ENDLESS(1.50f, 1.50f, true);

	public final float hpMultiplier;
	public final float damageMultiplier;
	public final boolean hungerEnabled;

	Difficulty(float hp, float dmg, boolean hunger) {
		this.hpMultiplier     = hp;
		this.damageMultiplier = dmg;
		this.hungerEnabled    = hunger;
	}

	public String label() {
		return Messages.get(Difficulty.class, name().toLowerCase() + "_name");
	}

	public String description() {
		return Messages.get(Difficulty.class, name().toLowerCase() + "_desc");
	}
}
