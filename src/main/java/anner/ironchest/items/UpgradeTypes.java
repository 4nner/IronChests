package anner.ironchest.items;

import anner.ironchest.blocks.ChestTypes;

public enum UpgradeTypes {
    WOOD_TO_COPPER(ChestTypes.WOOD, ChestTypes.COPPER),
    WOOD_TO_CHRISTMAS(ChestTypes.WOOD, ChestTypes.CHRISTMAS),
    WOOD_TO_IRON(ChestTypes.WOOD, ChestTypes.IRON),
    WOOD_TO_GOLD(ChestTypes.WOOD, ChestTypes.GOLD),
    WOOD_TO_DIAMOND(ChestTypes.WOOD, ChestTypes.DIAMOND),
    WOOD_TO_EMERALD(ChestTypes.WOOD, ChestTypes.EMERALD),
    WOOD_TO_CRYSTAL(ChestTypes.WOOD, ChestTypes.CRYSTAL),
    WOOD_TO_OBSIDIAN(ChestTypes.WOOD, ChestTypes.OBSIDIAN),
    COPPER_TO_IRON(ChestTypes.COPPER, ChestTypes.IRON),
    COPPER_TO_GOLD(ChestTypes.COPPER, ChestTypes.GOLD),
    COPPER_TO_DIAMOND(ChestTypes.COPPER, ChestTypes.DIAMOND),
    COPPER_TO_EMERALD(ChestTypes.COPPER, ChestTypes.EMERALD),
    COPPER_TO_CRYSTAL(ChestTypes.COPPER, ChestTypes.CRYSTAL),
    COPPER_TO_OBSIDIAN(ChestTypes.COPPER, ChestTypes.OBSIDIAN),
    IRON_TO_GOLD(ChestTypes.IRON, ChestTypes.GOLD),
    IRON_TO_DIAMOND(ChestTypes.IRON, ChestTypes.DIAMOND),
    IRON_TO_EMERALD(ChestTypes.IRON, ChestTypes.EMERALD),
    IRON_TO_CRYSTAL(ChestTypes.IRON, ChestTypes.CRYSTAL),
    IRON_TO_OBSIDIAN(ChestTypes.IRON, ChestTypes.OBSIDIAN),
    GOLD_TO_DIAMOND(ChestTypes.GOLD, ChestTypes.DIAMOND),
    GOLD_TO_EMERALD(ChestTypes.GOLD, ChestTypes.EMERALD),
    GOLD_TO_CRYSTAL(ChestTypes.GOLD, ChestTypes.CRYSTAL),
    GOLD_TO_OBSIDIAN(ChestTypes.GOLD, ChestTypes.OBSIDIAN),
    DIAMOND_TO_CRYSTAL(ChestTypes.DIAMOND, ChestTypes.CRYSTAL),
    DIAMOND_TO_OBSIDIAN(ChestTypes.DIAMOND, ChestTypes.OBSIDIAN),
    EMERALD_TO_CRYSTAL(ChestTypes.EMERALD, ChestTypes.CRYSTAL),
    EMERALD_TO_OBSIDIAN(ChestTypes.EMERALD, ChestTypes.OBSIDIAN);

    public final ChestTypes source;
    public final ChestTypes target;

    UpgradeTypes(ChestTypes source, ChestTypes target) {
        this.source = source;
        this.target = target;
    }

    public boolean canUpgrade(ChestTypes from) {
        return from == this.source;
    }

    // Upgrade Tooltips
    public static String tooltip(UpgradeTypes type) {
        return switch (type) {
            case WOOD_TO_CHRISTMAS -> "item.ironchest.wood_christmas_upgrade.tooltip";
            case WOOD_TO_COPPER -> "item.ironchest.wood_copper_upgrade.tooltip";
            case WOOD_TO_IRON -> "item.ironchest.wood_iron_upgrade.tooltip";
            case WOOD_TO_GOLD -> "item.ironchest.wood_gold_upgrade.tooltip";
            case WOOD_TO_DIAMOND -> "item.ironchest.wood_diamond_upgrade.tooltip";
            case WOOD_TO_EMERALD -> "item.ironchest.wood_emerald_upgrade.tooltip";
            case WOOD_TO_CRYSTAL -> "item.ironchest.wood_crystal_upgrade.tooltip";
            case WOOD_TO_OBSIDIAN -> "item.ironchest.wood_obsidian_upgrade.tooltip";
            case COPPER_TO_IRON -> "item.ironchest.copper_iron_upgrade.tooltip";
            case COPPER_TO_GOLD -> "item.ironchest.copper_gold_upgrade.tooltip";
            case COPPER_TO_DIAMOND -> "item.ironchest.copper_diamond_upgrade.tooltip";
            case COPPER_TO_EMERALD -> "item.ironchest.copper_emerald_upgrade.tooltip";
            case COPPER_TO_CRYSTAL -> "item.ironchest.copper_crystal_upgrade.tooltip";
            case COPPER_TO_OBSIDIAN -> "item.ironchest.copper_obsidian_upgrade.tooltip";
            case IRON_TO_GOLD -> "item.ironchest.iron_gold_upgrade.tooltip";
            case IRON_TO_DIAMOND -> "item.ironchest.iron_diamond_upgrade.tooltip";
            case IRON_TO_EMERALD -> "item.ironchest.iron_emerald_upgrade.tooltip";
            case IRON_TO_CRYSTAL -> "item.ironchest.iron_crystal_upgrade.tooltip";
            case IRON_TO_OBSIDIAN -> "item.ironchest.iron_obsidian_upgrade.tooltip";
            case GOLD_TO_DIAMOND -> "item.ironchest.gold_diamond_upgrade.tooltip";
            case GOLD_TO_EMERALD -> "item.ironchest.gold_emerald_upgrade.tooltip";
            case GOLD_TO_CRYSTAL -> "item.ironchest.gold_crystal_upgrade.tooltip";
            case GOLD_TO_OBSIDIAN -> "item.ironchest.gold_obsidian_upgrade.tooltip";
            case DIAMOND_TO_CRYSTAL -> "item.ironchest.diamond_crystal_upgrade.tooltip";
            case DIAMOND_TO_OBSIDIAN -> "item.ironchest.diamond_obsidian_upgrade.tooltip";
            case EMERALD_TO_CRYSTAL -> "item.ironchest.emerald_crystal_upgrade.tooltip";
            case EMERALD_TO_OBSIDIAN -> "item.ironchest.emerald_obsidian_upgrade.tooltip";
        };
    }
}
