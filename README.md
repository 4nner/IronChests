# Iron Chests

![Fabric](https://img.shields.io/static/v1?label=modloader&message=fabric&color=yellowgreen)
![NeoForge](https://img.shields.io/static/v1?label=modloader&message=neoforge&color=orange)
![Minecraft](https://img.shields.io/static/v1?label=minecraft&message=26.x&color=brightgreen)
![Mod Environment](https://img.shields.io/static/v1?label=environment&message=client%2Fserver&color=yellow)
[![License](https://img.shields.io/static/v1?label=licence&message=GPL-3.0&color=blue)](./LICENSE)

Bigger, upgradable chests for Minecraft, on Fabric and NeoForge. This repo
holds two mods: [Iron Chests](./iron-chests/README.md) (the chests) and
[Iron Core Utilities](./iron-core/README.md) (the shared library).

## For players

| Mod | Loaders | Requires |
| --- | --- | --- |
| Iron Chests | Fabric, NeoForge | Iron Core Utilities (+ Fabric API on Fabric) |
| Iron Core Utilities | Fabric, NeoForge | Loader API only |

Install **both** jars. Iron Chests will not run without Iron Core Utilities.

- Iron Chests: [Modrinth](https://modrinth.com/mod/ironchest) / [CurseForge](https://www.curseforge.com/minecraft/mc-mods/ironchest) (mod id `ironchest`)
- Iron Core Utilities: [Modrinth](https://modrinth.com/mod/iron-core-utilities) / [CurseForge](https://www.curseforge.com/minecraft/mc-mods/iron-core-utilities) (mod id `ironcore`)

Supports Minecraft 26.x. See each mod's README for details.

Maintained by [GatherToCraft](https://www.gathertocraft.com), a team-finder portal that matches Minecraft players by playstyle, edition, schedule, and modpacks.

## For developers

Layout:

```text
iron-chests/common    loader-agnostic content (blocks, items, menus)
iron-chests/fabric    Iron Chests for Fabric
iron-chests/neoforge  Iron Chests for NeoForge
iron-chests/resources assets + data shared by both loaders
iron-core/common      shared library (tiers, menus, registries)
iron-core/fabric      Iron Core Utilities for Fabric
iron-core/neoforge    Iron Core Utilities for NeoForge
```

### Dev environment (nix + devenv)

Requires [nix](https://nixos.org/download/) with flakes enabled and [devenv](https://devenv.sh/getting-started/).

```sh
# optional, for auto-activation on cd:
# once per machine: devenv direnvrc >> ~/.config/direnv/direnvrc
direnv allow
# or without direnv:
devenv shell
```

Then build/test with:

```sh
./gradlew build
./gradlew check
```

`devenv test` runs the build as well.

### Run the game

`devenv.nix` puts the native libs Minecraft needs (GLFW/OpenGL via
`libGL`, X11/Wayland, OpenAL/audio, `udev`) on `LD_LIBRARY_PATH`, so the
game launches from any devenv shell:

```sh
./gradlew :iron-chests:fabric:runClient      # or: run-client
./gradlew :iron-chests:neoforge:runClient    # or: run-client-neoforge
./gradlew :iron-chests:fabric:runServer      # or: run-server
./gradlew :iron-chests:neoforge:runServer    # or: run-server-neoforge
```

### Conventions for Contributions

- Java and nix sources are formatted (`google-java-format`, `nixfmt`)
- Commit messages follow [Conventional Commits](https://www.conventionalcommits.org/)
- Small, auditable commits. Please do not submit large PRs with massive commits, as they are hard to audit and review.

All enforced by git hooks, installed automatically with the shell
(see `git-hooks` in `devenv.nix`).

## Special Thanks
TechnoVision: Initial port of Iron Chests to Fabric \
foul-fortune-feline: Fix Wooden Upgrades, added upgrades from-to any chest tier \
FakeDomi: Fix incompatibility with FastChests \
ARES: Korean Translation \
yichifauzi: Chinese Translation \
XenoshiYT: German Translation

## License
Iron Chests is licensed under GPL-3.0, see [LICENSE](./LICENSE).
