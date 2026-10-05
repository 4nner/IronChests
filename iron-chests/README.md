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
  // ... gold, diamond, emerald, crystal, obsidian, netherite, christmas
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
- Multiplayer: the config must match between server and client.
