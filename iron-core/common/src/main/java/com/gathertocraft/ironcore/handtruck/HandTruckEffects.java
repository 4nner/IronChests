package com.gathertocraft.ironcore.handtruck;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;

/**
 * Server-side per-tick upkeep for loaded hand trucks. A loaded truck anywhere in the player's
 * possession (inventory, offhand, or cursor stack) inflicts Slowness II, refreshed every tick so it
 * ends the moment the truck leaves their hands.
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
}
