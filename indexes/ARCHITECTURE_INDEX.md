# Architecture Index — Shattered Pixel Dungeon

## High-Level Architecture

```
Platform Layer (android / desktop / ios)
        │
        ▼
  ShatteredPixelDungeon   ← game entry point (ApplicationListener)
        │
        ▼
   PixelScene  (noosa)    ← active screen
        │
   ┌────┴────────────┐
   │                 │
GameScene          Other Scenes
   │             (Title, Interlevel, etc.)
   │
   ├── Dungeon (static state)
   │     ├── Hero
   │     ├── Level
   │     └── Actors
   │
   └── Actor Loop (turn-based)
         ├── Hero
         ├── Mobs
         ├── Buffs
         └── Blobs
```

## Layer Descriptions

### Platform Layer
- **Concern**: OS integration, window management, input routing, lifecycle events
- **Files**: `android/AndroidLauncher`, `desktop/DesktopLauncher`, `ios/IOSLauncher`
- **Rule**: Zero game logic here. Delegates entirely to `ShatteredPixelDungeon`.

### Engine Layer (`SPD-classes`)
- **Concern**: Rendering, input, audio, utilities
- **Key classes**: `Game` (ApplicationListener base), `PixelScene`, `Visual`, `Image`, `Camera`
- **Rule**: No game-domain knowledge (no Hero, no Item, no Level).

### Game Entry (`ShatteredPixelDungeon`)
- **Concern**: Boot, settings, game state, scene routing
- **Key classes**: `ShatteredPixelDungeon`, `Dungeon`, `Statistics`, `Badges`
- **Rule**: Sets up global state, does NOT contain gameplay algorithms.

### Scene Layer (`scenes/`)
- **Concern**: Screen management and layout
- **Key classes**: `GameScene`, `TitleScene`, `InterlevelScene`, `RankingsScene`, `HeroSelectScene`
- **Pattern**: Each scene extends `PixelScene`. Scenes are self-contained and manage their own lifecycle.

### Actor System (`actors/`)
- **Concern**: Turn-based gameplay, entities, status effects, area effects
- **Base classes**: `Actor` → `Char` → `Hero` / `Mob`
- **Buff system**: `Buff` objects are attached to `Char` and processed each turn
- **Blob system**: `Blob` represents area effects (fire, toxic gas) tracked per tile
- **Rule**: Combat math, buff stacking, AI all live here.

### Item System (`items/`)
- **Concern**: All collectibles, their use actions, enchantments
- **Base class**: `Item`
- **Key methods**: `execute(Hero hero, String action)`, `storeInBundle`, `restoreFromBundle`
- **Sub-hierarchies**: weapons, armor, potions, scrolls, wands, rings, artifacts, trinkets
- **Pattern**: Each item category has its own sub-hierarchy; enchantments/glyphs/curses are composed.

### Level System (`levels/`)
- **Concern**: Procedural level generation
- **Pipeline**: `Level` → `LevelBuilder` (room graph) → `Painter` (tile fill) → trap/feature placement
- **Room types**: standard, special, secret, quest, sewerboss, connection
- **Tile system**: Constants in `Terrain`; custom multi-tile objects in `tiles/`

### UI Layer (`ui/`, `windows/`)
- **Concern**: HUD, dialogs, menus
- **Key classes**: `GameUI`, `Toolbar`, `StatusPane`, various `Window*` dialogs
- **Rule**: UI classes should not contain gameplay logic; delegate to actors/items.

### Services Layer (`services/`)
- **Concern**: Platform-agnostic update checks and news
- **Pattern**: Interface in `services/`, implementations in `services/updates/` and `services/news/`
- **Variants**: `debugUpdates`/`debugNews` (dev builds), `githubUpdates`/`shatteredNews` (release builds)

## Cross-Cutting Concerns

### Serialization
- Custom `Bundle` system (NOT JSON/XML)
- Every persistable class: `storeInBundle(Bundle)` + `restoreFromBundle(Bundle)`
- Upgrade path for save compatibility handled in `restoreFromBundle`

### Localization
- `Messages.get(Class, "key")` — keys are scoped to their class
- Never use raw strings for user-visible text

### Random Number Generation
- `Random` utility in SPD-classes
- `Dungeon.seedCurDepth()` scopes randomness per level for reproducibility

### Pathfinding
- `PathFinder` in SPD-classes (Dijkstra-based on tile grid)

_Generated 2026-05-18_
