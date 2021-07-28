package io.github.cyberanner.ironchests.items;

import io.github.cyberanner.ironchests.blocks.ChestTypes;
import static io.github.cyberanner.ironchests.blocks.ChestTypes.*;

public enum UpgradeTypes {
    WOOD_TO_COPPER(WOOD, COPPER),
    WOOD_TO_CHRISTMAS(WOOD, CHRISTMAS),
    COPPER_TO_IRON(COPPER, IRON),
    IRON_TO_GOLD(IRON, GOLD),
    GOLD_TO_DIAMOND(GOLD, DIAMOND),
    GOLD_TO_EMERALD(GOLD, EMERALD),
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
