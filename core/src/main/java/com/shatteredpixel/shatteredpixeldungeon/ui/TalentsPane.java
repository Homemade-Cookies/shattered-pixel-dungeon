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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TalentsPane extends ScrollPane {

	ArrayList<TalentTierPane> panes = new ArrayList<>();
	ArrayList<ColorBlock> separators = new ArrayList<>();

	ColorBlock sep;
	ColorBlock blocker;
	RenderedTextBlock blockText;

	public TalentsPane( TalentButton.Mode mode ) {
		this( mode, Dungeon.hero.talents );
	}

	public TalentsPane( TalentButton.Mode mode, ArrayList<LinkedHashMap<Talent, Integer>> talents ) {
		super(new Component());

		Ratmogrify.useRatroicEnergy = Dungeon.hero != null && Dungeon.hero.armorAbility instanceof Ratmogrify;

		int tiersAvailable = 1;

		if (mode == TalentButton.Mode.INFO){
			if (!Badges.isUnlocked(Badges.Badge.LEVEL_REACHED_1)){
				tiersAvailable = 1;
			} else if (!Badges.isUnlocked(Badges.Badge.LEVEL_REACHED_2) || !Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_2)){
				tiersAvailable = 2;
			} else if (!Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_4)){
				tiersAvailable = 3;
			} else {
				tiersAvailable = Talent.MAX_TALENT_TIERS;
			}
		} else {
			while (tiersAvailable < Talent.MAX_TALENT_TIERS
					&& Dungeon.hero.lvl+1 >= Talent.tierLevelThresholds[tiersAvailable+1]){
				tiersAvailable++;
			}
			if (tiersAvailable > 2 && Dungeon.hero.subClass == HeroSubClass.NONE){
				tiersAvailable = 2;
			} else if (tiersAvailable > 3 && Dungeon.hero.armorAbility == null){
				tiersAvailable = 3;
			}
		}

		tiersAvailable = Math.min(tiersAvailable, talents.size());

		// For INFO mode, only show tiers up to tiersAvailable (badge-gated).
		// For all other modes (UPGRADE, METAMORPH, etc.), show every tier that has
		// already been initialized in the talents list so the player can preview
		// upcoming talents. The level/subclass gate is enforced inside each
		// TalentButton — locked tiers are visible and readable but cannot be leveled.
		int tiersToShow = (mode == TalentButton.Mode.INFO) ? tiersAvailable : talents.size();

		for (int i = 0; i < tiersToShow; i++){
			if (talents.get(i).isEmpty()) continue;

			TalentTierPane pane = new TalentTierPane(talents.get(i), i+1, mode);
			panes.add(pane);
			content.add(pane);

			ColorBlock sep = new ColorBlock(0, 1, 0xFF000000);
			separators.add(sep);
			content.add(sep);
		}

		sep = new ColorBlock(0, 1, 0xFF000000);
		content.add(sep);

		blocker = new ColorBlock(0, 0, 0xFF222222);
		content.add(blocker);

		if (mode == TalentButton.Mode.INFO) {
			// INFO mode: gate block text by badge progress as before
			if (tiersAvailable == 1) {
				blockText = PixelScene.renderTextBlock(Messages.get(this, "unlock_tier2"), 6);
				content.add(blockText);
			} else if (tiersAvailable == 2) {
				blockText = PixelScene.renderTextBlock(Messages.get(this, "unlock_tier3"), 6);
				content.add(blockText);
			} else if (tiersAvailable == 3) {
				blockText = PixelScene.renderTextBlock(Messages.get(this, "unlock_tier4"), 6);
				content.add(blockText);
			} else {
				blockText = null;
			}
		} else {
			// UPGRADE/METAMORPH modes: only block for tiers not yet initialized.
			// Tiers 1 & 2 are always present; tier 3 appears after subclass selection;
			// tier 4 appears after an armor ability is chosen.
			if (talents.size() < 3) {
				blockText = PixelScene.renderTextBlock(Messages.get(this, "unlock_tier3"), 6);
				content.add(blockText);
			} else if (talents.size() < 4) {
				blockText = PixelScene.renderTextBlock(Messages.get(this, "unlock_tier4"), 6);
				content.add(blockText);
			} else {
				blockText = null;
			}
		}

		for (int i = panes.size()-1; i >= 0; i--){
			content.bringToFront(panes.get(i));
		}
	}

	@Override
	protected void layout() {
		super.layout();

		float top = 0;
		for (int i = 0; i < panes.size(); i++){
			top += 2;
			panes.get(i).setRect(x, top, width, 0);
			top = panes.get(i).bottom();

			separators.get(i).x = 0;
			separators.get(i).y = top + 2;
			separators.get(i).size(width, 1);

			top += 3;

		}

		float bottom;
		if (blockText != null) {
			bottom = Math.max(height, top + 20);

			blocker.x = 0;
			blocker.y = top;
			blocker.size(width, bottom - top);

			blockText.maxWidth((int) width);
			blockText.align(RenderedTextBlock.CENTER_ALIGN);
			blockText.setPos((width - blockText.width()) / 2f, blocker.y + (bottom - blocker.y - blockText.height()) / 2);
		} else {
			bottom = Math.max(height, top);

			blocker.visible = false;
		}

		content.setSize(width, bottom);
	}

	public static class TalentTierPane extends Component {

		private int tier;

		public RenderedTextBlock title;
		Image heroIcon;
		ArrayList<TalentButton> buttons;

		ArrayList<Image> stars = new ArrayList<>();
		IconButton random;

		public TalentTierPane(LinkedHashMap<Talent, Integer> talents, int tier, TalentButton.Mode mode){
			super();

			this.tier = tier;

			title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(TalentsPane.class, "tier", tier)), 9);
			title.hardlight(Window.TITLE_COLOR);
			add(title);

			if (Dungeon.hero != null) {
				heroIcon = new Image(Dungeon.hero.heroClass.spritesheet(), 0, 90, 12, 15);
				add(heroIcon);
			}

			if (mode == TalentButton.Mode.UPGRADE) {
				setupStars();
				if (Dungeon.hero.talentPointsAvailable(tier) > 0){

					random = new IconButton(Icons.SHUFFLE.get()){
						@Override
						protected void onClick() {
							super.onClick();
							GameScene.show(new WndOptions(
									Icons.SHUFFLE.get(),
									Messages.get(TalentsPane.class, "random_title"),
									Messages.get(TalentsPane.class, "random_sure"),
									Messages.get(TalentsPane.class, "random_yes"),
									Messages.get(TalentsPane.class, "random_one"),
									Messages.get(TalentsPane.class, "random_no")) {
								@Override
								protected void onSelect(int index) {
									super.onSelect(index);
									//safety check to ensure previous UI is still there
									if (TalentTierPane.this.parent == null){
										return;
									}
									if (index == 0 || index == 1){
										while (Dungeon.hero.talentPointsAvailable(tier) > 0){
											TalentButton button = Random.element(buttons);
											if (Dungeon.hero.pointsInTalent(button.talent) < button.talent.maxPoints()){
												button.upgradeTalent();
												if (index == 1){
													break;
												}
											}
										};
										setupStars();
										TalentTierPane.this.layout();
									}
								}
							});
						};

						@Override
						public void update() {
							if (Dungeon.hero.lvl >= 3 && Statistics.qualifiedForRandomVictoryBadge){
								icon.tint(1, 1, 1, (float)Math.abs(Math.cos(1.5f*Math.PI*Game.timeTotal)/2f));
							}
							super.update();
						}
					};
					add(random);
				}
			}

			buttons = new ArrayList<>();
			for (Talent talent : talents.keySet()){
				TalentButton btn = new TalentButton(tier, talent, talents.get(talent), mode){
					@Override
					public void upgradeTalent() {
						super.upgradeTalent();
						if (parent != null) {
							setupStars();
							TalentTierPane.this.layout();
						}
					}
				};
				buttons.add(btn);
				add(btn);
			}

		}

		private void setupStars(){
			if (!stars.isEmpty()){
				for (Image im : stars){
					im.killAndErase();
				}
				stars.clear();
			}

			int totStars = Talent.tierLevelThresholds[tier+1] - Talent.tierLevelThresholds[tier] + Dungeon.hero.bonusTalentPoints(tier);
			int openStars = Dungeon.hero.talentPointsAvailable(tier);
			int usedStars = Dungeon.hero.talentPointsSpent(tier);
			for (int i = 0; i < totStars; i++){
				Image im = new Speck().image(Speck.STAR);
				stars.add(im);
				add(im);
				if (i >= openStars && i < (openStars + usedStars)){
					im.tint(0.75f, 0.75f, 0.75f, 0.9f);
				} else if (i >= (openStars + usedStars)){
					im.tint(0f, 0f, 0f, 0.9f);
				}
			}

			if (random != null && openStars == 0){
				random.killAndErase();
				random.destroy();
				random = null;
			}
		}

		@Override
		protected void layout() {
			super.layout();

			int regStars = Talent.tierLevelThresholds[tier+1] - Talent.tierLevelThresholds[tier];

			float iconExtraWidth = heroIcon != null ? (heroIcon.width() + 2) : 0;
			float titleWidth = title.width() + iconExtraWidth;
			titleWidth += 2 + Math.min(stars.size(), regStars)*6;
			title.setPos(x + (width - titleWidth)/2f + iconExtraWidth, y);

			if (heroIcon != null) {
				heroIcon.x = title.left() - heroIcon.width() - 2;
				heroIcon.y = title.top() + (title.height() - heroIcon.height()) / 2f;
				PixelScene.align(heroIcon);
			}

			float left = title.right() + 2;

			float starTop = title.top();
			if (regStars < stars.size()) starTop -= 2;

			for (Image star : stars){
				star.x = left;
				star.y = starTop;
				PixelScene.align(star);
				left += 6;
				regStars--;
				if (regStars == 0){
					starTop += 6;
					left = title.right() + 2;
				}
			}

			if (random != null){
				random.setRect(width - 16, y-2, 16, 14);
			}

			// Wrap buttons into rows of up to 4 (the standard number of talents per tier).
			// When cross-class talents push the count beyond 4, extra buttons spill onto
			// additional rows so nothing gets squeezed or clipped.
			int buttonsPerRow = Math.min(buttons.size(), 4);
			int numRows = (int) Math.ceil(buttons.size() / (float) buttonsPerRow);

			float rowGap = (width - buttonsPerRow * TalentButton.WIDTH) / (buttonsPerRow + 1);
			float rowTop = title.bottom() + 4;
			for (int row = 0; row < numRows; row++) {
				int rowStart = row * buttonsPerRow;
				int rowEnd = Math.min(rowStart + buttonsPerRow, buttons.size());
				int rowCount = rowEnd - rowStart;
				float rowGapActual = (width - rowCount * TalentButton.WIDTH) / (rowCount + 1);
				left = x + rowGapActual;
				for (int i = rowStart; i < rowEnd; i++) {
					TalentButton btn = buttons.get(i);
					btn.setPos(left, rowTop);
					PixelScene.align(btn);
					left += btn.width() + rowGapActual;
				}
				rowTop += TalentButton.HEIGHT + 2;
			}

			height = buttons.get(buttons.size() - 1).bottom() - y;

		}

	}
}
