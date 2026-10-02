package com.gathertocraft.ironchest.legacy;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Placeholder for a pre-3.0 {@code ironchest:*_upgrade} id.
 *
 * <p>Carries no upgrade logic: the first server-side inventory tick swaps the stack for its {@code
 * ironcore:} successor. Stacks stored in block containers convert on pickup, since those never
 * tick.
 *
 * <p>TODO(4.0): remove this class and {@link LegacyUpgrades} — the conversion window closes.
 */
public final class LegacyUpgradeItem extends Item {
  private final String targetId;

  public LegacyUpgradeItem(String targetId, Properties properties) {
    super(properties);
    this.targetId = targetId;
  }

  @Override
  public Component getName(ItemStack stack) {
    return Component.translatable("item.ironchest.legacy_upgrade");
  }

  @Override
  public void inventoryTick(
      ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
    if (!(entity instanceof Player player)) {
      return;
    }
    Item target =
        BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath("ironcore", this.targetId));
    if (target == null || target == this) {
      return;
    }
    ItemStack replacement = new ItemStack(target, stack.getCount());
    Inventory inventory = player.getInventory();
    for (int i = 0; i < inventory.getContainerSize(); i++) {
      if (inventory.getItem(i) == stack) {
        inventory.setItem(i, replacement);
        return;
      }
    }
  }
}
