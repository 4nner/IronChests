# Iron Chests

Bigger, upgradable chests for Minecraft, on Fabric and NeoForge.

- Modrinth: <https://modrinth.com/mod/ironchest>
- CurseForge: <https://www.curseforge.com/minecraft/mc-mods/ironchest>
- Mod id: `ironchest`; Minecraft 26.x, client and server
- License: GPL-3.0

## Requires

- [Iron Core Utilities](../iron-core/README.md) (both loaders)
- [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) (Fabric only)

## Chests

Each tier is a full block with a larger inventory than the last. Row counts
are the defaults; see Configuration to change them.

| Chest | Slots | Rows | Columns |
| --- | --- | --- | --- |
| Dirt 9000! | 126 | 9 | 14, dirt-only |
| Copper | 45 | 5 | 9 |
| Iron | 54 | 6 | 9 |
| Gold | 81 | 9 | 9 |
| Diamond | 108 | 9 | 12 |
| Emerald | 108 | 9 | 12 |
| Crystal | 108 | 9 | 12, see-through |
| Obsidian | 108 | 9 | 12, blast-resistant |
| Netherite | 126 | 9 | 14, blast-resistant |
| Christmas | 27 | 3 | 9, vanilla-sized, festive |

## Upgrades

Craft a Chest Upgrade and **Shift + Right-Click** the chest to transform it
into the next tier. The chest keeps its inventory. No need to break it and
move everything by hand. Upgrades work step by step from a vanilla chest all
the way up.

## Locks

Craft a Container Key (gold ingot over a stick) and **Shift + Right-Click**
a chest to lock it. Only you, players on the key, and operators can open,
break, or upgrade it. Locked chests show who holds the key in their title.

- **Right-Click** with the key (air or locked chest): edit that key's
  trusted list. Editing a key updates every linked chest at once.
- **Shift + Right-Click** a locked chest with its own key: unlock it.
- **Shift + Right-Click** a locked chest with a different key: link the
  chest to that key instead, staying locked.
- Rename a key in an anvil to label it; the label shows in chest titles.
- Operators can reset any chest with two clicks: shift-click with a blank
  key, then shift-click again with that same key.
- Locked chests accept no hopper or other automation input or output,
  regardless of who placed it.
- Set `enableLocks` to `false` in `config/ironcore.json` (restart required)
  to disable locking: locked chests behave as unlocked while off, with no
  data lost, and existing locks resume when re-enabled.

## Configuration

Chest height is configurable per tier in `config/ironchest.json`, inside
the game instance (client or server) folder. The file is created with
defaults on first launch; delete it to regenerate them.

Each key is a chest tier and each value its **row count, 1-12**. Columns
are fixed per tier and cannot change; defaults are the Rows column above.

```jsonc
// IronChests chest sizes.
// Rows per chest tier, 1-12. Columns are fixed per tier and cannot change.
// Unknown keys are ignored; missing or invalid values reset to defaults.
{
  // Copper chest rows (1-12)
  "copper": 5,
  // Iron chest rows (1-12)
  "iron": 6,
  // ... dirt, gold, diamond, emerald, crystal, obsidian, netherite, christmas
  // Open chests placed under solid blocks (true/false)
  "openUnderSolidBlocks": false
}
```

Rules:

- Out-of-range or non-integer values reset to that tier's default, with a
  warning in the log. Unknown keys are ignored.
- The file is read once at startup: restart the game or server after
  editing.
- Shrinking a tier is possible but messy: stacks that no longer fit refill
  empty slots first, and the rest pop out of the chest when the
  chunk loads. Same for upgrades that lead to a narrower tier.
- `openUnderSolidBlocks` (default `false`) lets chests open with a solid
  block above them. It takes real `true`/`false` only (`0`/`1` reset to
  default). Sitting cats still block chests. It applies on the server
  side, so single-player and servers just work after a restart.
- Multiplayer: the row counts must match between server and client.
