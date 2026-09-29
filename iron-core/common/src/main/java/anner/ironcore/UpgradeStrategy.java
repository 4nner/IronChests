package anner.ironcore;

import net.minecraft.world.level.block.state.BlockState;

public interface UpgradeStrategy {
  boolean canUpgrade(BlockState state);

  BlockState resultState(BlockState oldState);
}
