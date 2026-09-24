package anner.ironchest;

import anner.ironchest.registry.ModBlockEntityType;
import anner.ironchest.registry.ModBlocks;
import anner.ironchest.registry.ModItemGroup;
import anner.ironchest.registry.ModItems;
import anner.ironchest.registry.ModScreenHandlerType;

public final class IronChestsCommon {
  private IronChestsCommon() {}

  public static void init() {
    ModBlocks.registerBlocks();
    ModItems.registerItems();
    ModItemGroup.registerItemGroup();
    ModItems.addTabItems(IronChests.TAB);
    ModBlockEntityType.registerBlockEntities();
    ModScreenHandlerType.registerScreenHandlers();
  }
}
