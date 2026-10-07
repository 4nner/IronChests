package com.gathertocraft.ironcore;

import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.entity.player.Player;

/** Operator bypass shared by every lock check. */
public final class LockPermissions {
  private LockPermissions() {}

  /** OP level 2+ bypasses locks entirely (open, break, upgrade, configure). */
  public static boolean isOperator(Player player) {
    if (player == null) {
      return false;
    }
    try {
      if (player.permissions() instanceof LevelBasedPermissionSet levelBased) {
        return levelBased.level().id() >= PermissionLevel.GAMEMASTERS.id();
      }
    } catch (LinkageError e) {
      // Very old runtime without the permissions API; fall through to no bypass.
    }
    return false;
  }
}
