package com.gathertocraft.ironchest;

import com.gathertocraft.ironchest.legacy.LegacyUpgrades;
import com.gathertocraft.ironcore.lock.LockGuards;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.loader.api.FabricLoader;

public class IronChests implements ModInitializer {
  @Override
  public void onInitialize() {
    IronChestsCommon.init(FabricLoader.getInstance().getConfigDir());
    LegacyUpgrades.registerAll();
    PlayerBlockBreakEvents.BEFORE.register(
        (level, player, pos, state, blockEntity) -> {
          if (LockGuards.isLockedFor(level, pos, player)) {
            if (!level.isClientSide()) {
              LockGuards.denyLocked(level, pos, player);
            }
            return false;
          }
          return true;
        });
  }
}
