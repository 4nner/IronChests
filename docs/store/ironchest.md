Bigger, upgradable chests for Minecraft. Ten tiers from Christmas to Netherite, on Fabric and NeoForge.

![All tiers](https://raw.githubusercontent.com/4nner/ironchests/main/docs/images/chests-lineup.png)

## Requires

- [Iron Core Utilities](https://modrinth.com/project/iron-core-utilities) (Fabric or NeoForge)
- [Fabric API](https://modrinth.com/mod/fabric-api) (Fabric only)

## Chests

Each tier is a full block with a larger inventory than the last:

| Chest | Slots | Notes |
| --- | --- | --- |
| Christmas | 27 | Vanilla-sized, festive |
| Dirt 9000! | 126 | Dirt only |
| Copper | 45 | |
| Iron | 54 | |
| Gold | 81 | |
| Diamond | 108 | |
| Emerald | 108 | |
| Crystal | 108 | See-through, contents visible from outside |
| Obsidian | 108 | Blast-resistant |
| Netherite | 126 | Blast-resistant |

Row counts are configurable per tier in `config/ironchest.json`. Each chest breaks like the material it's named after. An `openUnderSolidBlocks` setting in the same file lets chests open with a solid block above them.

![Crystal chest](https://raw.githubusercontent.com/4nner/ironchests/main/docs/images/crystal-chest.png)

## Upgrades

Craft a Chest Upgrade and **Shift + Right-Click** a chest to upgrade it to the next tier, inventory included. No breaking, no moving stacks by hand. Works step by step from a vanilla chest all the way up.

![Upgrades](https://raw.githubusercontent.com/4nner/ironchests/main/docs/images/chest-upgrades.png)

## Locks

Craft a Container Key and **Shift + Right-Click** a chest to lock it. Only you, players on the key, and operators can open, break, or upgrade it. Keys hold a trusted list editable at any time; one edit updates every linked chest. Locked chests block hoppers and all other automation.

## Copper golems

Copper golems deposit carried items into any tier. They will only deposit dirt into the Dirt Chest 9000! Deposits can be switched off with `copperGolemInteraction` in `config/ironchest.json`.

![Copper golem](https://raw.githubusercontent.com/4nner/ironchests/main/docs/images/copper-golem.png)
