# Multi-Version-Loader-Template

Template for multi-version, multi-loader Minecraft mods. One repo builds:

| MC     | Forge | Fabric | NeoForge | Java |
|--------|-------|--------|----------|------|
| 1.20.1 | yes   | yes    | no       | 17   |
| 1.21.1 | no    | yes    | yes      | 21   |
| 26.1.x | no    | yes    | yes      | 25   |

## Layout

```
common-<mcver>/       shared, loader-agnostic code (source-only, compiled by each loader module)
fabric-<mcver>/       Fabric entrypoint + platform impl
forge-1.20.1/         Forge entrypoint (standalone Gradle 8.8 build, see below)
neoforge-<mcver>/     NeoForge entrypoint (1.21.1+)
```

Common code never touches loader APIs. Anything loader-specific goes behind the
`PlatformHelper` ServiceLoader interface in `common-*/.../platform/` — each loader module
provides an implementation and registers it in `META-INF/services/`. Add more capabilities
(networking, config, events) the same way.

The loader modules don't depend on the common jar — they add the common module's source
directories to their own source set and compile everything together, so there is no runtime
common artifact to ship.

### forge-1.20.1 is a separate Gradle build

ForgeGradle 6 can't run on Gradle 9 and `includeBuild` can't bridge Gradle versions, so
`forge-1.20.1/` has its own Gradle 8.8 wrapper and is *not* in `settings.gradle`.

- CLI: `./gradlew forge1201Build` / `forge1201RunClient` / `forge1201RunServer` from the
  root (delegates to `forge-1.20.1/gradlew` via Exec).
- IntelliJ: Gradle tool window → `+` → Link Gradle Project → pick `forge-1.20.1/build.gradle`.
  It appears as a second root with its own task tree and run configs.
- Its `gradle.properties` duplicates the mod properties — keep it in sync with the root one.

## Commands

```
./gradlew build                       # everything except forge-1.20.1
./gradlew forge1201Build              # forge-1.20.1
./gradlew :fabric-1.20.1:runClient
./gradlew :fabric-1.21.1:runClient
./gradlew :fabric-26.1:runClient
./gradlew :neoforge-1.21.1:runClient
./gradlew :neoforge-26.1:runClient
./gradlew forge1201RunClient
```

Jars land in `<module>/build/libs/`.

IntelliJ users: `.run/` ships shared run configurations for every loader's `runClient`,
so they show up in the run dropdown as soon as the project is opened.

## CI / publishing

Forgejo Actions workflows in `.forgejo/workflows/`:

- `build.yml` — builds all six loader jars on every push/PR and stashes them as artifacts.
- `publish.yml` — on a `v*` tag: builds the matrix, publishes to Modrinth/CurseForge
  (mc-publish), publishes all modules to the Forgejo Maven, creates a Forgejo release with
  the six jars attached, and announces it via the Discord bot. A `workflow_dispatch` run
  stops after the build step (dry run).

Both read `mod_id`/`mod_name`/`mod_group_id` from `gradle.properties` at runtime, so they
survive the rename checklist untouched. Required repo secrets for publishing:
`MODRINTH_PROJECT_ID`, `MODRINTH_TOKEN`, `CURSEFORGE_PROJECT_ID`, `CURSEFORGE_TOKEN`,
`MAVEN_PUBLISH_TOKEN`, `DISCORD_RELEASE_WEBHOOK_SECRET`. Delete the steps/jobs a project
doesn't use (e.g. `notify-discord`, or the mc-publish step before the first public release).

Release notes come from `CHANGELOG.md`: keep one `## vX.Y.Z` section per release — the
pipeline extracts the section matching the pushed tag.

## Starting a new mod from this template

1. `gradle.properties` (root **and** `forge-1.20.1/gradle.properties`): set `mod_id`,
   `mod_name`, `mod_version`, `mod_group_id`, `mod_authors`, `mod_description`, `mod_license`.
2. Rename the package `net.tysontheember.examplemod` → your package root in every module
   (`common-*`, `fabric-*`, `forge-1.20.1`, `neoforge-*`).
3. Rename the classes: `ExampleMod`, `ExampleModFabric`, `ExampleModFabricClient`,
   `ExampleModForge`, `ExampleModNeoForge`, and `MOD_ID`/`MOD_NAME` in `Constants`.
4. Rename the mixin configs to match the new mod id — they are referenced by file name:
   - `common-*/src/main/resources/examplemod.mixins.json`
   - `fabric-*/src/main/resources/examplemod-fabric.mixins.json`
   - `forge-1.20.1/src/main/resources/examplemod-forge.mixins.json`
   - `neoforge-*/src/main/resources/examplemod.mixins.json`
   Update the `package` and `refmap` fields inside them too.
5. Rename the ServiceLoader registration files (file name = interface FQN) and update the
   implementation FQN inside:
   `*/src/main/resources/META-INF/services/net.tysontheember.examplemod.platform.PlatformHelper`
6. Update the entrypoint class names in each `fabric-*/src/main/resources/fabric.mod.json`.
7. `settings.gradle`: set `rootProject.name`.
8. Update `pack.mcmeta` descriptions, reset `CHANGELOG.md`, and drop in your `LICENSE`.
9. Delete the example `TitleScreenMixin` (all three `common-*` modules) once you have real
   mixins, or keep it to verify the pipeline.
10. `./gradlew build && ./gradlew forge1201Build` to confirm everything still compiles.

## Version notes

- Versions are pinned in `gradle.properties`; 1.20.1 Forge versions live in
  `forge-1.20.1/gradle.properties`.
- Fabric 1.20.1/1.21.1 use the `fabric-loom-remap` plugin with Mojang mappings — the stock
  plugin refuses Mojang mappings in a non-obfuscated multi-loader workspace. MC 26.1 is
  unobfuscated, so `fabric-26.1` uses stock `fabric-loom` with no mappings at all.
- API deltas between versions stay in the per-version modules. Example:
  `FMLEnvironment.dist`/`production` (NeoForge 1.21.1) became
  `FMLEnvironment.getDist()`/`isProduction()` (NeoForge 26.1) — see `NeoForgePlatformHelper`.
- Mixin `compatibilityLevel` per version: JAVA_17 (1.20.1), JAVA_21 (1.21.1), JAVA_25 (26.1).
  26.1 configs have no `refmap` (unobfuscated runtime).
- Publishing: `publish` pushes to `mavenRepoPath` (defaults to `build/maven-repo`) and to
  the Forgejo maven if `forgejoActor`/`forgejoToken` (or the env vars) are set.

## Credits

Structure follows [jaredlll08/MultiLoader-Template](https://github.com/jaredlll08/MultiLoader-Template),
adapted for multi-version with the patterns from EmbersTextAPI.
