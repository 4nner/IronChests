package com.gathertocraft.ironchest.registry;

import com.gathertocraft.ironchest.IronChestsCommon;
import com.gathertocraft.ironchest.blocks.ChestTypes;
import com.gathertocraft.ironchest.items.ChestBlockItem;
import com.gathertocraft.ironcore.platform.Platforms;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;

public class ModItems {

  private static final Map<String, Supplier<Item>> ITEMS = new LinkedHashMap<>();
  private static final Map<String, Supplier<Item>> BOUND = new LinkedHashMap<>();

  private static final List<String> TAB_ORDER =
      List.of(
          "dirt_chest",
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
    for (ChestTypes type : ChestTypes.PLAYABLE) {
      registerChest(type);
    }
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

  private static void registerChest(ChestTypes type) {
    register(
        type.registryId,
        () -> {
          Item.Properties properties =
              blockItemSettings(type.registryId)
                  .component(
                      DataComponents.LORE,
                      new ItemLore(List.of(), ChestBlockItem.tooltipLines(type)));
          if (type == ChestTypes.NETHERITE) {
            properties = properties.fireResistant();
          }
          return new ChestBlockItem(type, properties);
        });
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
