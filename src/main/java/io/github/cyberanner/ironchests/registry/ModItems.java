package io.github.cyberanner.ironchests.registry;

import io.github.cyberanner.ironchests.IronChests;
import io.github.cyberanner.ironchests.items.UpgradeItem;
import io.github.cyberanner.ironchests.items.UpgradeTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    // Upgrade Items
    public static final Item WOOD_COPPER_UPGRADE = new UpgradeItem(UpgradeTypes.WOOD_TO_COPPER);
    public static final Item COPPER_IRON_UPGRADE = new UpgradeItem(UpgradeTypes.COPPER_TO_IRON);
    public static final Item IRON_GOLD_UPGRADE = new UpgradeItem(UpgradeTypes.IRON_TO_GOLD);
    public static final Item GOLD_DIAMOND_UPGRADE = new UpgradeItem(UpgradeTypes.GOLD_TO_DIAMOND);
    public static final Item DIAMOND_OBSIDIAN_UPGRADE = new UpgradeItem(UpgradeTypes.DIAMOND_TO_OBSIDIAN);

    // Chest Items
    public static final BlockItem COPPER_CHEST = new BlockItem(ModBlocks.COPPER_CHEST, new Item.Settings().group(IronChests.TAB));
    public static final BlockItem IRON_CHEST = new BlockItem(ModBlocks.IRON_CHEST, new Item.Settings().group(IronChests.TAB));
    public static final BlockItem GOLD_CHEST = new BlockItem(ModBlocks.GOLD_CHEST, new Item.Settings().group(IronChests.TAB));
    public static final BlockItem DIAMOND_CHEST = new BlockItem(ModBlocks.DIAMOND_CHEST, new Item.Settings().group(IronChests.TAB));
    public static final BlockItem OBSIDIAN_CHEST = new BlockItem(ModBlocks.OBSIDIAN_CHEST, new Item.Settings().group(IronChests.TAB));

    public static void registerItems() {
        // Item
        Registry.register(Registry.ITEM, new Identifier(IronChests.MOD_ID, "wood_copper_upgrade"), WOOD_COPPER_UPGRADE);
        Registry.register(Registry.ITEM, new Identifier(IronChests.MOD_ID, "copper_iron_upgrade"), COPPER_IRON_UPGRADE);
        Registry.register(Registry.ITEM, new Identifier(IronChests.MOD_ID, "iron_gold_upgrade"), IRON_GOLD_UPGRADE);
        Registry.register(Registry.ITEM, new Identifier(IronChests.MOD_ID, "gold_diamond_upgrade"), GOLD_DIAMOND_UPGRADE);
        Registry.register(Registry.ITEM, new Identifier(IronChests.MOD_ID, "diamond_obsidian_upgrade"), DIAMOND_OBSIDIAN_UPGRADE);

        // Block Items
        Registry.register(Registry.ITEM, new Identifier(IronChests.MOD_ID, "copper_chest"), COPPER_CHEST);
        Registry.register(Registry.ITEM, new Identifier(IronChests.MOD_ID, "iron_chest"), IRON_CHEST);
        Registry.register(Registry.ITEM, new Identifier(IronChests.MOD_ID, "gold_chest"), GOLD_CHEST);
        Registry.register(Registry.ITEM, new Identifier(IronChests.MOD_ID, "diamond_chest"), DIAMOND_CHEST);
        Registry.register(Registry.ITEM, new Identifier(IronChests.MOD_ID, "obsidian_chest"), OBSIDIAN_CHEST);
    }
}
