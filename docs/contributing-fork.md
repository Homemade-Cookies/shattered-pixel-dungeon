# Forking & Contributing — Shattered Pixel Dungeon

> **Note:** The upstream repository at `github.com/00-Evan/shattered-pixel-dungeon` does **not** accept pull requests. This guide is for developers building their own fork.

## Setting Up Your Fork

1. Fork the repo on GitHub (or clone directly for a private fork)
2. Follow the platform-specific build guides:
   - [Android](getting-started-android.md)
   - [Desktop](getting-started-desktop.md)
   - [iOS](getting-started-ios.md)
3. Make the [recommended changes](recommended-changes.md) to distinguish your fork

## Recommended First Changes

See `docs/recommended-changes.md` for the upstream-suggested list. Key changes:

| Change | Location | Why |
|--------|----------|-----|
| App package name | `build.gradle` `appPackageName` | Avoid conflicts with the official app on device |
| App name | `build.gradle` `appName` | Distinguish in launcher |
| Version code/name | `build.gradle` | Avoid confusing users |
| Update/news service | `services/` | Point to your own update infra or disable |
| Google Play billing | `android/` | Remove if not distributing on Play |

## Understanding the Codebase Before Changing

Read these docs first:
- [Architecture](architecture.md) — module structure and layer responsibilities
- [Game Loop](game-loop.md) — actor system, turn order, mob AI
- [Item System](item-system.md) — item hierarchy and how to add items
- [Level Generation](level-generation.md) — procedural gen pipeline

## Common Fork Tasks

### Adding a New Hero Class

1. Add enum value to `HeroClass` in `actors/hero/HeroClass.java`
2. Implement starting inventory in `HeroClass.startingInventory()`
3. Add subclass values to `HeroSubClass`
4. Add abilities in `actors/hero/abilities/yourclass/`
5. Add talents in `Talent.java`
6. Add localization in `assets/messages/actors/hero.properties`
7. Add splash art in `assets/splashes/`

### Adding a New Item

See [item-system.md](item-system.md#how-to-add-a-new-item).

### Adding a New Mob

1. Create in `actors/mobs/`
2. Extend `Mob`
3. Set `HP`, `defenseSkill`, `EXP`, `maxLvl` in initializer
4. Implement `damageRoll()`, `attackSkill()`, `drRoll()`
5. Create sprite class in `sprites/`
6. Add localization in `assets/messages/actors/mobs.properties`
7. Add to spawn tables in appropriate `Level` subclass

### Adding a New Level

1. Create in `levels/`
2. Extend `RegularLevel` (for standard procedural levels) or `Level` directly
3. Override `builder()` to return your chosen `Builder`
4. Override `painter()` to return your room painter
5. Override `initRooms()` to define room mix
6. Wire to a depth in `Dungeon.levelClass(int depth)`

### Modifying the HUD

UI components live in `scenes/GameScene.java` and `ui/`. Windows are in `windows/`. Extend or modify as needed — `GameScene` composes the full HUD.

## Save Compatibility

If you add fields to any `Bundlable` class, you **must** handle upgrade from saves that don't have those fields:

```java
@Override
public void restoreFromBundle(Bundle bundle) {
    super.restoreFromBundle(bundle);
    // Check before reading — old saves won't have this key
    if (bundle.contains(MY_NEW_FIELD)) {
        myNewField = bundle.getInt(MY_NEW_FIELD);
    } else {
        myNewField = DEFAULT_VALUE; // sensible default for old saves
    }
}
```

Breaking save compatibility is acceptable when changing core systems, but warn players.

## Localization

Your fork can add or modify locale strings. Each locale has its own directory under `assets/messages/`. The English `en` files are the fallback. Format:

```
assets/messages/
├── actors/
│   ├── hero.properties        (English)
│   ├── hero_zh-rCN.properties (Chinese)
│   └── ...
├── items/
│   └── ...
└── ...
```

Supported locales for locale-specific files: `zh-rCN, ko, ru, es, pt, fr, de, zh-rTW, ja, pl, it, tr, vi, uk, id, cs, nl, sv, hu, fi, el, be, eo`.

## Distributing on Google Play

Read the warning at the end of [getting-started-android.md](getting-started-android.md). You **must** change the app package name and must not use Shattered Pixel Dungeon branding without permission.

## License

This project is GPL-3.0. Any distributed fork must also be GPL-3.0 and include source code. See `LICENSE.txt`.
