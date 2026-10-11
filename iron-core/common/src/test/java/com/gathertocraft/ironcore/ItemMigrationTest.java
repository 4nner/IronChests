package com.gathertocraft.ironcore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import org.junit.jupiter.api.Test;

/** TODO(4.0): delete with {@link ItemMigration} — the conversion window closes. */
class ItemMigrationTest {
  private static CompoundTag levels(String id, int level) {
    CompoundTag levels = new CompoundTag();
    levels.putInt(id, level);
    return levels;
  }

  private static CompoundTag flag(boolean showInTooltip) {
    CompoundTag component = new CompoundTag();
    component.putBoolean("show_in_tooltip", showInTooltip);
    return component;
  }

  private static CompoundTag wrapped(String field, CompoundTag inner, boolean showInTooltip) {
    CompoundTag component = flag(showInTooltip);
    component.put(field, inner);
    return component;
  }

  private static CompoundTag holding(String componentId, CompoundTag component) {
    CompoundTag components = new CompoundTag();
    components.put(componentId, component);
    return components;
  }

  private static CompoundTag stack(String id, CompoundTag components) {
    CompoundTag stack = new CompoundTag();
    stack.putString("id", id);
    stack.putInt("count", 1);
    stack.put("components", components);
    return stack;
  }

  private static CompoundTag component(CompoundTag stack, String id) {
    return stack.getCompoundOrEmpty("components").getCompoundOrEmpty(id);
  }

  private static CompoundTag oldBook() {
    return stack(
        "minecraft:enchanted_book",
        holding(
            "minecraft:stored_enchantments",
            wrapped("levels", levels("minecraft:sharpness", 3), true)));
  }

  private static CompoundTag oldTool() {
    return stack(
        "minecraft:diamond_chestplate",
        holding(
            "minecraft:enchantments", wrapped("levels", levels("minecraft:protection", 2), true)));
  }

  private static CompoundTag shulkerWith(CompoundTag inner) {
    CompoundTag slot = new CompoundTag();
    slot.putByte("slot", (byte) 0);
    slot.put("item", inner);
    ListTag container = new ListTag();
    container.add(slot);
    CompoundTag components = new CompoundTag();
    components.put("minecraft:container", container);
    return stack("minecraft:shulker_box", components);
  }

  private static CompoundTag bundleWith(CompoundTag inner) {
    ListTag items = new ListTag();
    items.add(inner);
    CompoundTag components = new CompoundTag();
    components.put("minecraft:bundle_contents", items);
    return stack("minecraft:bundle", components);
  }

  private static CompoundTag shulkerItem(CompoundTag shulker) {
    return shulker
        .getCompoundOrEmpty("components")
        .getListOrEmpty("minecraft:container")
        .getCompoundOrEmpty(0)
        .getCompoundOrEmpty("item");
  }

  private static CompoundTag bundledItem(CompoundTag bundle) {
    return bundle
        .getCompoundOrEmpty("components")
        .getListOrEmpty("minecraft:bundle_contents")
        .getCompoundOrEmpty(0);
  }

  @Test
  void unwrapsLevelsWrapper() {
    CompoundTag before = oldBook();

    CompoundTag fixed = ItemMigration.fixItemStackTag(before);

    assertEquals(
        levels("minecraft:sharpness", 3), component(fixed, "minecraft:stored_enchantments"));
    assertTrue(component(before, "minecraft:stored_enchantments").contains("levels"));
  }

  @Test
  void hiddenTooltipBecomesTooltipDisplay() {
    CompoundTag fixed =
        ItemMigration.fixItemStackTag(
            stack(
                "minecraft:enchanted_book",
                holding(
                    "minecraft:stored_enchantments",
                    wrapped("levels", levels("minecraft:sharpness", 3), false))));

    CompoundTag display = component(fixed, "minecraft:tooltip_display");
    assertEquals(false, display.getBooleanOr("hide_tooltip", true));
    ListTag hidden = display.getListOrEmpty("hidden_components");
    assertEquals(1, hidden.size());
    assertEquals(StringTag.valueOf("minecraft:stored_enchantments"), hidden.get(0));
  }

  @Test
  void unwrapsActiveEnchantments() {
    CompoundTag fixed = ItemMigration.fixItemStackTag(oldTool());

    assertEquals(levels("minecraft:protection", 2), component(fixed, "minecraft:enchantments"));
  }

  @Test
  void newFormatPassesThroughUntouched() {
    CompoundTag book =
        stack(
            "minecraft:enchanted_book",
            holding("minecraft:stored_enchantments", levels("minecraft:sharpness", 3)));

    assertSame(book, ItemMigration.fixItemStackTag(book));
  }

  @Test
  void stackWithoutComponentsPassesThroughUntouched() {
    CompoundTag stick = new CompoundTag();
    stick.putString("id", "minecraft:stick");
    stick.putInt("count", 1);

    assertSame(stick, ItemMigration.fixItemStackTag(stick));
  }

  @Test
  void hideTooltipBecomesTooltipDisplay() {
    CompoundTag components = new CompoundTag();
    components.putBoolean("minecraft:hide_tooltip", true);
    CompoundTag fixed =
        ItemMigration.fixItemStackTag(stack("minecraft:enchanted_book", components));

    CompoundTag after = fixed.getCompoundOrEmpty("components");
    assertFalse(after.contains("minecraft:hide_tooltip"));
    assertTrue(
        after.getCompoundOrEmpty("minecraft:tooltip_display").getBooleanOr("hide_tooltip", false));
  }

  @Test
  void unbreakableDropsTooltipFlag() {
    CompoundTag fixed =
        ItemMigration.fixItemStackTag(
            stack("minecraft:iron_chestplate", holding("minecraft:unbreakable", flag(false))));

    CompoundTag after = fixed.getCompoundOrEmpty("components");
    assertEquals(new CompoundTag(), after.getCompoundOrEmpty("minecraft:unbreakable"));
    ListTag hidden =
        after.getCompoundOrEmpty("minecraft:tooltip_display").getListOrEmpty("hidden_components");
    assertEquals(1, hidden.size());
    assertEquals(StringTag.valueOf("minecraft:unbreakable"), hidden.get(0));
  }

  @Test
  void repairsBookInsideShulkerBox() {
    CompoundTag before = shulkerWith(oldBook());

    CompoundTag fixedBook = shulkerItem(ItemMigration.fixItemStackTag(before));

    assertEquals(
        levels("minecraft:sharpness", 3), component(fixedBook, "minecraft:stored_enchantments"));
    assertTrue(component(shulkerItem(before), "minecraft:stored_enchantments").contains("levels"));
  }

  @Test
  void repairsToolInsideBundle() {
    CompoundTag fixedTool = bundledItem(ItemMigration.fixItemStackTag(bundleWith(oldTool())));

    assertEquals(levels("minecraft:protection", 2), component(fixedTool, "minecraft:enchantments"));
  }

  @Test
  void newFormatNestedPassesThroughUntouched() {
    CompoundTag shulker =
        shulkerWith(
            stack(
                "minecraft:enchanted_book",
                holding("minecraft:stored_enchantments", levels("minecraft:sharpness", 3))));

    assertSame(shulker, ItemMigration.fixItemStackTag(shulker));
  }

  @Test
  void deepNestingTerminatesAtDepthCap() {
    CompoundTag inner = oldBook();
    for (int i = 0; i < 10; i++) {
      inner = shulkerWith(inner);
    }

    // Terminates; the book is 10 deep, past the cap, so its wrapper survives.
    CompoundTag level = ItemMigration.fixItemStackTag(inner);
    for (int i = 0; i < 10; i++) {
      level = shulkerItem(level);
    }
    assertTrue(component(level, "minecraft:stored_enchantments").contains("levels"));
  }

  @Test
  void repairsBookAtDepthCapBoundary() {
    // Three levels down is still within reach.
    CompoundTag inner = oldBook();
    for (int i = 0; i < 3; i++) {
      inner = shulkerWith(inner);
    }

    CompoundTag level = ItemMigration.fixItemStackTag(inner);
    for (int i = 0; i < 3; i++) {
      level = shulkerItem(level);
    }
    assertEquals(
        levels("minecraft:sharpness", 3), component(level, "minecraft:stored_enchantments"));
  }
}
