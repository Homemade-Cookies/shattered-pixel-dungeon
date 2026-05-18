# Domain Index — Shattered Pixel Dungeon

Game domain model and entity relationships.

## Core Domain Concepts

### Hero
- **File**: `core/.../actors/hero/Hero.java`
- **Class**: `Hero.java`, `HeroClass.java`, `HeroSubClass.java`
- **Inventory**: `Belongings.java` — manages equipped items and bags
- **Talents**: `Talent.java` — passive and active upgrades
- **Abilities**: `actors/hero/abilities/` — subclass-specific actives
- **Spells**: `actors/hero/spells/` — Arcanist spell abilities

### Enemies (Mobs)
- **Base**: `actors/mobs/Mob.java` extends `Char`
- **NPCs**: `actors/mobs/npcs/` — friendly characters (shopkeeper, blacksmith, etc.)
- **All mobs**: `actors/mobs/` — ~50+ enemy types
- **AI**: Built into each `Mob` subclass via `act()` and `AiState`

### Buffs (Status Effects)
- **Base**: `actors/buffs/Buff.java`
- **Positive buffs**: Haste, Invisibility, Levitation, etc.
- **Negative buffs**: Burning, Poison, Weakness, Blindness, etc.
- **Attached to**: Any `Char` via `Buff.affect(char, BuffClass.class)`

### Blobs (Area Effects)
- **Base**: `actors/blobs/Blob.java`
- **Examples**: Fire, ToxicGas, ConfusionGas, Freezing, SmokeScreen
- **Storage**: Per-tile maps within the current `Level`

### Items
| Category | Location | Examples |
|----------|----------|---------|
| Melee Weapons | `items/weapon/melee/` | Shortsword, Longsword, StoneHammer |
| Missile Weapons | `items/weapon/missiles/` | Shuriken, Javelin, ForceCube |
| Armor | `items/armor/` | LeatherArmor, PlateArmor |
| Potions | `items/potions/` | PotionOfHealing, PotionOfStrength |
| Exotic Potions | `items/potions/exotic/` | PotionOfDragonsBreath |
| Elixirs | `items/potions/elixirs/` | ElixirOfAquaticRejuvenation |
| Brews | `items/potions/brews/` | BlizzardBrew |
| Scrolls | `items/scrolls/` | ScrollOfUpgrade, ScrollOfIdentify |
| Exotic Scrolls | `items/scrolls/exotic/` | ScrollOfPrismaticImage |
| Wands | `items/wands/` | WandOfFireblast, WandOfCorrosion |
| Rings | `items/rings/` | RingOfAccuracy, RingOfWealth |
| Artifacts | `items/artifacts/` | CapeOfThorns, EtherealChains |
| Trinkets | `items/trinkets/` | Various trinket effects |
| Bags | `items/bags/` | MagicalHolster, VelvetPouch |
| Food | `items/food/` | Ration, MysteryMeat |
| Bombs | `items/bombs/` | Bomb, Flashbang |
| Spells | `items/spells/` | Alchemize, AquaBlast |
| Runestones | `items/stones/` | StoneOfAggression |
| Keys | `items/keys/` | IronKey, SkeletonKey |

### Enchantments & Glyphs
- Weapon enchantments: `items/weapon/enchantments/` — positive modifiers
- Weapon curses: `items/weapon/curses/` — negative modifiers
- Armor glyphs: `items/armor/glyphs/` — positive modifiers
- Armor curses: `items/armor/curses/` — negative modifiers

### Levels & Rooms
| Level Type | Theme |
|-----------|-------|
| SewerLevel | Floors 1-5 (sewers) |
| PrisonLevel | Floors 6-10 |
| CavesLevel | Floors 11-15 |
| CityLevel | Floors 16-20 |
| HallsLevel | Floors 21-25 (final) |
| BossLevel | Boss encounter floors |
| ShopLevel | Shop floor |

- **Room types**: standard, special, secret, quest, connection, sewerboss
- **Builders**: `RegularBuilder`, `LoopBuilder`, `LineBuilder`
- **Traps**: `levels/traps/` — ~25 trap types

### Plants
- **Location**: `plants/`
- **Examples**: Dreamfoil, Stormvine, Sungrass, Earthroot

### Journal & Catalog
- **Location**: `journal/`
- **Tracks**: discovered items, defeated enemies, lore entries

### Scenes (Screens)
| Scene | Purpose |
|-------|---------|
| TitleScene | Main menu |
| HeroSelectScene | Hero class selection |
| GameScene | Active gameplay |
| InterlevelScene | Level transition/loading |
| SurfaceScene | Dungeon exit cutscene |
| RankingsScene | High scores / rankings |
| StartScene | Run-start config |

### Services
| Service | Purpose |
|---------|---------|
| UpdateChecker | Checks for game updates |
| News | Fetches dev blog news |

## Key Static State

| Class | Role |
|-------|------|
| `Dungeon` | Current run state (hero, level, depth, seed) |
| `Statistics` | Run statistics |
| `Badges` | Achievement tracking |
| `Challenges` | Active challenge modifiers |
| `Bones` | Hero remains between runs |

_Generated 2026-05-18_
