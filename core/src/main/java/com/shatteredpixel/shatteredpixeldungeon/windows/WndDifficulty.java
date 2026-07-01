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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Difficulty;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndDifficulty extends Window {

	private static final int WIDTH = 120;
	private static final int MARGIN = 4;

	private Difficulty selected = Dungeon.difficulty;
	private boolean hungerOn = Dungeon.hungerEnabled;
	private RedButton hungerToggle;
	private RedButton[] diffButtons;

	public WndDifficulty() {
		super();

		RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 8);
		title.hardlight(TITLE_COLOR);
		add(title);

		diffButtons = new RedButton[4];
		Difficulty[] difficulties = Difficulty.values();
		float yPos = title.bottom() + MARGIN * 2;

		for (int i = 0; i < difficulties.length; i++) {
			final Difficulty d = difficulties[i];

			diffButtons[i] = new RedButton(d.label()) {
				@Override
				protected void onClick() {
					super.onClick();
					selected = d;
					hungerOn = d.hungerEnabled;
					updateHungerToggle();
					updateDifficultyButtons();
				}
			};

			add(diffButtons[i]);
			diffButtons[i].setRect(MARGIN, yPos, WIDTH - MARGIN * 2, 16);

			RenderedTextBlock desc = PixelScene.renderTextBlock(d.description(), 6);
			desc.maxWidth(WIDTH - MARGIN * 2);
			add(desc);
			desc.setPos(MARGIN, diffButtons[i].bottom() + 1);

			yPos = desc.bottom() + MARGIN;
		}

		updateDifficultyButtons();

		hungerToggle = new RedButton(hungerLabel()) {
			@Override
			protected void onClick() {
				super.onClick();
				hungerOn = !hungerOn;
				text(hungerLabel());
			}
		};
		add(hungerToggle);
		hungerToggle.setRect(MARGIN, yPos, WIDTH - MARGIN * 2, 16);
		yPos = hungerToggle.bottom() + MARGIN * 2;

		RedButton confirm = new RedButton(Messages.get(this, "btn_confirm")) {
			@Override
			protected void onClick() {
				super.onClick();
				Dungeon.difficulty = selected;
				Dungeon.hungerEnabled = hungerOn;
				hide();
			}
		};
		add(confirm);
		confirm.setRect(MARGIN, yPos, WIDTH - MARGIN * 2, 16);

		resize(WIDTH, (int) (confirm.bottom() + MARGIN));
	}

	private void updateHungerToggle() {
		if (hungerToggle != null) {
			hungerToggle.text(hungerLabel());
		}
	}

	private String hungerLabel() {
		return hungerOn
				? Messages.get(this, "hunger_on")
				: Messages.get(this, "hunger_off");
	}

	private void updateDifficultyButtons() {
		Difficulty[] difficulties = Difficulty.values();
		for (int i = 0; i < diffButtons.length; i++) {
			if (difficulties[i] == selected) {
				diffButtons[i].textColor(0xFFFF00);
			} else {
				diffButtons[i].textColor(0xFFFFFF);
			}
		}
	}
}
