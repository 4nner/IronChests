# Iron Chests
[![FabricAPI](https://img.shields.io/static/v1?label=modloader&message=fabric&color=yellowgreen)](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
![Minecraft](https://img.shields.io/static/v1?label=minecraft&message=26.2&color=brightgreen)
![Mod Environment](https://img.shields.io/static/v1?label=environment&message=client%2Fserver&color=yellow)
[![License](https://img.shields.io/static/v1?label=licence&message=GPL-3.0&color=blue)](./LICENSE)

Iron Chests for Fabric is a simple mod that enhances your Minecraft Gameplay by adding new, upgradable chests, that will allow you to store more items in a single block. \
Each Chest can be upgraded, starting from the Wooden Chest (Vanilla Minecraft). By doing so you will just need to craft a Chest Upgrade, and SHIFT + RIGHT_CLICK the chest you want to upgrade. \
This will avoid the need to break the already placed chest. Do not worry, the chest will keep its inventory.

**Requires:** [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

## Dev environment (nix + devenv)

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

### Run the game client

`devenv.nix` puts the native libs Minecraft needs (GLFW/OpenGL via
`libGL`, X11/Wayland, OpenAL/audio, `udev`) on `LD_LIBRARY_PATH`, so
`runClient` works from any devenv shell:

```sh
./gradlew runClient   # or: run-client
./gradlew runServer   # or: run-server
```

## Special Thanks
TechnoVision: Initial port of Iron Chests to Fabric \
foul-fortune-feline: Fix Wooden Upgrades, added upgrades from-to any chest tier \
FakeDomi: Fix incompatibility with FastChests \
ARES: Korean Translation \
yichifauzi: Chinese Translation \
XenoshiYT: German Translation

## License
Iron Chests is licensed under GPL-3.0, see [LICENSE](./LICENSE).