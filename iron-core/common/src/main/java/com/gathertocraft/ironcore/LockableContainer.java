package com.gathertocraft.ironcore;

import net.minecraft.world.entity.player.Player;

/**
 * Implemented by containers that can be locked to an owner plus a trusted allow-list.
 *
 * <p>{@link UpgradeItem} can deny upgrades on locked containers
 */
public interface LockableContainer {
  /** True when an owner has locked this container. Unlocked containers are open to everyone. */
  boolean isLocked();

  /**
   * True when the player may open, break, or upgrade this container: owner, trusted, OP bypass, or
   * unlocked.
   */
  boolean isAuthorized(Player player);
}
