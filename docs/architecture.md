# Architecture — Shattered Pixel Dungeon

## System Overview

```mermaid
flowchart TD
    A[Platform Launcher\nAndroid / Desktop / iOS] --> B[ShatteredPixelDungeon\nApplicationListener]
    B --> C[Scene Router\nPixelScene]
    C --> D[TitleScene]
    C --> E[GameScene]
    C --> F[InterlevelScene]
    C --> G[Other Scenes]
    E --> H[Dungeon State\nstatic]
    H --> I[Hero]
    H --> J[Level]
    H --> K[Actor Loop]
    K --> L[Mobs]
    K --> M[Buffs]
    K --> N[Blobs]

    style A stroke:#03A9F4,stroke-width:2px
    style B stroke:#9C27B0,stroke-width:2px
    style C stroke:#FF9800,stroke-width:2px
    style E stroke:#4CAF50,stroke-width:2px
    style H stroke:#F44336,stroke-width:2px
```

## Module Dependency Graph

```mermaid
flowchart LR
    SC[SPD-classes\ncom.watabou.*] --> CORE[core\ncom.shatteredpixel.*]
    SVC[services] --> CORE
    CORE --> AND[android]
    CORE --> DESK[desktop]
    CORE --> IOS[ios]
    SVC --> AND
    SVC --> DESK

    style SC stroke:#03A9F4,stroke-width:2px
    style CORE stroke:#4CAF50,stroke-width:2px
    style SVC stroke:#FF9800,stroke-width:2px
    style AND stroke:#9C27B0,stroke-width:2px
    style DESK stroke:#9C27B0,stroke-width:2px
    style IOS stroke:#9C27B0,stroke-width:2px
```

## Layer Breakdown

### Platform Layer — `android/`, `desktop/`, `ios/`

Contains only OS-specific entry points. Zero game logic.

| Module | Launcher Class | Backend |
|--------|---------------|---------|
| android | `AndroidLauncher` | libGDX Android backend |
| desktop | `DesktopLauncher` | LWJGL3 |
| ios | `IOSLauncher` | RoboVM |

### Engine Layer — `SPD-classes/`

Custom libGDX wrapper, the Noosa scene graph. No game-domain knowledge.

```mermaid
classDiagram
    class Gizmo {
        +update()
        +draw()
        +destroy()
    }
    class Visual {
        +x, y, width, height
        +alpha, angle, scale
    }
    class Image {
        +TextureFilm frame
    }
    class MovieClip {
        +Animation[] anims
        +play(anim)
    }
    class Group {
        +ArrayList members
        +add(Gizmo)
    }
    class Scene {
        +create()
        +update()
        +destroy()
    }

    Gizmo <|-- Visual
    Gizmo <|-- Group
    Visual <|-- Image
    Image <|-- MovieClip
    Group <|-- Scene
```

### Game Entry — `ShatteredPixelDungeon`, `Dungeon`

- `ShatteredPixelDungeon` — boot, settings, scene switching
- `Dungeon` — static singleton for the active run (hero, level, depth, seed, quests)
- `Statistics` — cumulative run stats
- `Badges` — achievement tracking
- `Rankings` — high score persistence

### Scene Layer — `core/.../scenes/`

Each scene extends `PixelScene` and is self-contained.

```mermaid
flowchart LR
    Title[TitleScene] -->|New Game| HS[HeroSelectScene]
    Title -->|Load| G[GameScene]
    HS -->|Start| IL[InterlevelScene]
    IL -->|Load complete| G
    G -->|Level exit| IL
    G -->|Win/Die| SR[SurfaceScene / RankingsScene]

    style G stroke:#4CAF50,stroke-width:2px
    style IL stroke:#FF9800,stroke-width:2px
```

### Actor System — `core/.../actors/`

Turn-based. All entities extend `Actor`. The loop picks the actor with the lowest `time` value and calls `act()`. See [game-loop.md](game-loop.md) for detail.

### Item System — `core/.../items/`

`Item` base class with deep inheritance. See [item-system.md](item-system.md) for the full hierarchy.

### Level System — `core/.../levels/`

Procedural. See [level-generation.md](level-generation.md) for the pipeline.

## Cross-Cutting Concerns

### Serialization

All persistable objects implement `Bundlable` (`storeInBundle` / `restoreFromBundle`). The `Bundle` class handles encoding to a binary/text format. Save files are stored per-platform in the app's data directory.

### Localization

`Messages.get(Class, "key")` loads from `.properties` files under `core/src/main/assets/messages/`. Keys are fully qualified: `com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing.name=Potion of Healing`.

### Event Bus

`Signal<T>` (in SPD-classes) provides a lightweight observer pattern used for decoupled communication (e.g., level events, hero death).

### Pathfinding

`PathFinder` (SPD-classes) uses Dijkstra on the flat tile grid (`Level.map` — a 1D int array of width×height).

## Key Static State

| Class | Role |
|-------|------|
| `Dungeon` | Active run: hero, level, depth, seed, quests, actors |
| `Statistics` | Turn count, gold collected, food eaten, etc. |
| `Badges` | Unlocked achievements |
| `SPDSettings` | Persisted player preferences |
| `Assets` | String constants for all asset file paths |
