package anner.ironchest.registry;

import anner.ironchest.IronChestsCommon;
import anner.ironchest.blocks.ChestTypes;
import anner.ironchest.items.UpgradeItem;
import anner.ironchest.items.UpgradeTypes;
import anner.ironchest.platform.Platforms;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;

public class ModItems {

  private static final Map<String, Supplier<Item>> ITEMS = new LinkedHashMap<>();
  private static final Map<String, Supplier<Item>> BOUND = new LinkedHashMap<>();

  private static final List<String> TAB_ORDER =
      List.of(
          "copper_chest",
          "iron_chest",
          "gold_chest",
          "diamond_chest",
          "emerald_chest",
          "crystal_chest",
          "obsidian_chest",
          "netherite_chest",
          "christmas_chest",
          "wood_copper_upgrade",
          "wood_iron_upgrade",
          "wood_gold_upgrade",
          "wood_diamond_upgrade",
          "wood_emerald_upgrade",
          "wood_crystal_upgrade",
          "wood_obsidian_upgrade",
          "copper_iron_upgrade",
          "copper_gold_upgrade",
          "copper_diamond_upgrade",
          "copper_emerald_upgrade",
          "copper_crystal_upgrade",
          "copper_obsidian_upgrade",
          "iron_gold_upgrade",
          "iron_diamond_upgrade",
          "iron_emerald_upgrade",
          "iron_crystal_upgrade",
          "iron_obsidian_upgrade",
          "gold_diamond_upgrade",
          "gold_emerald_upgrade",
          "gold_crystal_upgrade",
          "gold_obsidian_upgrade",
          "diamond_crystal_upgrade",
          "diamond_obsidian_upgrade",
          "emerald_crystal_upgrade",
          "emerald_obsidian_upgrade",
          "wood_christmas_upgrade");

  static {
    register(
        "wood_copper_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.WOOD_TO_COPPER,
                upgradeItemSettings("wood_copper_upgrade", UpgradeTypes.WOOD_TO_COPPER)));
    register(
        "wood_iron_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.WOOD_TO_IRON,
                upgradeItemSettings("wood_iron_upgrade", UpgradeTypes.WOOD_TO_IRON)));
    register(
        "wood_gold_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.WOOD_TO_GOLD,
                upgradeItemSettings("wood_gold_upgrade", UpgradeTypes.WOOD_TO_GOLD)));
    register(
        "wood_diamond_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.WOOD_TO_DIAMOND,
                upgradeItemSettings("wood_diamond_upgrade", UpgradeTypes.WOOD_TO_DIAMOND)));
    register(
        "wood_emerald_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.WOOD_TO_EMERALD,
                upgradeItemSettings("wood_emerald_upgrade", UpgradeTypes.WOOD_TO_EMERALD)));
    register(
        "wood_crystal_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.WOOD_TO_CRYSTAL,
                upgradeItemSettings("wood_crystal_upgrade", UpgradeTypes.WOOD_TO_CRYSTAL)));
    register(
        "wood_obsidian_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.WOOD_TO_OBSIDIAN,
                upgradeItemSettings("wood_obsidian_upgrade", UpgradeTypes.WOOD_TO_OBSIDIAN)));
    register(
        "copper_iron_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.COPPER_TO_IRON,
                upgradeItemSettings("copper_iron_upgrade", UpgradeTypes.COPPER_TO_IRON)));
    register(
        "copper_gold_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.COPPER_TO_GOLD,
                upgradeItemSettings("copper_gold_upgrade", UpgradeTypes.COPPER_TO_GOLD)));
    register(
        "copper_diamond_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.COPPER_TO_DIAMOND,
                upgradeItemSettings("copper_diamond_upgrade", UpgradeTypes.COPPER_TO_DIAMOND)));
    register(
        "copper_emerald_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.COPPER_TO_EMERALD,
                upgradeItemSettings("copper_emerald_upgrade", UpgradeTypes.COPPER_TO_EMERALD)));
    register(
        "copper_crystal_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.COPPER_TO_CRYSTAL,
                upgradeItemSettings("copper_crystal_upgrade", UpgradeTypes.COPPER_TO_CRYSTAL)));
    register(
        "copper_obsidian_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.COPPER_TO_OBSIDIAN,
                upgradeItemSettings("copper_obsidian_upgrade", UpgradeTypes.COPPER_TO_OBSIDIAN)));
    register(
        "iron_gold_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.IRON_TO_GOLD,
                upgradeItemSettings("iron_gold_upgrade", UpgradeTypes.IRON_TO_GOLD)));
    register(
        "iron_diamond_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.IRON_TO_DIAMOND,
                upgradeItemSettings("iron_diamond_upgrade", UpgradeTypes.IRON_TO_DIAMOND)));
    register(
        "iron_emerald_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.IRON_TO_EMERALD,
                upgradeItemSettings("iron_emerald_upgrade", UpgradeTypes.IRON_TO_EMERALD)));
    register(
        "iron_crystal_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.IRON_TO_CRYSTAL,
                upgradeItemSettings("iron_crystal_upgrade", UpgradeTypes.IRON_TO_CRYSTAL)));
    register(
        "iron_obsidian_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.IRON_TO_OBSIDIAN,
                upgradeItemSettings("iron_obsidian_upgrade", UpgradeTypes.IRON_TO_OBSIDIAN)));
    register(
        "gold_diamond_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.GOLD_TO_DIAMOND,
                upgradeItemSettings("gold_diamond_upgrade", UpgradeTypes.GOLD_TO_DIAMOND)));
    register(
        "gold_emerald_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.GOLD_TO_EMERALD,
                upgradeItemSettings("gold_emerald_upgrade", UpgradeTypes.GOLD_TO_EMERALD)));
    register(
        "gold_crystal_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.GOLD_TO_CRYSTAL,
                upgradeItemSettings("gold_crystal_upgrade", UpgradeTypes.GOLD_TO_CRYSTAL)));
    register(
        "gold_obsidian_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.GOLD_TO_OBSIDIAN,
                upgradeItemSettings("gold_obsidian_upgrade", UpgradeTypes.GOLD_TO_OBSIDIAN)));
    register(
        "diamond_obsidian_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.DIAMOND_TO_OBSIDIAN,
                upgradeItemSettings("diamond_obsidian_upgrade", UpgradeTypes.DIAMOND_TO_OBSIDIAN)));
    register(
        "diamond_crystal_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.DIAMOND_TO_CRYSTAL,
                upgradeItemSettings("diamond_crystal_upgrade", UpgradeTypes.DIAMOND_TO_CRYSTAL)));
    register(
        "emerald_obsidian_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.EMERALD_TO_OBSIDIAN,
                upgradeItemSettings("emerald_obsidian_upgrade", UpgradeTypes.EMERALD_TO_OBSIDIAN)));
    register(
        "emerald_crystal_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.EMERALD_TO_CRYSTAL,
                upgradeItemSettings("emerald_crystal_upgrade", UpgradeTypes.EMERALD_TO_CRYSTAL)));
    register(
        "wood_christmas_upgrade",
        () ->
            new UpgradeItem(
                UpgradeTypes.WOOD_TO_CHRISTMAS,
                upgradeItemSettings("wood_christmas_upgrade", UpgradeTypes.WOOD_TO_CHRISTMAS)));
    register(
        "copper_chest",
        () -> new BlockItem(ChestTypes.COPPER.getBlock(), blockItemSettings("copper_chest")));
    register(
        "iron_chest",
        () -> new BlockItem(ChestTypes.IRON.getBlock(), blockItemSettings("iron_chest")));
    register(
        "gold_chest",
        () -> new BlockItem(ChestTypes.GOLD.getBlock(), blockItemSettings("gold_chest")));
    register(
        "diamond_chest",
        () -> new BlockItem(ChestTypes.DIAMOND.getBlock(), blockItemSettings("diamond_chest")));
    register(
        "emerald_chest",
        () -> new BlockItem(ChestTypes.EMERALD.getBlock(), blockItemSettings("emerald_chest")));
    register(
        "crystal_chest",
        () -> new BlockItem(ChestTypes.CRYSTAL.getBlock(), blockItemSettings("crystal_chest")));
    register(
        "obsidian_chest",
        () -> new BlockItem(ChestTypes.OBSIDIAN.getBlock(), blockItemSettings("obsidian_chest")));
    register(
        "netherite_chest",
        () ->
            new BlockItem(
                ChestTypes.NETHERITE.getBlock(),
                blockItemSettings("netherite_chest").fireResistant()));
    register(
        "christmas_chest",
        () -> new BlockItem(ChestTypes.CHRISTMAS.getBlock(), blockItemSettings("christmas_chest")));
  }

  public static void registerItems() {
    ITEMS.forEach(
        (id, supplier) ->
            BOUND.put(
                id,
                Platforms.registry()
                    .register(
                        BuiltInRegistries.ITEM,
                        Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, id),
                        supplier)));
  }

  public static void addTabItems(ResourceKey<CreativeModeTab> tab) {
    Platforms.registry()
        .addTabItems(tab, () -> TAB_ORDER.stream().map(id -> BOUND.get(id).get()).toList());
  }

  private static void register(String id, Supplier<Item> item) {
    ITEMS.put(id, item);
  }

  private static Item.Properties settings(String name) {
    return new Item.Properties()
        .setId(
            ResourceKey.create(
                Registries.ITEM, Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, name)));
  }

  private static Item.Properties blockItemSettings(String name) {
    return settings(name).useBlockDescriptionPrefix();
  }

  private static Item.Properties upgradeItemSettings(String name, UpgradeTypes type) {
    return settings(name)
        .component(
            DataComponents.LORE,
            new ItemLore(
                List.of(),
                List.of(Component.translatable(type.tooltipKey).withStyle(ChatFormatting.GREEN))));
  }
}
