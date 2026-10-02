package com.gathertocraft.ironchest;

import com.gathertocraft.ironchest.registry.ModBlockEntityType;
import com.gathertocraft.ironchest.registry.ModBlocks;
import com.gathertocraft.ironchest.registry.ModItemGroup;
import com.gathertocraft.ironchest.registry.ModItems;
import com.gathertocraft.ironchest.registry.ModScreenHandlerType;
import com.gathertocraft.ironchest.registry.ModUpgradeBindings;
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
    ModUpgradeBindings.bindVanilla();
    ModItems.registerItems();
    ModItemGroup.registerItemGroup();
    ModItems.addTabItems(TAB);
    ModBlockEntityType.registerBlockEntities();
    ModScreenHandlerType.registerScreenHandlers();
  }
}
