# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Run game on desktop (debug, with live reload)
./gradlew desktop:debug

# Build Android APK
./gradlew android:assembleDebug      # debug APK
./gradlew android:assembleRelease    # release APK (requires signing config)

# Package desktop for distribution
./gradlew desktop:release            # fat JAR
./gradlew desktop:jpackageImage      # native image via jpackage

# Build iOS (macOS only, requires RoboVM)
./gradlew ios:createIPA

# Clean
./gradlew clean
```

There are no automated tests. QA is manual.

## Architecture

The repo is a multi-platform libGDX game split into Gradle modules:

- **`SPD-classes`** — engine layer (`com.watabou.*`): Noosa scene graph, OpenGL wrappers, input, utilities. Contains zero game logic.
- **`core`** — all game logic (`com.shatteredpixel.shatteredpixeldungeon.*`): actors, items, levels, scenes, UI.
- **`android` / `desktop` / `ios`** — platform launchers; delegate entirely to `core`. No game logic here.
- **`services`** — update-check and news-feed abstractions with `debug*` (no-op) and real implementations wired per build type.

### Key Design Patterns

**Actor loop (turn-based):** `Actor` subclasses hold a floating-point `time` field. The loop always processes the actor with the lowest `time`, then calls `act()`. After acting, the actor calls `spend(n)` to advance its own clock. Priority (`actPriority`) breaks ties: VFX (100) → Hero (0) → Blobs (-10) → Mobs (-20) → Buffs (-30).

**Scene graph:** All visual objects extend `Gizmo` (or `Visual` / `Group`). Screens extend `PixelScene`. `ShatteredPixelDungeon extends Game` is the `ApplicationListener` entry point; `Dungeon` is the static singleton holding active-run state (hero, level, depth, seed, actors).

**Serialization:** Every persistable class implements `Bundlable` — `storeInBundle(Bundle)` and `restoreFromBundle(Bundle)`. This is a custom binary format, not JSON. Save-upgrade logic lives inside `restoreFromBundle`: always guard new fields with `bundle.contains(KEY)` and provide a default for old saves. Never reuse a bundle key from a parent class.

**Localization:** All user-visible strings go through `Messages.get(SomeClass.class, "key")` (never hardcoded). Properties files live in `core/src/main/assets/messages/` and are scoped by class name: `com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing.name=Potion of Healing`.

### Core Package Map

| Package | Contents |
|---------|----------|
| `actors/hero` | `Hero`, `HeroClass` (WARRIOR/MAGE/ROGUE/HUNTRESS/DUELIST/CLERIC), `HeroSubClass`, `Talent`, `Belongings` |
| `actors/mobs` | Enemy types extending `Mob`; AI via `AiState` (Sleeping→Wandering→Hunting→Fleeing) |
| `actors/buffs` | Status effects attached to `Char`; applied via `Buff.affect(char, BuffClass.class)` |
| `actors/blobs` | Area effects on the tile grid (Fire, ToxicGas, etc.); stored in `Level.blobs` |
| `items/` | Deep hierarchy rooted at `Item`; weapons, armor, potions, scrolls, wands, rings, artifacts, trinkets |
| `levels/` | Procedural gen: `RegularLevel.create()` → Builder (room graph) → Painter (tile fill) → traps/items/mobs |
| `scenes/` | Game screens extending `PixelScene` (TitleScene, GameScene, InterlevelScene, etc.) |
| `messages/` | `Messages` localization class |

### Level Generation

`Level.map` is a flat `int[]` of `width × height` tiles. `Terrain.java` defines tile constants. Level types map to dungeon depths: `SewerLevel` (1–4), boss levels at 5/10/15/20/25, `PrisonLevel` (6–9), `CavesLevel` (11–14), `CityLevel` (16–19), `HallsLevel` (21–24). Builders (`RegularBuilder`, `LoopBuilder`, `LineBuilder`, etc.) lay out the room graph; `Painter` subclasses fill tiles per room.

## Fork-Specific Configuration

Key variables in the root `build.gradle`:
- `appName`, `appPackageName` — must be changed when distributing a fork
- `appVersionCode` — never decrement; compatibility code references specific version numbers (see `ShatteredPixelDungeon.java` constants like `v2_5_4`, `v3_3_0`)
- `appVersionName` — user-visible string

Services to change for a fork:
- **Update checker:** swap `:services:updates:githubUpdates` for `:services:updates:debugUpdates` in release configs, or edit `GitHubUpdates.java` to point to your repo
- **News feed:** disable `btnNews` in `TitleScene.java`, or edit `ShatteredNews.java` to point to your atom feed
- **Supporter button:** disable `btnSupport` in `TitleScene.java` if distributing on Google Play (Google removes apps that mention Patreon)

Assets to swap for visual identity:
- Title graphic: `core/src/main/assets/interfaces/banners.png`
- Icons: `android/src/main/res/`, `android/src/debug/res/`, `desktop/src/main/assets/icons/`, `ios/assets/`

Credits: `AboutScene.java`. Supporter nag: `WndSupportPrompt.java`, triggered from `WornKey.java`.
