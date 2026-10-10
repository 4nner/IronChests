# Iron Core Utilities

Shared foundation for the Iron suite of mods, on Fabric and NeoForge.
Iron Chests stands on it; future Iron mods will too.

- Modrinth: <https://modrinth.com/mod/iron-core-utilities>
- CurseForge: <https://www.curseforge.com/minecraft/mc-mods/iron-core-utilities>
- Mod id: `ironcore`; Minecraft 26.x, client and server
- License: GPL-3.0

## Requires

Loader API only (Fabric API on Fabric, nothing extra on NeoForge).

## What it provides

- **Tier specs**: data-driven container sizes shared by every mod.
- **Sized container menus**: inventory screens that adapt rows, columns,
  and textures to the tier instead of hardcoding vanilla layouts.
- **Loader-agnostic registries**: one registration API with a Fabric and a
  NeoForge backend, discovered automatically per loader. Content mods
  register under their own namespace.
- **Inventory helpers**: capacity clamping and sanitizing when containers
  change size.
- **Container Key locks**: key-linked container locking any mod can adopt.
  A `Container Key` mints a registry entry on first use; locked containers
  store the key id and resolve the live entry on every check, so edits apply
  to all linked containers instantly. Implement `KeyLinkable` on a block
  entity and delegate open/break guards to `LockGuards`. OP level 2+
  bypasses everything.

## Hand Trucks

Lift and relocate filled containers with contents (and lock) intact.

- **Hand Truck** (25 durability): single containers only.
- **Enhanced Hand Truck** (30 durability): containers and spawners.
- **Controls**: **Shift + Right-Click** a container to lift it;
  **Right-Click** the ground to set it back down. Double chests, locked
  (for you), in-use, or nested (holding a loaded truck) containers stay
  put.
- **Cost**: putting a container down costs 1 durability; a spawner move
  costs 10.
- **Spawner gate**: `handTruckCanMoveSpawners` in `config/ironcore.json`
  (default `true`,) can be set to `false` to prevent spawner to be moved using
  the Enhanced Hand Truck.
- **While loaded**: carrying a loaded truck (inventory, offhand, or cursor)
  inflicts Slowness II.
- **Inventory restriction**: a loaded truck must stay in your inventory.
