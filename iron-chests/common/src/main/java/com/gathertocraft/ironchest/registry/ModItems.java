package com.gathertocraft.ironchest.registry;

import com.gathertocraft.ironchest.IronChestsCommon;
import com.gathertocraft.ironchest.blocks.ChestTypes;
import com.gathertocraft.ironcore.platform.Platforms;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

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
          "christmas_chest");

  static {
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
}
