# Code Templates — Shattered Pixel Dungeon

Handlebars-style templates for common game extension patterns.

## Available Templates

| Template | File | Purpose |
|----------|------|---------|
| New Item | `java/new-item.hbs` | Generic item subclass with bundle serialization |
| New Mob | `java/new-mob.hbs` | Enemy mob with AI hooks |
| New Buff | `java/new-buff.hbs` | Timed status effect |

## Usage

Templates use `{{VariableName}}` syntax. Copy the template to the appropriate source directory and replace variables.

### Quick Example — New Potion

1. Copy `java/new-item.hbs` → `core/.../items/potions/PotionOfAwesome.java`
2. Replace `{{Name}}` → `PotionOfAwesome`
3. Change parent class from `Item` → `Potion`
4. Add your use logic in `execute()`
5. Add localization keys in `assets/messages/items/potions.properties`:
   ```properties
   com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfAwesome.name=Potion of Awesome
   com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfAwesome.desc=A very awesome potion.
   ```

## Key Patterns

### storeInBundle / restoreFromBundle
Every class with persistent fields must implement these. Use the parent's bundle key names as a reference — don't reuse existing keys or you'll corrupt save data.

### Messages
Always use `Messages.get(this, "key")` for user-visible text. Never hardcode strings.

### Sprites
Set `image = ItemSpriteSheet.SOME_CONSTANT` in the initializer block. Sprite sheets are in `core/src/main/assets/sprites/`.
