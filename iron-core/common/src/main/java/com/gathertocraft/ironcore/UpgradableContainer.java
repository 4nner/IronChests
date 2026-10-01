package com.gathertocraft.ironcore;

import net.minecraft.world.entity.player.Player;

/**
 * A container block entity that upgrade items can transform in place.
 *
 * <p>Content mods implement this on their containers (chests, barrels, …); core's {@link
 * UpgradeItem} works against the interface, never against a specific block entity type.
 */
public interface UpgradableContainer {
  /** The container's current tier. Strategies match on it to decide what an upgrade applies to. */
  TierSpec tier();

  /** True when nobody is using the container and it may be transformed right now. */
  boolean isAvailableForUpgrade(Player player);
}
