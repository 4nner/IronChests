package anner.ironchest.screenhandlers;

import anner.ironchest.blocks.TierSpec;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

// Chest-specific alias keeping registry, client, and mixin wiring stable.
// All sizing and slot logic lives in SizedContainerMenu.
public final class ChestScreenHandler extends SizedContainerMenu {
  public ChestScreenHandler(
      MenuType<?> menuType,
      TierSpec tier,
      int syncId,
      Inventory playerInventory,
      Container container) {
    super(menuType, tier, syncId, playerInventory, container);
  }
}
