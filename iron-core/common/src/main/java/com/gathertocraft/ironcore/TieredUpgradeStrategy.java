package com.gathertocraft.ironcore;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

/** Upgrades any bound container to one fixed {@link MaterialTier}, from its declared sources. */
public class TieredUpgradeStrategy implements UpgradeStrategy {
  private final MaterialTier target;

  public TieredUpgradeStrategy(MaterialTier target) {
    this.target = target;
  }

  @Override
  public boolean canUpgrade(BlockState state) {
    MaterialTier source = UpgradeBindings.tierOf(state.getBlock());
    return source != null
        && this.target.sources().contains(source)
        && UpgradeBindings.resultBlock(this.target) != null;
  }

  @Override
  public BlockState resultState(BlockState oldState) {
    Block result = UpgradeBindings.resultBlock(this.target);
    if (result == null) {
      return oldState;
    }
    BlockState newState = result.defaultBlockState();
    for (Property<?> oldProperty : oldState.getProperties()) {
      newState = copyIfPresent(oldState, oldProperty, newState);
    }
    return newState;
  }

  private static <T extends Comparable<T>> BlockState copyIfPresent(
      BlockState oldState, Property<T> oldProperty, BlockState newState) {
    Property<?> raw = newState.getBlock().getStateDefinition().getProperty(oldProperty.getName());
    if (raw != null && raw.getValueClass().equals(oldProperty.getValueClass())) {
      @SuppressWarnings("unchecked")
      Property<T> newProperty = (Property<T>) raw;
      T value = oldState.getValue(oldProperty);
      if (newProperty.getPossibleValues().contains(value)) {
        return newState.setValue(newProperty, value);
      }
    }
    return newState;
  }
}
