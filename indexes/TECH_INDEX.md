# Technology Index — Shattered Pixel Dungeon

## Build System

| Tool | Version | Config File |
|------|---------|-------------|
| Gradle | wrapper | `gradlew` / `gradle.properties` |
| Android Gradle Plugin | 9.1.0 | `build.gradle` (root) |
| Java compatibility | 11 | `appJavaCompatibility` in root `build.gradle` |
| org.beryx.runtime | 2.0.1 | `desktop/build.gradle` (jpackage) |

## Core Dependencies

| Library | Version | Used In |
|---------|---------|---------|
| libGDX core | 1.14.0 | `core`, `android`, `desktop`, `ios` |
| gdx-backend-android | 1.14.0 | `android` |
| gdx-backend-lwjgl3 | 1.14.0 | `desktop` |
| gdx-freetype | 1.14.0 | `core`, `android`, `desktop` |
| gdx-controllers | 2.2.4 | `android`, `desktop` |
| RoboVM | 2.3.24 | `ios` |
| LWJGL tinyFD | 3.3.3 | `desktop` (crash dialogs) |
| Temurin JDK 17 | 17.0.17+10 | bundled in desktop packages |

## Language Features Used

- Java 11 language level
- Standard Java collections (ArrayList, HashMap, etc.)
- Anonymous classes and lambdas (where compatible with Java 11)
- No Lombok, no annotation processors
- No Kotlin

## Asset Pipeline

| Asset Type | Location | Format |
|------------|----------|--------|
| Sprites | `core/src/main/assets/sprites/` | PNG atlases |
| Environment tiles | `core/src/main/assets/environment/` | PNG |
| Custom tiles | `core/src/main/assets/environment/custom_tiles/` | PNG |
| Effects | `core/src/main/assets/effects/` | PNG |
| Fonts | `core/src/main/assets/fonts/` | TTF/FNT |
| Music | `core/src/main/assets/music/` | OGG |
| Sounds | `core/src/main/assets/sounds/` | OGG/WAV |
| Interface assets | `core/src/main/assets/interfaces/` | PNG |
| Splash screens | `core/src/main/assets/splashes/` | PNG |
| Localization | `core/src/main/assets/messages/` | .properties |
| libGDX assets | `core/src/main/assets/gdx/` | various |

## Localization

- System: Custom `Messages` class loading `.properties` files
- 24 supported locales: en, zh-rCN, ko, ru, es, pt, fr, de, zh-rTW, ja, pl, it, tr, vi, uk, id, cs, nl, sv, hu, fi, el, be, eo
- Translation project: Transifex

## Android Platform

| Config | Value |
|--------|-------|
| minSdkVersion | 21 (Android 5.0) |
| targetSdkVersion | 36 (Android 15) |
| compileSdk | 36 |
| App ID | `com.shatteredpixel.shatteredpixeldungeon` |
| Debug app ID | `com.shatteredpixel.shatteredpixeldungeon.indev` |
| R8/ProGuard | Enabled for release |
| AndroidX | Enabled |

## Desktop Platform

| Config | Value |
|--------|-------|
| Backend | LWJGL3 |
| JDK (bundled) | Temurin 17 |
| Entry point | `com.shatteredpixel.shatteredpixeldungeon.desktop.DesktopLauncher` |
| Package formats | jpackage image (Windows .exe, macOS .app, Linux binary) |

_Generated 2026-05-18_
