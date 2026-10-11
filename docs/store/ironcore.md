IronCore is a library/utility mod required by the Iron-collection mods like Iron Chest (more to come). The mod is used to provide shared, common logic between these mods, as well as some useful utilities.

## Supported mods

- [Iron Chests](https://modrinth.com/mod/ironchest): bigger, upgradable chests.

## Container keys and locks

Lock any supporting container with a Container Key (**Shift + Right-Click**). The owner and trusted players can access it; everyone else gets no access. Locked containers cannot be used in automations (like hoppers). Keys carry a trusted list: edit it once, every linked container syncs. Rename a key in an anvil to label it. Locking can be switched off entirely with `enableLocks` in `config/ironcore.json` without losing data.

![Locked container](https://raw.githubusercontent.com/4nner/ironchests/main/docs/images/container-key.png)

## Upgrades

Upgrade items upgrade a container into its next tier in place, inventory included. **Shift + Right-Click** the container with the matching upgrade.

![Upgrades](https://raw.githubusercontent.com/4nner/ironchests/main/docs/images/chest-upgrades.png)

## Hand trucks

Relocate filled containers with contents and lock intact. **Shift + Right-Click** to lift, **Right-Click** to set down.

- **Hand Truck** (25 durability): single containers.
- **Enhanced Hand Truck** (30 durability): containers and spawners.
- Setting a container down costs 1 durability; a spawner move costs 10. Carrying a loaded truck slows you down, and it must stay in your inventory. Spawner moves can be disabled in `config/ironcore.json`.

![Hand truck](https://raw.githubusercontent.com/4nner/ironchests/main/docs/images/hand-truck.png)


## Requires

Loader API only (Fabric API on Fabric, nothing extra on NeoForge).
