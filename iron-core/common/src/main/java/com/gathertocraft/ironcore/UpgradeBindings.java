package com.gathertocraft.ironcore;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;

/**
 * Lets content mods plug their containers into the shared upgrade ladder.
 *
 * <p>A mod binds each of its container blocks to a {@link MaterialTier} and binds each tier it
 * supports to the block an upgrade produces. Core strategies only ever read these maps, so new
 * container mods need no core changes.
 */
public final class UpgradeBindings {
  private UpgradeBindings() {}

  private static final Map<Block, MaterialTier> BLOCK_TIERS = new HashMap<>();
  private static final Map<MaterialTier, Supplier<Block>> RESULT_BLOCKS =
      new EnumMap<>(MaterialTier.class);

  public static void bindTier(Block block, MaterialTier tier) {
    BLOCK_TIERS.put(block, tier);
  }

  public static void bindResult(MaterialTier tier, Supplier<Block> block) {
    RESULT_BLOCKS.put(tier, block);
  }

  public static MaterialTier tierOf(Block block) {
    return BLOCK_TIERS.get(block);
  }

  public static Block resultBlock(MaterialTier tier) {
    Supplier<Block> supplier = RESULT_BLOCKS.get(tier);
    return supplier != null ? supplier.get() : null;
  }
}
