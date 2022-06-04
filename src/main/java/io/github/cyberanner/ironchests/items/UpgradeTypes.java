package io.github.cyberanner.ironchests.items;

import io.github.cyberanner.ironchests.blocks.ChestTypes;
import static io.github.cyberanner.ironchests.blocks.ChestTypes.*;

public enum UpgradeTypes {
    WOOD_TO_COPPER(WOOD, COPPER),
    WOOD_TO_CHRISTMAS(WOOD, CHRISTMAS),
    WOOD_TO_IRON(WOOD, IRON),
    WOOD_TO_GOLD(WOOD, GOLD),
    WOOD_TO_DIAMOND(WOOD, DIAMOND),
    WOOD_TO_EMERALD(WOOD, EMERALD),
    WOOD_TO_CRYSTAL(WOOD, CRYSTAL),
    WOOD_TO_OBSIDIAN(WOOD, OBSIDIAN),
    COPPER_TO_IRON(COPPER, IRON),
    COPPER_TO_GOLD(COPPER, GOLD),
    COPPER_TO_DIAMOND(COPPER, DIAMOND),
    COPPER_TO_EMERALD(COPPER, EMERALD),
    COPPER_TO_CRYSTAL(COPPER, CRYSTAL),
    COPPER_TO_OBSIDIAN(COPPER, OBSIDIAN),
    IRON_TO_GOLD(IRON, GOLD),
    IRON_TO_DIAMOND(IRON, DIAMOND),
    IRON_TO_EMERALD(IRON, EMERALD),
    IRON_TO_CRYSTAL(IRON, CRYSTAL),
    IRON_TO_OBSIDIAN(IRON, OBSIDIAN),
    GOLD_TO_DIAMOND(GOLD, DIAMOND),
    GOLD_TO_EMERALD(GOLD, EMERALD),
    GOLD_TO_CRYSTAL(GOLD, CRYSTAL),
    GOLD_TO_OBSIDIAN(GOLD, OBSIDIAN),
    DIAMOND_TO_CRYSTAL(DIAMOND, CRYSTAL),
    DIAMOND_TO_OBSIDIAN(DIAMOND, OBSIDIAN),
    EMERALD_TO_CRYSTAL(EMERALD, CRYSTAL),
    EMERALD_TO_OBSIDIAN(EMERALD, OBSIDIAN);

    public final ChestTypes source;
    public final ChestTypes target;

    UpgradeTypes(ChestTypes source, ChestTypes target) {
        this.source = source;
        this.target = target;
    }

    public boolean canUpgrade(ChestTypes from) {
        return from == this.source;
    }
}
