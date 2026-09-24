package anner.ironchest;

import anner.ironchest.registry.ModBlockEntityType;
import anner.ironchest.registry.ModBlocks;
import anner.ironchest.registry.ModItemGroup;
import anner.ironchest.registry.ModItems;
import anner.ironchest.registry.ModScreenHandlerType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public final class IronChestsCommon {
  private IronChestsCommon() {}

  public static final String MOD_ID = "ironchest";
  public static final ResourceKey<CreativeModeTab> TAB =
      ResourceKey.create(
          Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(MOD_ID, "general"));

  public static void init() {
    ModBlocks.registerBlocks();
    ModItems.registerItems();
    ModItemGroup.registerItemGroup();
    ModItems.addTabItems(TAB);
    ModBlockEntityType.registerBlockEntities();
    ModScreenHandlerType.registerScreenHandlers();
  }
}
