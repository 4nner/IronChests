package com.gathertocraft.ironchest.support;

import com.gathertocraft.ironchest.blocks.ChestTypes;
import com.gathertocraft.ironchest.blocks.GenericChestBlock;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Decides whether copper golems may deposit items into IronChests containers.
 *
 * <p>Vanilla golems only deposit into {@code minecraft:chest} and {@code minecraft:trapped_chest}.
 * Loader mixins consult this helper so every tier behaves as a deposit target. Golems never take
 * from these chests: an empty-handed golem reports no match. The Dirt chest only accepts golems
 * carrying dirt: its dirt-only rule is enforced in {@code canPlaceItem}, which the golem deposit
 * path bypasses, so the held item is checked here before the golem commits.
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
    if (!enabled || !(state.getBlock() instanceof GenericChestBlock chest)) {
      return false;
    }
    if (mob.getMainHandItem().isEmpty()) {
      return false;
    }
    return !isDirtChest(chest) || mob.getMainHandItem().is(ItemTags.DIRT);
  }

  private static boolean isDirtChest(GenericChestBlock chest) {
    return chest.getType() == ChestTypes.DIRT;
  }
}
