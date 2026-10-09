package com.gathertocraft.ironcore.handtruck;

import static com.gathertocraft.ironcore.handtruck.HandTruckSupport.Deny.ALLOW;
import static com.gathertocraft.ironcore.handtruck.HandTruckSupport.Deny.DOUBLE_CHEST;
import static com.gathertocraft.ironcore.handtruck.HandTruckSupport.Deny.IN_USE;
import static com.gathertocraft.ironcore.handtruck.HandTruckSupport.Deny.LOCKED;
import static com.gathertocraft.ironcore.handtruck.HandTruckSupport.Deny.NESTED;
import static com.gathertocraft.ironcore.handtruck.HandTruckSupport.Deny.NOT_MOVABLE;
import static com.gathertocraft.ironcore.handtruck.HandTruckSupport.Deny.SPAWNER_DISABLED;
import static com.gathertocraft.ironcore.handtruck.HandTruckSupport.Deny.SPAWNER_WRONG_TRUCK;
import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.Test;

class HandTruckSupportTest {
  // (isSpawner, isContainer, isDoubleChest, lockedForPlayer, inUse, containsLoadedTruck,
  // isEnhancedTruck, spawnersAllowed)
  private static HandTruckSupport.Deny check(
      boolean spawner,
      boolean container,
      boolean doubled,
      boolean locked,
      boolean inUse,
      boolean nested,
      boolean enhanced,
      boolean allowed) {
    return HandTruckSupport.check(
        spawner, container, doubled, locked, inUse, nested, enhanced, allowed);
  }

  @Test
  void plainContainerAllowed() {
    assertEquals(ALLOW, check(false, true, false, false, false, false, false, false));
  }

  @Test
  void nonContainerDenied() {
    assertEquals(NOT_MOVABLE, check(false, false, false, false, false, false, true, true));
  }

  @Test
  void doubleChestDenied() {
    assertEquals(DOUBLE_CHEST, check(false, true, true, false, false, false, false, false));
  }

  @Test
  void lockedBeatsInUseAndNested() {
    assertEquals(LOCKED, check(false, true, false, true, true, true, false, false));
  }

  @Test
  void inUseDenied() {
    assertEquals(IN_USE, check(false, true, false, false, true, false, false, false));
  }

  @Test
  void nestedTruckDenied() {
    assertEquals(NESTED, check(false, true, false, false, false, true, true, true));
  }

  @Test
  void spawnerNeedsEnhancedTruck() {
    assertEquals(SPAWNER_WRONG_TRUCK, check(true, false, false, false, false, false, false, true));
  }

  @Test
  void spawnerNeedsConfigFlag() {
    assertEquals(SPAWNER_DISABLED, check(true, false, false, false, false, false, true, false));
  }

  @Test
  void spawnerAllowed() {
    assertEquals(ALLOW, check(true, false, false, false, false, false, true, true));
  }

  @Test
  void containerMoveCostsOne() {
    assertEquals(
        1,
        HandTruckSupport.moveCost(
            new CarriedBlock(
                Identifier.withDefaultNamespace("chest"),
                new CompoundTag(),
                new CompoundTag(),
                false)));
  }

  @Test
  void spawnerMoveCostsTen() {
    assertEquals(
        10,
        HandTruckSupport.moveCost(
            new CarriedBlock(
                Identifier.withDefaultNamespace("chest"),
                new CompoundTag(),
                new CompoundTag(),
                true)));
  }
}
