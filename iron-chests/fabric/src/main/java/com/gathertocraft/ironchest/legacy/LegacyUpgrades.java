package com.gathertocraft.ironchest.legacy;

import com.gathertocraft.ironchest.IronChestsCommon;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

/**
 * Fabric-only re-registration of the 27 pre-3.0 upgrade ids as self-converting placeholders.
 *
 * <p>Every id maps to its target-tier successor. {@code wood_christmas} maps to {@code
 * copper_upgrade}.
 *
 * <p>TODO(4.0): delete this package — the conversion window closes.
 */
public final class LegacyUpgrades {
  private LegacyUpgrades() {}

  private static final Map<String, String> TARGETS =
      Map.ofEntries(
          // Exact single-step successors.
          Map.entry("wood_copper_upgrade", "copper_upgrade"),
          Map.entry("copper_iron_upgrade", "iron_upgrade"),
          Map.entry("iron_gold_upgrade", "gold_upgrade"),
          Map.entry("gold_diamond_upgrade", "diamond_upgrade"),
          Map.entry("gold_emerald_upgrade", "emerald_upgrade"),
          Map.entry("diamond_crystal_upgrade", "crystal_upgrade"),
          Map.entry("emerald_crystal_upgrade", "crystal_upgrade"),
          Map.entry("diamond_obsidian_upgrade", "obsidian_upgrade"),
          Map.entry("emerald_obsidian_upgrade", "obsidian_upgrade"),
          // Skip-upgrades: target-tier successor (value preserved).
          Map.entry("wood_christmas_upgrade", "copper_upgrade"),
          Map.entry("wood_iron_upgrade", "iron_upgrade"),
          Map.entry("wood_gold_upgrade", "gold_upgrade"),
          Map.entry("wood_diamond_upgrade", "diamond_upgrade"),
          Map.entry("wood_emerald_upgrade", "emerald_upgrade"),
          Map.entry("wood_crystal_upgrade", "crystal_upgrade"),
          Map.entry("wood_obsidian_upgrade", "obsidian_upgrade"),
          Map.entry("copper_gold_upgrade", "gold_upgrade"),
          Map.entry("copper_diamond_upgrade", "diamond_upgrade"),
          Map.entry("copper_emerald_upgrade", "emerald_upgrade"),
          Map.entry("copper_crystal_upgrade", "crystal_upgrade"),
          Map.entry("copper_obsidian_upgrade", "obsidian_upgrade"),
          Map.entry("iron_diamond_upgrade", "diamond_upgrade"),
          Map.entry("iron_emerald_upgrade", "emerald_upgrade"),
          Map.entry("iron_crystal_upgrade", "crystal_upgrade"),
          Map.entry("iron_obsidian_upgrade", "obsidian_upgrade"),
          Map.entry("gold_crystal_upgrade", "crystal_upgrade"),
          Map.entry("gold_obsidian_upgrade", "obsidian_upgrade"));

  public static void registerAll() {
    for (Map.Entry<String, String> entry : TARGETS.entrySet()) {
      String id = entry.getKey();
      Identifier identifier = Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, id);
      LegacyUpgradeItem item =
          new LegacyUpgradeItem(
              entry.getValue(),
              new Item.Properties().setId(ResourceKey.create(Registries.ITEM, identifier)));
      Registry.register(BuiltInRegistries.ITEM, identifier, item);
    }
  }
}
