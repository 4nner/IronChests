package com.gathertocraft.ironcore.handtruck;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;

/**
 * Server-side per-tick upkeep for loaded hand trucks. A loaded truck anywhere in the player's
 * possession (inventory, offhand, or cursor stack) inflicts Slowness II, refreshed every tick so it
 * ends the moment the truck leaves their hands. Loaded trucks found in foreign containers (block
 * inventories open in a menu) are moved back into the player inventory.
 */
public final class HandTruckEffects {
  private HandTruckEffects() {}

  private static final int SLOW_DURATION_TICKS = 30;
  private static final int SLOWNESS_II = 1;

  /** Applies Slowness II when the player carries a loaded hand truck. Call every server tick. */
  public static void tickPlayer(ServerPlayer player) {
    if (carriesLoadedTruck(player)) {
      player.addEffect(
          new MobEffectInstance(
              MobEffects.SLOWNESS, SLOW_DURATION_TICKS, SLOWNESS_II, false, false, true));
    }
    ejectFromForeignContainers(player);
  }

  /** True when any inventory slot or the cursor stack holds a loaded hand truck. */
  static boolean carriesLoadedTruck(ServerPlayer player) {
    Inventory inventory = player.getInventory();
    for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
      if (HandTruckSupport.isLoaded(inventory.getItem(slot))) {
        return true;
      }
    }
    return HandTruckSupport.isLoaded(player.containerMenu.getCarried());
  }

  /**
   * Moves loaded hand trucks out of foreign containers (block inventories open in the player's
   * menu) back into the player inventory, dropping at their feet when full. The player inventory,
   * the crafting grid, and the cursor stack are theirs to keep.
   */
  static void ejectFromForeignContainers(ServerPlayer player) {
    boolean ejected = false;
    for (Slot slot : player.containerMenu.slots) {
      Container container = slot.container;
      if (container == null
          || container == player.getInventory()
          || container instanceof TransientCraftingContainer) {
        continue;
      }
      ItemStack stack = slot.getItem();
      if (!HandTruckSupport.isLoaded(stack)) {
        continue;
      }
      slot.set(ItemStack.EMPTY);
      if (!player.getInventory().add(stack)) {
        Containers.dropItemStack(
            player.level(), player.getX(), player.getY(), player.getZ(), stack);
      }
      ejected = true;
    }
    if (ejected) {
      player.sendOverlayMessage(
          Component.translatable("message.ironcore.hand_truck.not_in_containers"));
    }
  }
}
