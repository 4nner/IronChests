package com.gathertocraft.ironcore.registry;

import com.gathertocraft.ironcore.IronCoreCommon;
import com.gathertocraft.ironcore.MaterialTier;
import com.gathertocraft.ironcore.TieredUpgradeStrategy;
import com.gathertocraft.ironcore.UpgradeItem;
import com.gathertocraft.ironcore.handtruck.HandTruckItem;
import com.gathertocraft.ironcore.lock.KeyItem;
import com.gathertocraft.ironcore.platform.Platforms;
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
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;

public class ModCoreItems {

  private static final Map<String, Supplier<Item>> ITEMS = new LinkedHashMap<>();
  private static final Map<String, Supplier<Item>> BOUND = new LinkedHashMap<>();

  private static final List<String> TAB_ORDER =
      List.of(
          "copper_upgrade",
          "iron_upgrade",
          "gold_upgrade",
          "diamond_upgrade",
          "emerald_upgrade",
          "crystal_upgrade",
          "obsidian_upgrade",
          "netherite_upgrade",
          "container_key",
          "hand_truck");

  static {
    register("copper_upgrade", MaterialTier.COPPER);
    register("iron_upgrade", MaterialTier.IRON);
    register("gold_upgrade", MaterialTier.GOLD);
    register("diamond_upgrade", MaterialTier.DIAMOND);
    register("emerald_upgrade", MaterialTier.EMERALD);
    register("crystal_upgrade", MaterialTier.CRYSTAL);
    register("obsidian_upgrade", MaterialTier.OBSIDIAN);
    register("netherite_upgrade", MaterialTier.NETHERITE);
    ITEMS.put(
        "container_key",
        () ->
            new KeyItem(
                new Item.Properties()
                    .setId(
                        ResourceKey.create(
                            Registries.ITEM,
                            Identifier.fromNamespaceAndPath(
                                IronCoreCommon.MOD_ID, "container_key")))
                    .stacksTo(1)
                    .component(
                        DataComponents.LORE,
                        new ItemLore(
                            List.of(),
                            List.of(
                                Component.translatable("tooltip.ironcore.container_key.edit")
                                    .withStyle(ChatFormatting.GRAY),
                                Component.translatable("tooltip.ironcore.container_key.lock")
                                    .withStyle(ChatFormatting.GRAY),
                                Component.translatable("tooltip.ironcore.container_key.rename")
                                    .withStyle(ChatFormatting.DARK_GRAY))))));
    ITEMS.put("hand_truck", () -> new HandTruckItem(handTruckSettings("hand_truck", 25), false));
  }

  public static void registerItems() {
    ITEMS.forEach(
        (id, supplier) ->
            BOUND.put(
                id,
                Platforms.registry()
                    .register(
                        BuiltInRegistries.ITEM,
                        Identifier.fromNamespaceAndPath(IronCoreCommon.MOD_ID, id),
                        supplier)));
  }

  public static void addTabItems(ResourceKey<CreativeModeTab> tab) {
    Platforms.registry()
        .addTabItems(tab, () -> TAB_ORDER.stream().map(id -> BOUND.get(id).get()).toList());
  }

  public static Item get(String id) {
    return BOUND.get(id).get();
  }

  private static void register(String id, MaterialTier target) {
    ITEMS.put(id, () -> new UpgradeItem(new TieredUpgradeStrategy(target), settings(id)));
  }

  private static Item.Properties handTruckSettings(String name, int durability) {
    return new Item.Properties()
        .setId(
            ResourceKey.create(
                Registries.ITEM, Identifier.fromNamespaceAndPath(IronCoreCommon.MOD_ID, name)))
        .stacksTo(1)
        .durability(durability);
  }

  private static Item.Properties settings(String name) {
    String tooltipKey = "item." + IronCoreCommon.MOD_ID + "." + name + ".tooltip";
    return new Item.Properties()
        .setId(
            ResourceKey.create(
                Registries.ITEM, Identifier.fromNamespaceAndPath(IronCoreCommon.MOD_ID, name)))
        .component(
            DataComponents.LORE,
            new ItemLore(
                List.of(),
                List.of(
                    Component.translatable(tooltipKey).withStyle(ChatFormatting.GREEN),
                    Component.translatable(tooltipKey + ".size").withStyle(ChatFormatting.GREEN))));
  }
}
