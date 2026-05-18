# AGENTS.md — Shattered Pixel Dungeon

AI assistant context for the `shattered-pixel-dungeon` repository.

## Project Overview

**Shattered Pixel Dungeon** is an open-source traditional roguelike built on libGDX.
It targets Android, iOS, and Desktop (Windows/macOS/Linux). The codebase is Java 11 using Gradle.

- Version: `3.3.8` (versionCode `896`)
- Package: `com.shatteredpixel.shatteredpixeldungeon`
- Engine: libGDX 1.14.0 with Noosa scene graph (in `SPD-classes`)

## Repository Structure

```
shattered-pixel-dungeon/
├── SPD-classes/        # Shared libGDX engine layer (com.watabou.*)
│   └── src/main/java/com/watabou/
│       ├── noosa/      # Scene graph, cameras, visual objects
│       ├── glwrap/     # OpenGL wrappers
│       ├── gltextures/ # Texture management
│       ├── input/      # Input handling
│       └── utils/      # Utilities (PointF, Random, Callback, etc.)
├── core/               # All game logic (com.shatteredpixel.shatteredpixeldungeon.*)
│   └── src/main/java/com/shatteredpixel/shatteredpixeldungeon/
│       ├── actors/     # Game entities (Hero, Mob, Buff, Blob, NPC, Ability, Spell)
│       ├── items/      # All collectibles (weapons, armor, potions, scrolls, etc.)
│       ├── levels/     # Level generation (builders, painters, rooms, traps)
│       ├── scenes/     # Game screens (GameScene, TitleScene, etc.)
│       ├── ui/         # HUD and UI components
│       ├── windows/    # Dialog/popup windows
│       ├── effects/    # Visual effects
│       ├── sprites/    # Actor/item visual sprites
│       ├── messages/   # Localization system
│       ├── journal/    # Lore catalog
│       ├── mechanics/  # Shared game mechanic utilities
│       ├── plants/     # Plant entities
│       └── tiles/      # Tile types and custom tile rendering
├── android/            # Android platform module
├── desktop/            # Desktop platform module (LWJGL3)
├── ios/                # iOS platform module (RoboVM)
├── services/           # Update/news service abstraction
│   ├── updates/        # debugUpdates, githubUpdates
│   └── news/           # debugNews, shatteredNews
├── docs/               # Getting-started guides
└── project-context.yaml
```

## Key Architectural Concepts

- **Noosa scene graph** (`SPD-classes`): All visual objects extend `Visual` → `Gizmo`. Scenes extend `PixelScene`.
- **Actor system** (`core/actors`): Turn-based actor loop. `Actor` is the base; `Char` extends it for characters. `Hero` and `Mob` are the main character types. `Buff` objects are attached to `Char`s.
- **Item system** (`core/items`): `Item` base class with `execute()` for use actions. Extensive hierarchy: weapons (melee/missiles/enchantments), armor (glyphs/curses), potions (standard/exotic/elixirs/brews), scrolls (standard/exotic), wands, rings, artifacts, trinkets, etc.
- **Level generation** (`core/levels`): Procedural. `Level` class + `LevelBuilder` creates room graphs; `Painter` fills tiles.
- **Message system** (`core/messages`): Properties files under `assets/messages/`. Key format: `ClassName.key`.
- **Services** (`services/`): Platform-agnostic interfaces for update checks and news, with debug/release implementations.

## Common Commands

```bash
# Run on desktop (debug mode)
./gradlew desktop:debug

# Build Android debug APK
./gradlew android:assembleDebug

# Build Android release APK
./gradlew android:assembleRelease

# Package desktop release JAR
./gradlew desktop:release

# Create native desktop image (jpackage)
./gradlew desktop:jpackageImage
```

## Conventions

- **Language**: Java 11 (no Kotlin, no Lombok)
- **Naming**: Standard Java conventions — PascalCase classes, camelCase methods/fields, UPPER_SNAKE_CASE constants
- **No unit tests**: QA is manual. Do not add test frameworks unless explicitly asked.
- **No external DI**: No Spring or similar. Dependency access is mostly static or passed through constructors.
- **Serialization**: Custom `Bundle`-based system, not JSON or XML. Class upgrades handled in `storeInBundle`/`restoreFromBundle` methods.
- **Assets**: All game assets (sprites, sounds, music, messages) live in `core/src/main/assets/`.
- **Localization**: Never hardcode user-facing strings. Use `Messages.get(ClassName.class, "key")`.

## Do Not

- Do not add Kotlin to this project — it's purely Java.
- Do not add annotation processors (APT, kapt) — the build doesn't support them.
- Do not hardcode user-facing strings — use the Messages system.
- Do not add new Gradle plugins without checking compatibility with Android Gradle Plugin 9.1.0.
- Do not submit pull requests — the upstream repo does not accept them.
- Do not modify `appVersionCode` or `appVersionName` in `build.gradle` unless doing a release.

## Getting Started

See `/docs` for platform-specific build guides:
- Android: `docs/getting-started-android.md`
- Desktop: `docs/getting-started-desktop.md`
- iOS: `docs/getting-started-ios.md`
- Custom fork guidance: `docs/recommended-changes.md`
