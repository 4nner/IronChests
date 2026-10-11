package com.gathertocraft.ironcore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.level.storage.ValueInput;

/**
 * Keeps enchantments on items stored before 1.21.5.
 *
 * <p>1.21.5 reshaped components (enchantments lost their {@code levels} wrapper) but vanilla only
 * upgrades its own containers, so our chests load old items with the enchantments silently missing.
 * This runs vanilla's own migration ({@code TooltipDisplayComponentFix}) at load time.
 *
 * <p>Harmless on new data (passes through untouched); also fixes shulkers and bundles inside.
 *
 * <p>TODO(4.0): delete this class and the repair call in {@code GenericChestEntity} — the
 * conversion window closes.
 */
public final class ItemMigration {
  private ItemMigration() {}

  private static final String ITEMS_KEY = "Items";
  private static final String COMPONENTS_KEY = "components";
  private static final String SHOW_IN_TOOLTIP = "show_in_tooltip";
  private static final String HIDE_TOOLTIP = "minecraft:hide_tooltip";
  private static final String HIDE_ADDITIONAL_TOOLTIP = "minecraft:hide_additional_tooltip";
  private static final String TOOLTIP_DISPLAY = "minecraft:tooltip_display";
  private static final String CONTAINER = "minecraft:container";
  private static final String BUNDLE_CONTENTS = "minecraft:bundle_contents";
  private static final String NESTED_ITEM_KEY = "item";

  /** Nesting cap for the repair. Vanilla can't nest deeper than one level; this is paranoia. */
  private static final int MAX_DEPTH = 3;

  /** Components that only lost their {@code show_in_tooltip} flag. */
  private static final List<String> SIMPLE_COMPONENTS =
      List.of("minecraft:trim", "minecraft:unbreakable");

  /**
   * Components whose inner field moved to top level in 1.21.5. Enchantments only unwrap a compound,
   * so a hypothetical enchantment actually named "levels" is left alone.
   */
  private static final Map<String, String> UNWRAPPED_COMPONENTS =
      Map.of(
          "minecraft:dyed_color",
          "rgb",
          "minecraft:attribute_modifiers",
          "modifiers",
          "minecraft:enchantments",
          "levels",
          "minecraft:stored_enchantments",
          "levels",
          "minecraft:jukebox_playable",
          "song");

  private static final Set<String> STRICT_UNWRAP =
      Set.of("minecraft:enchantments", "minecraft:stored_enchantments");

  /** Vanilla's list of components with an "additional" tooltip, copied verbatim. */
  private static final List<String> CONVERTED_ADDITIONAL_TOOLTIP_TYPES =
      List.of(
          "minecraft:banner_patterns",
          "minecraft:bees",
          "minecraft:block_entity_data",
          "minecraft:block_state",
          "minecraft:bundle_contents",
          "minecraft:charged_projectiles",
          "minecraft:container",
          "minecraft:container_loot",
          "minecraft:firework_explosion",
          "minecraft:fireworks",
          "minecraft:instrument",
          "minecraft:map_id",
          "minecraft:painting/variant",
          "minecraft:pot_decorations",
          "minecraft:potion_contents",
          "minecraft:tropical_fish/pattern",
          "minecraft:written_book_content");

  /**
   * Repairs the raw {@code Items} entries and returns the ones that changed, decoded. Callers
   * overwrite just those slots after a normal load.
   */
  // lookup() is deprecated, but it's the only way to decode a tag we fixed ourselves — vanilla
  // still uses it internally for the same reason.
  @SuppressWarnings("deprecation")
  public static List<ItemStackWithSlot> repairedItems(ValueInput input, int capacity) {
    List<ItemStackWithSlot> repaired = new ArrayList<>();
    var ops = input.lookup().createSerializationContext(NbtOps.INSTANCE);
    for (CompoundTag raw : input.listOrEmpty(ITEMS_KEY, CompoundTag.CODEC)) {
      CompoundTag fixed = fixItemStackTag(raw);
      if (fixed == raw) {
        continue;
      }
      ItemStackWithSlot.CODEC
          .parse(ops, fixed)
          .result()
          .ifPresent(
              entry -> {
                if (entry.isValidInContainer(capacity)) {
                  repaired.add(entry);
                }
              });
    }
    return repaired;
  }

  /**
   * Returns {@code tag} untouched when neither it nor its nested items need repair, else a fixed
   * copy. The input is never modified.
   */
  public static CompoundTag fixItemStackTag(CompoundTag tag) {
    return fixItemStackTag(tag, 0);
  }

  private static CompoundTag fixItemStackTag(CompoundTag tag, int depth) {
    if (!needsRepairRecursive(tag, depth)) {
      return tag;
    }
    CompoundTag copy = tag.copy();
    CompoundTag components = copy.getCompoundOrEmpty(COMPONENTS_KEY);
    fixComponents(components);
    if (depth < MAX_DEPTH) {
      fixNestedItems(components, depth + 1);
    }
    return copy;
  }

  private static boolean needsRepairRecursive(CompoundTag tag, int depth) {
    CompoundTag components = tag.getCompound(COMPONENTS_KEY).orElse(null);
    if (components == null) {
      return false;
    }
    if (needsRepair(components)) {
      return true;
    }
    return depth < MAX_DEPTH && hasNestedRepair(components, depth + 1);
  }

  private static boolean hasNestedRepair(CompoundTag components, int depth) {
    ListTag container = components.getList(CONTAINER).orElse(null);
    if (container != null) {
      for (Tag entry : container) {
        if (entry instanceof CompoundTag slot
            && slot.getCompound(NESTED_ITEM_KEY)
                .map(item -> needsRepairRecursive(item, depth))
                .orElse(false)) {
          return true;
        }
      }
    }
    ListTag bundle = components.getList(BUNDLE_CONTENTS).orElse(null);
    if (bundle != null) {
      for (Tag element : bundle) {
        if (element instanceof CompoundTag item && needsRepairRecursive(item, depth)) {
          return true;
        }
      }
    }
    return false;
  }

  private static void fixNestedItems(CompoundTag components, int depth) {
    ListTag container = components.getList(CONTAINER).orElse(null);
    if (container != null) {
      for (int i = 0; i < container.size(); i++) {
        if (container.get(i) instanceof CompoundTag slot) {
          CompoundTag item = slot.getCompound(NESTED_ITEM_KEY).orElse(null);
          if (item != null) {
            CompoundTag fixed = fixItemStackTag(item, depth);
            if (fixed != item) {
              slot.put(NESTED_ITEM_KEY, fixed);
            }
          }
        }
      }
    }
    ListTag bundle = components.getList(BUNDLE_CONTENTS).orElse(null);
    if (bundle != null) {
      for (int i = 0; i < bundle.size(); i++) {
        if (bundle.get(i) instanceof CompoundTag item) {
          CompoundTag fixed = fixItemStackTag(item, depth);
          if (fixed != item) {
            bundle.set(i, fixed);
          }
        }
      }
    }
  }

  private static boolean needsRepair(CompoundTag components) {
    for (String id : SIMPLE_COMPONENTS) {
      if (hasShowInTooltip(components, id)) {
        return true;
      }
    }
    for (Map.Entry<String, String> entry : UNWRAPPED_COMPONENTS.entrySet()) {
      CompoundTag component = components.getCompound(entry.getKey()).orElse(null);
      if (component == null) {
        continue;
      }
      if (component.contains(SHOW_IN_TOOLTIP)) {
        return true;
      }
      Tag inner = component.get(entry.getValue());
      if (inner != null
          && (!STRICT_UNWRAP.contains(entry.getKey()) || inner instanceof CompoundTag)) {
        return true;
      }
    }
    return components.contains(HIDE_TOOLTIP) || components.contains(HIDE_ADDITIONAL_TOOLTIP);
  }

  private static boolean hasShowInTooltip(CompoundTag components, String id) {
    CompoundTag component = components.getCompound(id).orElse(null);
    return component != null && component.contains(SHOW_IN_TOOLTIP);
  }

  private static void fixComponents(CompoundTag components) {
    Set<String> hidden = new HashSet<>();
    for (String id : SIMPLE_COMPONENTS) {
      fixSimpleComponent(components, id, hidden);
    }
    for (Map.Entry<String, String> entry : UNWRAPPED_COMPONENTS.entrySet()) {
      fixUnwrappedComponent(components, entry.getKey(), entry.getValue(), hidden);
    }
    boolean hideTooltip = components.contains(HIDE_TOOLTIP);
    components.remove(HIDE_TOOLTIP);
    boolean hideAdditionalTooltip = components.contains(HIDE_ADDITIONAL_TOOLTIP);
    components.remove(HIDE_ADDITIONAL_TOOLTIP);
    if (hideAdditionalTooltip) {
      for (String id : CONVERTED_ADDITIONAL_TOOLTIP_TYPES) {
        if (components.contains(id)) {
          hidden.add(id);
        }
      }
    }
    if (!hidden.isEmpty() || hideTooltip) {
      CompoundTag display = new CompoundTag();
      display.putBoolean("hide_tooltip", hideTooltip);
      ListTag hiddenComponents = new ListTag();
      for (String id : hidden) {
        hiddenComponents.add(StringTag.valueOf(id));
      }
      display.put("hidden_components", hiddenComponents);
      components.put(TOOLTIP_DISPLAY, display);
    }
  }

  private static void fixSimpleComponent(CompoundTag components, String id, Set<String> hidden) {
    CompoundTag component = components.getCompound(id).orElse(null);
    if (component == null) {
      return;
    }
    if (!component.getBooleanOr(SHOW_IN_TOOLTIP, true)) {
      hidden.add(id);
    }
    component.remove(SHOW_IN_TOOLTIP);
  }

  private static void fixUnwrappedComponent(
      CompoundTag components, String id, String field, Set<String> hidden) {
    CompoundTag component = components.getCompound(id).orElse(null);
    if (component == null) {
      return;
    }
    if (!component.getBooleanOr(SHOW_IN_TOOLTIP, true)) {
      hidden.add(id);
    }
    component.remove(SHOW_IN_TOOLTIP);
    Tag inner = component.get(field);
    if (inner != null && (!STRICT_UNWRAP.contains(id) || inner instanceof CompoundTag)) {
      components.put(id, inner.copy());
    }
  }
}
