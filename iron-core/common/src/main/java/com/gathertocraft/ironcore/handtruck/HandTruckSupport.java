package com.gathertocraft.ironcore.handtruck;

import com.gathertocraft.ironcore.UpgradableContainer;
import com.gathertocraft.ironcore.lock.LockGuards;
import com.gathertocraft.ironcore.registry.ModDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

/**
 * Decides what a hand truck may pick up. The {@link #check} core is pure booleans so it unit-tests
 * without a level; the adapters below read game state into those booleans.
 */
public final class HandTruckSupport {
  private HandTruckSupport() {}

  /** Outcome of the movability check. {@link #ALLOW} is the only success. */
  public enum Deny {
    ALLOW,
    NOT_MOVABLE,
    DOUBLE_CHEST,
    LOCKED,
    IN_USE,
    NESTED,
    SPAWNER_WRONG_TRUCK,
    SPAWNER_DISABLED
  }

  /**
   * Pure movability decision. Spawners skip the container checks (no inventory, no locks);
   * everything else must be a single, unlocked, unused container holding no loaded truck.
   */
  public static Deny check(
      boolean isSpawner,
      boolean isContainer,
      boolean isDoubleChest,
      boolean lockedForPlayer,
      boolean inUse,
      boolean containsLoadedTruck,
      boolean isEnhancedTruck,
      boolean spawnersAllowed) {
    if (isSpawner) {
      if (!isEnhancedTruck) {
        return Deny.SPAWNER_WRONG_TRUCK;
      }
      if (!spawnersAllowed) {
        return Deny.SPAWNER_DISABLED;
      }
      return Deny.ALLOW;
    }
    if (!isContainer) {
      return Deny.NOT_MOVABLE;
    }
    if (isDoubleChest) {
      return Deny.DOUBLE_CHEST;
    }
    if (lockedForPlayer) {
      return Deny.LOCKED;
    }
    if (inUse) {
      return Deny.IN_USE;
    }
    if (containsLoadedTruck) {
      return Deny.NESTED;
    }
    return Deny.ALLOW;
  }

  /** Durability cost of completing a move: containers 1, spawner moves 10. */
  public static int moveCost(CarriedBlock carried) {
    return carried.spawnerMove() ? 10 : 1;
  }

  /** True for vanilla spawners and trial spawners, the only spawner kinds trucks handle. */
  public static boolean isSpawner(BlockEntity entity) {
    return entity instanceof SpawnerBlockEntity || entity instanceof TrialSpawnerBlockEntity;
  }

  /** True when the state is a vanilla chest merged into a double chest. */
  public static boolean isDoubleChest(BlockState state) {
    if (state.getBlock() instanceof ChestBlock) {
      return state.getValue(ChestBlock.TYPE) != ChestType.SINGLE;
    }
    return false;
  }

  /** True when a player is using the container right now. */
  public static boolean isInUse(BlockEntity entity, Player player) {
    if (entity instanceof UpgradableContainer upgradable) {
      return !upgradable.isAvailableForUpgrade(player);
    }
    if (entity instanceof ChestBlockEntity chest
        && chest.getLevel() != null
        && ChestBlockEntity.getOpenCount(chest.getLevel(), chest.getBlockPos()) > 0) {
      return true;
    }
    return false;
  }

  /** True when the container at pos is locked and the player is outside its access list. */
  public static boolean isLockedFor(BlockGetter level, BlockPos pos, Player player) {
    return LockGuards.isLockedFor(level, pos, player);
  }

  /** True when the stack is a hand truck currently carrying a block. */
  public static boolean isLoaded(ItemStack stack) {
    return stack.has(ModDataComponents.carriedBlock());
  }

  /** True when any slot of the container holds a loaded hand truck. */
  public static boolean containsLoadedTruck(Container container) {
    for (int slot = 0; slot < container.getContainerSize(); slot++) {
      if (isLoaded(container.getItem(slot))) {
        return true;
      }
    }
    return false;
  }
}
