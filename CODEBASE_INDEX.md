# Codebase Index — Shattered Pixel Dungeon

**Version**: 3.3.8 | **Language**: Java 11 | **Engine**: libGDX 1.14.0 | **Build**: Gradle

> AI Agent Navigation Hub. Start here to orient in the codebase.

---

## Quick Navigation

| Goal | Go To |
|------|-------|
| Add a new item | `core/.../items/` — extend nearest existing type |
| Add a new mob | `core/.../actors/mobs/` — extend `Mob` |
| Add a new buff | `core/.../actors/buffs/` — extend `Buff` or `FlavourBuff` |
| Add a new level room | `core/.../levels/rooms/` — extend `Room` |
| Add a UI element | `core/.../ui/` or `core/.../windows/` |
| Add a new scene/screen | `core/.../scenes/` — extend `PixelScene` |
| Add a new effect | `core/.../effects/` |
| Change localized text | `core/src/main/assets/messages/` |
| Change tile visuals | `core/.../tiles/` and `core/src/main/assets/environment/` |
| Change sprites | `core/.../sprites/` and `core/src/main/assets/sprites/` |
| Platform entry points | `android/`, `desktop/`, `ios/` |
| Update/news services | `services/` |

---

## Module Map

```
SPD-classes   →  core  →  android
                      →  desktop
                      →  ios
              services ↗
```

### SPD-classes (`com.watabou.*`)
Shared libGDX engine layer. Do **not** add game-specific logic here.

| Package | Contents |
|---------|----------|
| `noosa` | Scene graph — Visual, Gizmo, Image, Text, Camera, PixelScene |
| `noosa/audio` | SoundEmitter, Music, Sample |
| `noosa/particles` | Particle system |
| `glwrap` | OpenGL buffer/shader/texture wrappers |
| `gltextures` | Texture atlas management |
| `input` | Key, pointer, controller input routing |
| `utils` | PointF, Random, Callback, PathFinder, Signal, etc. |

### Core (`com.shatteredpixel.shatteredpixeldungeon.*`)
All game logic lives here.

| Package | Contents |
|---------|----------|
| `actors` | Turn-based entity system |
| `actors/hero` | `Hero`, `HeroClass`, `HeroSubClass`, `Talent`, `Belongings` |
| `actors/hero/abilities` | Hero talent abilities |
| `actors/hero/spells` | Hero spell abilities |
| `actors/mobs` | All enemy types, extends `Mob` |
| `actors/mobs/npcs` | Non-hostile NPCs |
| `actors/buffs` | Status effects and conditions |
| `actors/blobs` | Area-of-effect terrain blobs (fire, gas, etc.) |
| `items` | `Item` base + all item types |
| `items/weapon/melee` | Melee weapons |
| `items/weapon/missiles` | Thrown weapons |
| `items/weapon/enchantments` | Weapon enchantments |
| `items/weapon/curses` | Weapon curses |
| `items/armor` | Armor types |
| `items/armor/glyphs` | Armor glyphs (positive) |
| `items/armor/curses` | Armor curses (negative) |
| `items/potions` | Standard potions |
| `items/potions/exotic` | Exotic potion variants |
| `items/potions/elixirs` | Elixir variants |
| `items/potions/brews` | Brew variants |
| `items/scrolls` | Standard scrolls |
| `items/scrolls/exotic` | Exotic scroll variants |
| `items/wands` | Wands |
| `items/rings` | Rings |
| `items/artifacts` | Artifacts (unique slot items) |
| `items/trinkets` | Trinket items |
| `items/bags` | Container bags |
| `items/food` | Food items |
| `items/bombs` | Bomb items |
| `items/spells` | Spell-use items |
| `items/stones` | Runestone items |
| `items/keys` | Key items |
| `items/journal` | Journal / lore items |
| `items/quest` | Quest-specific items |
| `items/remains` | Hero remains items |
| `levels` | `Level` base class + level types |
| `levels/builders` | Layout graph builders |
| `levels/painters` | Tile-fill painters |
| `levels/rooms` | Room type definitions |
| `levels/rooms/standard` | Standard room types |
| `levels/rooms/special` | Special rooms |
| `levels/rooms/secret` | Hidden rooms |
| `levels/rooms/quest` | Quest rooms |
| `levels/rooms/connection` | Corridor/connection rooms |
| `levels/rooms/sewerboss` | Sewer boss area rooms |
| `levels/traps` | Trap types |
| `levels/features` | Terrain features (doors, stairs, etc.) |
| `scenes` | Game screens extending `PixelScene` |
| `ui` | HUD and reusable UI components |
| `windows` | Dialog/popup windows |
| `effects` | Visual effects (particles, spell FX) |
| `sprites` | Visual sprite classes for actors and items |
| `messages` | Localization — `Messages.get(Class, key)` |
| `journal` | Catalog and lore tracking |
| `mechanics` | Reusable game mechanics (Ballistica, Combo, etc.) |
| `plants` | Plant entity types |
| `tiles` | Tile terrain constants and custom tile rendering |
| `utils` | Game-wide utilities |
| `services` | Service interfaces (update check, news) |

### Platform Modules
| Module | Entry Point |
|--------|-------------|
| `android` | `AndroidLauncher` |
| `desktop` | `DesktopLauncher` |
| `ios` | `IOSLauncher` |

---

## File Statistics

| Location | Approximate Count |
|----------|-------------------|
| Java source files (core) | ~700+ |
| Java source files (SPD-classes) | ~80+ |
| Localization message files | ~60+ (24 locales × multiple categories) |
| Sprite assets | ~50+ PNG atlases |
| Sound assets | ~80+ OGG files |

---

## Architecture Notes

1. **No dependency injection** — dependencies are accessed statically (e.g., `Dungeon.hero`) or passed via constructors.
2. **Custom serialization** — `Bundle` system, not JSON. Every persistable class implements `storeInBundle`/`restoreFromBundle`.
3. **Event bus** — `Signal<T>` in SPD-classes for decoupled event propagation.
4. **No unit tests** — manual QA only. Don't add test frameworks.
5. **Localization** — never hardcode user-visible strings; use `Messages.get(SomeClass.class, "key")`.

---

## Related Indexes

- [Technology Index](indexes/TECH_INDEX.md)
- [Architecture Index](indexes/ARCHITECTURE_INDEX.md)
- [Domain Index](indexes/DOMAIN_INDEX.md)
- [Quick Reference](indexes/QUICK_REF.md)

## Documentation

- [Architecture & Module Overview](docs/architecture.md)
- [Game Loop & Actor System](docs/game-loop.md)
- [Item System & Hierarchy](docs/item-system.md)
- [Level Generation Pipeline](docs/level-generation.md)
- [Forking & Contribution Guide](docs/contributing-fork.md)
- [Full Docs Index](docs/README.docs.md)

---

_Generated by orche WM-repository-initialization — 2026-05-18 | Updated 2026-05-18_
