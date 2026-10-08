package com.gathertocraft.ironchest.support;

import com.gathertocraft.ironchest.blocks.ChestTypes;
import com.gathertocraft.ironchest.blocks.GenericChestBlock;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Decides whether copper golems may deposit items into IronChests containers.
 *
 * <p>Vanilla golems only deposit into {@code minecraft:chest} and {@code minecraft:trapped_chest}.
 * Loader mixins consult this helper so every tier behaves as a deposit target. Golems never take
 * from these chests: an empty-handed golem reports no match. The Dirt chest is excluded: its
 * dirt-only rule is enforced in {@code canPlaceItem}, which the golem deposit path bypasses.
 */
public final class CopperGolemSupport {
  private CopperGolemSupport() {}

  private static volatile boolean enabled = true;

  public static void setEnabled(boolean allow) {
    enabled = allow;
  }

  public static boolean isEnabled() {
    return enabled;
  }

  /**
   * Mixin entry point: true when a golem carrying items should treat {@code state} as a valid
   * deposit target.
   */
  public static boolean shouldInteract(PathfinderMob mob, BlockState state) {
    if (!enabled || !isEligibleChest(state)) {
      return false;
    }
    return !mob.getMainHandItem().isEmpty();
  }

  private static boolean isEligibleChest(BlockState state) {
    return state.getBlock() instanceof GenericChestBlock chest
        && chest.getType() != ChestTypes.DIRT;
  }
}
