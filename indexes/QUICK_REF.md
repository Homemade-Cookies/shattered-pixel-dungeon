# Quick Reference — Shattered Pixel Dungeon

Common tasks and where to find things.

## Most-Edited Files / Hotspots

| What | Path |
|------|------|
| Game entry point | `core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/ShatteredPixelDungeon.java` |
| Dungeon state | `core/.../Dungeon.java` |
| Hero | `core/.../actors/hero/Hero.java` |
| Main gameplay scene | `core/.../scenes/GameScene.java` |
| Item base class | `core/.../items/Item.java` |
| Mob base class | `core/.../actors/mobs/Mob.java` |
| Level base class | `core/.../levels/Level.java` |
| Buff base class | `core/.../actors/buffs/Buff.java` |
| Localization lookup | `core/.../messages/Messages.java` |
| Tile terrain constants | `core/.../tiles/Terrain.java` (or similar) |
| Android launcher | `android/src/main/java/.../AndroidLauncher.java` |
| Desktop launcher | `desktop/src/main/java/.../DesktopLauncher.java` |

## Common Build Commands

```bash
# Run game (desktop, debug)
./gradlew desktop:debug

# Build Android APK (debug)
./gradlew android:assembleDebug

# Build Android APK (release)
./gradlew android:assembleRelease

# Package desktop (all platforms)
./gradlew desktop:jpackageImage

# Clean
./gradlew clean
```

## Adding a New Item

1. Create class in appropriate `items/` subpackage
2. Extend the nearest parent class (e.g., `MeleeWeapon`, `Potion`, `Scroll`)
3. Override `storeInBundle` / `restoreFromBundle` if you add fields
4. Add localization keys to `assets/messages/items/`
5. Add sprite reference if needed in `sprites/ItemSpriteSheet.java`
6. Register in generator/loot tables where relevant

## Adding a New Mob

1. Create class in `actors/mobs/`
2. Extend `Mob` (or a relevant sub-type)
3. Implement `act()` for AI behavior
4. Add sprite class in `sprites/`
5. Add localization keys in `assets/messages/actors/`
6. Add to appropriate level spawn tables

## Adding a New Buff

1. Create class in `actors/buffs/`
2. Extend `Buff`, `FlavourBuff`, or a relevant sub-type
3. Override `act()` for turn-based effect
4. Apply via: `Buff.affect(char, YourBuff.class)`

## Adding a New Level Room

1. Create class in appropriate `levels/rooms/` sub-package
2. Extend `Room`
3. Implement `paint(Level level)` for tile-filling logic
4. Register in appropriate `LevelBuilder` or room lists

## Localization Pattern

```java
// In a class method
Messages.get(this, "key");           // instance — uses this.getClass()
Messages.get(MyClass.class, "key");  // static reference

// properties file: assets/messages/items/myitem.properties
// com.shatteredpixel.shatteredpixeldungeon.items.MyItem.key=Value
```

## Serialization Pattern

```java
@Override
public void storeInBundle(Bundle bundle) {
    super.storeInBundle(bundle);
    bundle.put("myField", myField);
}

@Override
public void restoreFromBundle(Bundle bundle) {
    super.restoreFromBundle(bundle);
    myField = bundle.getInt("myField");
}
```

## Key Constants / Enums

| Constant/Enum | Location | Purpose |
|---------------|----------|---------|
| `Terrain` | `tiles/Terrain.java` | Tile type constants |
| `HeroClass` | `actors/hero/HeroClass.java` | Warrior, Mage, Rogue, Huntress, Duelist, Cleric |
| `HeroSubClass` | `actors/hero/HeroSubClass.java` | Subclass choices |
| `Talent` | `actors/hero/Talent.java` | All talent definitions |

_Generated 2026-05-18_
