package com.gathertocraft.ironchest.registry;

import com.gathertocraft.ironchest.blocks.ChestTypes;
import com.gathertocraft.ironcore.MaterialTier;
import com.gathertocraft.ironcore.UpgradeBindings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * Plugs chests into core's shared upgrade ladder.
 *
 * <p>Vanilla wood binds eagerly at init (vanilla blocks always exist). Modded blocks bind in the
 * block factory: the instance exists there on both loaders (Fabric resolves eagerly, NeoForge at
 * registry-fill time), while result bindings stay lazy suppliers so init never dereferences a
 * registry. Christmas stays outside the ladder — it is craft-only.
 */
public final class ModUpgradeBindings {
  private ModUpgradeBindings() {}

  public static void bindVanilla() {
    UpgradeBindings.bindTier(Blocks.CHEST, MaterialTier.WOOD);
    UpgradeBindings.bindTier(Blocks.TRAPPED_CHEST, MaterialTier.WOOD);
  }

  public static Block bindChest(ChestTypes type, Block block) {
    if (type == ChestTypes.CHRISTMAS) {
      return block;
    }
    MaterialTier tier = MaterialTier.valueOf(type.name());
    UpgradeBindings.bindTier(block, tier);
    UpgradeBindings.bindResult(tier, type::getBlock);
    return block;
  }
}
