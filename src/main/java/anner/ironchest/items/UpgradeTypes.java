package anner.ironchest.items;

import anner.ironchest.blocks.ChestTypes;

public enum UpgradeTypes {
    WOOD_TO_COPPER(ChestTypes.WOOD, ChestTypes.COPPER, "item.ironchest.wood_copper_upgrade.tooltip"),
    WOOD_TO_CHRISTMAS(ChestTypes.WOOD, ChestTypes.CHRISTMAS, "item.ironchest.wood_christmas_upgrade.tooltip"),
    WOOD_TO_IRON(ChestTypes.WOOD, ChestTypes.IRON, "item.ironchest.wood_iron_upgrade.tooltip"),
    WOOD_TO_GOLD(ChestTypes.WOOD, ChestTypes.GOLD, "item.ironchest.wood_gold_upgrade.tooltip"),
    WOOD_TO_DIAMOND(ChestTypes.WOOD, ChestTypes.DIAMOND, "item.ironchest.wood_diamond_upgrade.tooltip"),
    WOOD_TO_EMERALD(ChestTypes.WOOD, ChestTypes.EMERALD, "item.ironchest.wood_emerald_upgrade.tooltip"),
    WOOD_TO_CRYSTAL(ChestTypes.WOOD, ChestTypes.CRYSTAL, "item.ironchest.wood_crystal_upgrade.tooltip"),
    WOOD_TO_OBSIDIAN(ChestTypes.WOOD, ChestTypes.OBSIDIAN, "item.ironchest.wood_obsidian_upgrade.tooltip"),
    COPPER_TO_IRON(ChestTypes.COPPER, ChestTypes.IRON, "item.ironchest.copper_iron_upgrade.tooltip"),
    COPPER_TO_GOLD(ChestTypes.COPPER, ChestTypes.GOLD, "item.ironchest.copper_gold_upgrade.tooltip"),
    COPPER_TO_DIAMOND(ChestTypes.COPPER, ChestTypes.DIAMOND, "item.ironchest.copper_diamond_upgrade.tooltip"),
    COPPER_TO_EMERALD(ChestTypes.COPPER, ChestTypes.EMERALD, "item.ironchest.copper_emerald_upgrade.tooltip"),
    COPPER_TO_CRYSTAL(ChestTypes.COPPER, ChestTypes.CRYSTAL, "item.ironchest.copper_crystal_upgrade.tooltip"),
    COPPER_TO_OBSIDIAN(ChestTypes.COPPER, ChestTypes.OBSIDIAN, "item.ironchest.copper_obsidian_upgrade.tooltip"),
    IRON_TO_GOLD(ChestTypes.IRON, ChestTypes.GOLD, "item.ironchest.iron_gold_upgrade.tooltip"),
    IRON_TO_DIAMOND(ChestTypes.IRON, ChestTypes.DIAMOND, "item.ironchest.iron_diamond_upgrade.tooltip"),
    IRON_TO_EMERALD(ChestTypes.IRON, ChestTypes.EMERALD, "item.ironchest.iron_emerald_upgrade.tooltip"),
    IRON_TO_CRYSTAL(ChestTypes.IRON, ChestTypes.CRYSTAL, "item.ironchest.iron_crystal_upgrade.tooltip"),
    IRON_TO_OBSIDIAN(ChestTypes.IRON, ChestTypes.OBSIDIAN, "item.ironchest.iron_obsidian_upgrade.tooltip"),
    GOLD_TO_DIAMOND(ChestTypes.GOLD, ChestTypes.DIAMOND, "item.ironchest.gold_diamond_upgrade.tooltip"),
    GOLD_TO_EMERALD(ChestTypes.GOLD, ChestTypes.EMERALD, "item.ironchest.gold_emerald_upgrade.tooltip"),
    GOLD_TO_CRYSTAL(ChestTypes.GOLD, ChestTypes.CRYSTAL, "item.ironchest.gold_crystal_upgrade.tooltip"),
    GOLD_TO_OBSIDIAN(ChestTypes.GOLD, ChestTypes.OBSIDIAN, "item.ironchest.gold_obsidian_upgrade.tooltip"),
    DIAMOND_TO_CRYSTAL(ChestTypes.DIAMOND, ChestTypes.CRYSTAL, "item.ironchest.diamond_crystal_upgrade.tooltip"),
    DIAMOND_TO_OBSIDIAN(ChestTypes.DIAMOND, ChestTypes.OBSIDIAN, "item.ironchest.diamond_obsidian_upgrade.tooltip"),
    EMERALD_TO_CRYSTAL(ChestTypes.EMERALD, ChestTypes.CRYSTAL, "item.ironchest.emerald_crystal_upgrade.tooltip"),
    EMERALD_TO_OBSIDIAN(ChestTypes.EMERALD, ChestTypes.OBSIDIAN, "item.ironchest.emerald_obsidian_upgrade.tooltip");

    public final ChestTypes source;
    public final ChestTypes target;
    public final String tooltipKey;

    UpgradeTypes(ChestTypes source, ChestTypes target, String tooltipKey) {
        this.source = source;
        this.target = target;
        this.tooltipKey = tooltipKey;
    }
}
