package anner.ironchest.registry;

import anner.ironchest.IronChests;
import anner.ironchest.items.UpgradeItem;
import anner.ironchest.items.UpgradeTypes;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModItems {

    private static final Map<String, Item> ITEMS_BY_ID = new LinkedHashMap<>();

    public static final Item WOOD_COPPER_UPGRADE = register("wood_copper_upgrade", new UpgradeItem(UpgradeTypes.WOOD_TO_COPPER, upgradeItemSettings("wood_copper_upgrade", UpgradeTypes.WOOD_TO_COPPER)));
    public static final Item WOOD_IRON_UPGRADE = register("wood_iron_upgrade", new UpgradeItem(UpgradeTypes.WOOD_TO_IRON, upgradeItemSettings("wood_iron_upgrade", UpgradeTypes.WOOD_TO_IRON)));
    public static final Item WOOD_GOLD_UPGRADE = register("wood_gold_upgrade", new UpgradeItem(UpgradeTypes.WOOD_TO_GOLD, upgradeItemSettings("wood_gold_upgrade", UpgradeTypes.WOOD_TO_GOLD)));
    public static final Item WOOD_DIAMOND_UPGRADE = register("wood_diamond_upgrade", new UpgradeItem(UpgradeTypes.WOOD_TO_DIAMOND, upgradeItemSettings("wood_diamond_upgrade", UpgradeTypes.WOOD_TO_DIAMOND)));
    public static final Item WOOD_EMERALD_UPGRADE = register("wood_emerald_upgrade", new UpgradeItem(UpgradeTypes.WOOD_TO_EMERALD, upgradeItemSettings("wood_emerald_upgrade", UpgradeTypes.WOOD_TO_EMERALD)));
    public static final Item WOOD_CRYSTAL_UPGRADE = register("wood_crystal_upgrade", new UpgradeItem(UpgradeTypes.WOOD_TO_CRYSTAL, upgradeItemSettings("wood_crystal_upgrade", UpgradeTypes.WOOD_TO_CRYSTAL)));
    public static final Item WOOD_OBSIDIAN_UPGRADE = register("wood_obsidian_upgrade", new UpgradeItem(UpgradeTypes.WOOD_TO_OBSIDIAN, upgradeItemSettings("wood_obsidian_upgrade", UpgradeTypes.WOOD_TO_OBSIDIAN)));

    public static final Item COPPER_IRON_UPGRADE = register("copper_iron_upgrade", new UpgradeItem(UpgradeTypes.COPPER_TO_IRON, upgradeItemSettings("copper_iron_upgrade", UpgradeTypes.COPPER_TO_IRON)));
    public static final Item COPPER_GOLD_UPGRADE = register("copper_gold_upgrade", new UpgradeItem(UpgradeTypes.COPPER_TO_GOLD, upgradeItemSettings("copper_gold_upgrade", UpgradeTypes.COPPER_TO_GOLD)));
    public static final Item COPPER_DIAMOND_UPGRADE = register("copper_diamond_upgrade", new UpgradeItem(UpgradeTypes.COPPER_TO_DIAMOND, upgradeItemSettings("copper_diamond_upgrade", UpgradeTypes.COPPER_TO_DIAMOND)));
    public static final Item COPPER_EMERALD_UPGRADE = register("copper_emerald_upgrade", new UpgradeItem(UpgradeTypes.COPPER_TO_EMERALD, upgradeItemSettings("copper_emerald_upgrade", UpgradeTypes.COPPER_TO_EMERALD)));
    public static final Item COPPER_CRYSTAL_UPGRADE = register("copper_crystal_upgrade", new UpgradeItem(UpgradeTypes.COPPER_TO_CRYSTAL, upgradeItemSettings("copper_crystal_upgrade", UpgradeTypes.COPPER_TO_CRYSTAL)));
    public static final Item COPPER_OBSIDIAN_UPGRADE = register("copper_obsidian_upgrade", new UpgradeItem(UpgradeTypes.COPPER_TO_OBSIDIAN, upgradeItemSettings("copper_obsidian_upgrade", UpgradeTypes.COPPER_TO_OBSIDIAN)));

    public static final Item IRON_GOLD_UPGRADE = register("iron_gold_upgrade", new UpgradeItem(UpgradeTypes.IRON_TO_GOLD, upgradeItemSettings("iron_gold_upgrade", UpgradeTypes.IRON_TO_GOLD)));
    public static final Item IRON_DIAMOND_UPGRADE = register("iron_diamond_upgrade", new UpgradeItem(UpgradeTypes.IRON_TO_DIAMOND, upgradeItemSettings("iron_diamond_upgrade", UpgradeTypes.IRON_TO_DIAMOND)));
    public static final Item IRON_EMERALD_UPGRADE = register("iron_emerald_upgrade", new UpgradeItem(UpgradeTypes.IRON_TO_EMERALD, upgradeItemSettings("iron_emerald_upgrade", UpgradeTypes.IRON_TO_EMERALD)));
    public static final Item IRON_CRYSTAL_UPGRADE = register("iron_crystal_upgrade", new UpgradeItem(UpgradeTypes.IRON_TO_CRYSTAL, upgradeItemSettings("iron_crystal_upgrade", UpgradeTypes.IRON_TO_CRYSTAL)));
    public static final Item IRON_OBSIDIAN_UPGRADE = register("iron_obsidian_upgrade", new UpgradeItem(UpgradeTypes.IRON_TO_OBSIDIAN, upgradeItemSettings("iron_obsidian_upgrade", UpgradeTypes.IRON_TO_OBSIDIAN)));

    public static final Item GOLD_DIAMOND_UPGRADE = register("gold_diamond_upgrade", new UpgradeItem(UpgradeTypes.GOLD_TO_DIAMOND, upgradeItemSettings("gold_diamond_upgrade", UpgradeTypes.GOLD_TO_DIAMOND)));
    public static final Item GOLD_EMERALD_UPGRADE = register("gold_emerald_upgrade", new UpgradeItem(UpgradeTypes.GOLD_TO_EMERALD, upgradeItemSettings("gold_emerald_upgrade", UpgradeTypes.GOLD_TO_EMERALD)));
    public static final Item GOLD_CRYSTAL_UPGRADE = register("gold_crystal_upgrade", new UpgradeItem(UpgradeTypes.GOLD_TO_CRYSTAL, upgradeItemSettings("gold_crystal_upgrade", UpgradeTypes.GOLD_TO_CRYSTAL)));
    public static final Item GOLD_OBSIDIAN_UPGRADE = register("gold_obsidian_upgrade", new UpgradeItem(UpgradeTypes.GOLD_TO_OBSIDIAN, upgradeItemSettings("gold_obsidian_upgrade", UpgradeTypes.GOLD_TO_OBSIDIAN)));

    public static final Item DIAMOND_OBSIDIAN_UPGRADE = register("diamond_obsidian_upgrade", new UpgradeItem(UpgradeTypes.DIAMOND_TO_OBSIDIAN, upgradeItemSettings("diamond_obsidian_upgrade", UpgradeTypes.DIAMOND_TO_OBSIDIAN)));
    public static final Item DIAMOND_CRYSTAL_UPGRADE = register("diamond_crystal_upgrade", new UpgradeItem(UpgradeTypes.DIAMOND_TO_CRYSTAL, upgradeItemSettings("diamond_crystal_upgrade", UpgradeTypes.DIAMOND_TO_CRYSTAL)));

    public static final Item EMERALD_OBSIDIAN_UPGRADE = register("emerald_obsidian_upgrade", new UpgradeItem(UpgradeTypes.EMERALD_TO_OBSIDIAN, upgradeItemSettings("emerald_obsidian_upgrade", UpgradeTypes.EMERALD_TO_OBSIDIAN)));
    public static final Item EMERALD_CRYSTAL_UPGRADE = register("emerald_crystal_upgrade", new UpgradeItem(UpgradeTypes.EMERALD_TO_CRYSTAL, upgradeItemSettings("emerald_crystal_upgrade", UpgradeTypes.EMERALD_TO_CRYSTAL)));

    public static final Item WOOD_CHRISTMAS_UPGRADE = register("wood_christmas_upgrade", new UpgradeItem(UpgradeTypes.WOOD_TO_CHRISTMAS, upgradeItemSettings("wood_christmas_upgrade", UpgradeTypes.WOOD_TO_CHRISTMAS)));

    public static final BlockItem COPPER_CHEST = register("copper_chest", new BlockItem(ModBlocks.COPPER_CHEST, blockItemSettings("copper_chest")));
    public static final BlockItem IRON_CHEST = register("iron_chest", new BlockItem(ModBlocks.IRON_CHEST, blockItemSettings("iron_chest")));
    public static final BlockItem GOLD_CHEST = register("gold_chest", new BlockItem(ModBlocks.GOLD_CHEST, blockItemSettings("gold_chest")));
    public static final BlockItem DIAMOND_CHEST = register("diamond_chest", new BlockItem(ModBlocks.DIAMOND_CHEST, blockItemSettings("diamond_chest")));
    public static final BlockItem EMERALD_CHEST = register("emerald_chest", new BlockItem(ModBlocks.EMERALD_CHEST, blockItemSettings("emerald_chest")));
    public static final BlockItem CRYSTAL_CHEST = register("crystal_chest", new BlockItem(ModBlocks.CRYSTAL_CHEST, blockItemSettings("crystal_chest")));
    public static final BlockItem OBSIDIAN_CHEST = register("obsidian_chest", new BlockItem(ModBlocks.OBSIDIAN_CHEST, blockItemSettings("obsidian_chest")));
    public static final BlockItem NETHERITE_CHEST = register("netherite_chest", new BlockItem(ModBlocks.NETHERITE_CHEST, blockItemSettings("netherite_chest").fireResistant()));
    public static final BlockItem CHRISTMAS_CHEST = register("christmas_chest", new BlockItem(ModBlocks.CHRISTMAS_CHEST, blockItemSettings("christmas_chest")));

    public static void registerItems() {
        ITEMS_BY_ID.forEach((id, item) ->
            Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(IronChests.MOD_ID, id), item)
        );

        CreativeModeTabEvents.modifyOutputEvent(IronChests.TAB).register(output -> {
            output.accept(COPPER_CHEST);
            output.accept(IRON_CHEST);
            output.accept(GOLD_CHEST);
            output.accept(DIAMOND_CHEST);
            output.accept(EMERALD_CHEST);
            output.accept(CRYSTAL_CHEST);
            output.accept(OBSIDIAN_CHEST);
            output.accept(NETHERITE_CHEST);
            output.accept(CHRISTMAS_CHEST);
            output.accept(WOOD_COPPER_UPGRADE);
            output.accept(WOOD_IRON_UPGRADE);
            output.accept(WOOD_GOLD_UPGRADE);
            output.accept(WOOD_DIAMOND_UPGRADE);
            output.accept(WOOD_EMERALD_UPGRADE);
            output.accept(WOOD_CRYSTAL_UPGRADE);
            output.accept(WOOD_OBSIDIAN_UPGRADE);
            output.accept(COPPER_IRON_UPGRADE);
            output.accept(COPPER_GOLD_UPGRADE);
            output.accept(COPPER_DIAMOND_UPGRADE);
            output.accept(COPPER_EMERALD_UPGRADE);
            output.accept(COPPER_CRYSTAL_UPGRADE);
            output.accept(COPPER_OBSIDIAN_UPGRADE);
            output.accept(IRON_GOLD_UPGRADE);
            output.accept(IRON_DIAMOND_UPGRADE);
            output.accept(IRON_EMERALD_UPGRADE);
            output.accept(IRON_CRYSTAL_UPGRADE);
            output.accept(IRON_OBSIDIAN_UPGRADE);
            output.accept(GOLD_DIAMOND_UPGRADE);
            output.accept(GOLD_EMERALD_UPGRADE);
            output.accept(GOLD_CRYSTAL_UPGRADE);
            output.accept(GOLD_OBSIDIAN_UPGRADE);
            output.accept(DIAMOND_CRYSTAL_UPGRADE);
            output.accept(DIAMOND_OBSIDIAN_UPGRADE);
            output.accept(EMERALD_CRYSTAL_UPGRADE);
            output.accept(EMERALD_OBSIDIAN_UPGRADE);
            output.accept(WOOD_CHRISTMAS_UPGRADE);
        });
    }

    private static <T extends Item> T register(String id, T item) {
        ITEMS_BY_ID.put(id, item);
        return item;
    }

    private static Item.Properties settings(String name) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(IronChests.MOD_ID, name)));
    }

    private static Item.Properties blockItemSettings(String name) {
        return settings(name).useBlockDescriptionPrefix();
    }

    private static Item.Properties upgradeItemSettings(String name, UpgradeTypes type) {
        return settings(name).component(
            DataComponents.LORE,
            new ItemLore(List.of(), List.of(Component.translatable(type.tooltipKey).withStyle(ChatFormatting.GREEN)))
        );
    }
}
