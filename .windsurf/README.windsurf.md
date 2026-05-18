# Windsurf Configuration — Shattered Pixel Dungeon

## Project Context

This is a Java 11 / libGDX roguelike game. See `project-context.yaml` for the full stack definition.

## AI Assistant Notes

- Language is **Java 11** — no Kotlin, no TypeScript, no C#
- Build system is **Gradle** (Groovy DSL) — do not suggest Maven
- No unit test framework — do not add JUnit or Mockito
- No dependency injection — do not suggest Spring or Guice
- All user-visible strings must go through the `Messages` localization system
- Serialization uses the custom `Bundle` system, not Gson/Jackson

## Directories

- `custom workflows/` — project-specific Windsurf workflows (if any)
- `custom rules/` — project-specific Windsurf rules (if any)

## Key Files for Context

| File | Purpose |
|------|---------|
| `AGENTS.md` | Full AI context document |
| `CODEBASE_INDEX.md` | Navigation hub |
| `project-context.yaml` | Detected stack config |
| `indexes/` | Detailed architecture, tech, domain, and quick-ref indexes |
