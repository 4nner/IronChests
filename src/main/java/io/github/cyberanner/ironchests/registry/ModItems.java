package io.github.cyberanner.ironchests.registry;

import io.github.cyberanner.ironchests.IronChests;
import io.github.cyberanner.ironchests.items.UpgradeItem;
import io.github.cyberanner.ironchests.items.UpgradeTypes;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModItems {

    // Upgrade Items
    public static final Item WOOD_COPPER_UPGRADE = new UpgradeItem(UpgradeTypes.WOOD_TO_COPPER);
    public static final Item WOOD_IRON_UPGRADE = new UpgradeItem(UpgradeTypes.WOOD_TO_IRON);
    public static final Item WOOD_GOLD_UPGRADE = new UpgradeItem(UpgradeTypes.WOOD_TO_GOLD);
    public static final Item WOOD_DIAMOND_UPGRADE = new UpgradeItem(UpgradeTypes.WOOD_TO_DIAMOND);
    public static final Item WOOD_EMERALD_UPGRADE = new UpgradeItem(UpgradeTypes.WOOD_TO_EMERALD);
    public static final Item WOOD_CRYSTAL_UPGRADE = new UpgradeItem(UpgradeTypes.WOOD_TO_CRYSTAL);
    public static final Item WOOD_OBSIDIAN_UPGRADE = new UpgradeItem(UpgradeTypes.WOOD_TO_OBSIDIAN);

    public static final Item COPPER_IRON_UPGRADE = new UpgradeItem(UpgradeTypes.COPPER_TO_IRON);
    public static final Item COPPER_GOLD_UPGRADE = new UpgradeItem(UpgradeTypes.COPPER_TO_GOLD);
    public static final Item COPPER_DIAMOND_UPGRADE = new UpgradeItem(UpgradeTypes.COPPER_TO_DIAMOND);
    public static final Item COPPER_EMERALD_UPGRADE = new UpgradeItem(UpgradeTypes.COPPER_TO_EMERALD);
    public static final Item COPPER_CRYSTAL_UPGRADE = new UpgradeItem(UpgradeTypes.COPPER_TO_CRYSTAL);
    public static final Item COPPER_OBSIDIAN_UPGRADE = new UpgradeItem(UpgradeTypes.COPPER_TO_OBSIDIAN);

    public static final Item IRON_GOLD_UPGRADE = new UpgradeItem(UpgradeTypes.IRON_TO_GOLD);
    public static final Item IRON_DIAMOND_UPGRADE = new UpgradeItem(UpgradeTypes.IRON_TO_DIAMOND);
    public static final Item IRON_EMERALD_UPGRADE = new UpgradeItem(UpgradeTypes.IRON_TO_EMERALD);
    public static final Item IRON_CRYSTAL_UPGRADE = new UpgradeItem(UpgradeTypes.IRON_TO_CRYSTAL);
    public static final Item IRON_OBSIDIAN_UPGRADE = new UpgradeItem(UpgradeTypes.IRON_TO_OBSIDIAN);

    public static final Item GOLD_DIAMOND_UPGRADE = new UpgradeItem(UpgradeTypes.GOLD_TO_DIAMOND);
    public static final Item GOLD_EMERALD_UPGRADE = new UpgradeItem(UpgradeTypes.GOLD_TO_EMERALD);
    public static final Item GOLD_CRYSTAL_UPGRADE = new UpgradeItem(UpgradeTypes.GOLD_TO_CRYSTAL);
    public static final Item GOLD_OBSIDIAN_UPGRADE = new UpgradeItem(UpgradeTypes.GOLD_TO_OBSIDIAN);

    public static final Item DIAMOND_OBSIDIAN_UPGRADE = new UpgradeItem(UpgradeTypes.DIAMOND_TO_OBSIDIAN);
    public static final Item DIAMOND_CRYSTAL_UPGRADE = new UpgradeItem(UpgradeTypes.DIAMOND_TO_CRYSTAL);

    public static final Item EMERALD_OBSIDIAN_UPGRADE = new UpgradeItem(UpgradeTypes.EMERALD_TO_OBSIDIAN);
    public static final Item EMERALD_CRYSTAL_UPGRADE = new UpgradeItem(UpgradeTypes.EMERALD_TO_CRYSTAL);

    public static final Item WOOD_CHRISTMAS_UPGRADE = new UpgradeItem(UpgradeTypes.WOOD_TO_CHRISTMAS);

    // Chest Items
    public static final BlockItem COPPER_CHEST = new BlockItem(ModBlocks.COPPER_CHEST, new Item.Settings());
    public static final BlockItem IRON_CHEST = new BlockItem(ModBlocks.IRON_CHEST, new Item.Settings());
    public static final BlockItem GOLD_CHEST = new BlockItem(ModBlocks.GOLD_CHEST, new Item.Settings());
    public static final BlockItem DIAMOND_CHEST = new BlockItem(ModBlocks.DIAMOND_CHEST, new Item.Settings());
    public static final BlockItem EMERALD_CHEST = new BlockItem(ModBlocks.EMERALD_CHEST, new Item.Settings());
    public static final BlockItem CRYSTAL_CHEST = new BlockItem(ModBlocks.CRYSTAL_CHEST, new Item.Settings());
    public static final BlockItem OBSIDIAN_CHEST = new BlockItem(ModBlocks.OBSIDIAN_CHEST, new Item.Settings());
    public static final BlockItem CHRISTMAS_CHEST = new BlockItem(ModBlocks.CHRISTMAS_CHEST, new Item.Settings());

    public static void registerItems() {
        // Item
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "wood_copper_upgrade"), WOOD_COPPER_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "wood_iron_upgrade"), WOOD_IRON_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "wood_gold_upgrade"), WOOD_GOLD_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "wood_diamond_upgrade"), WOOD_DIAMOND_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "wood_emerald_upgrade"), WOOD_EMERALD_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "wood_crystal_upgrade"), WOOD_CRYSTAL_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "wood_obsidian_upgrade"), WOOD_OBSIDIAN_UPGRADE);

        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "copper_iron_upgrade"), COPPER_IRON_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "copper_gold_upgrade"), COPPER_GOLD_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "copper_diamond_upgrade"), COPPER_DIAMOND_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "copper_emerald_upgrade"), COPPER_EMERALD_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "copper_crystal_upgrade"), COPPER_CRYSTAL_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "copper_obsidian_upgrade"), COPPER_OBSIDIAN_UPGRADE);

        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "iron_gold_upgrade"), IRON_GOLD_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "iron_diamond_upgrade"), IRON_DIAMOND_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "iron_emerald_upgrade"), IRON_EMERALD_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "iron_crystal_upgrade"), IRON_CRYSTAL_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "iron_obsidian_upgrade"), IRON_OBSIDIAN_UPGRADE);

        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "gold_diamond_upgrade"), GOLD_DIAMOND_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "gold_emerald_upgrade"), GOLD_EMERALD_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "gold_crystal_upgrade"), GOLD_CRYSTAL_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "gold_obsidian_upgrade"), GOLD_OBSIDIAN_UPGRADE);

        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "diamond_crystal_upgrade"), DIAMOND_CRYSTAL_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "diamond_obsidian_upgrade"), DIAMOND_OBSIDIAN_UPGRADE);

        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "emerald_crystal_upgrade"), EMERALD_CRYSTAL_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "emerald_obsidian_upgrade"), EMERALD_OBSIDIAN_UPGRADE);

        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "wood_christmas_upgrade"), WOOD_CHRISTMAS_UPGRADE);

        // Block Items
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "copper_chest"), COPPER_CHEST);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "iron_chest"), IRON_CHEST);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "gold_chest"), GOLD_CHEST);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "diamond_chest"), DIAMOND_CHEST);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "emerald_chest"), EMERALD_CHEST);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "crystal_chest"), CRYSTAL_CHEST);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "obsidian_chest"), OBSIDIAN_CHEST);
        Registry.register(Registries.ITEM, new Identifier(IronChests.MOD_ID, "christmas_chest"), CHRISTMAS_CHEST);

	ItemGroupEvents.modifyEntriesEvent(IronChests.TAB).register(entries -> {
		entries.add(COPPER_CHEST);
		entries.add(IRON_CHEST);
		entries.add(GOLD_CHEST);
		entries.add(DIAMOND_CHEST);
		entries.add(EMERALD_CHEST);
		entries.add(CRYSTAL_CHEST);
		entries.add(OBSIDIAN_CHEST);
		entries.add(CHRISTMAS_CHEST);
	});
    }
}
