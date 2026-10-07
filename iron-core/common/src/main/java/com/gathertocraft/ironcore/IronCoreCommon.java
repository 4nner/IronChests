package com.gathertocraft.ironcore;

import com.gathertocraft.ironcore.lock.KeyEditorMenus;
import com.gathertocraft.ironcore.registry.ModCoreItemGroup;
import com.gathertocraft.ironcore.registry.ModCoreItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public final class IronCoreCommon {
  private IronCoreCommon() {}

  public static final String MOD_ID = "ironcore";
  public static final ResourceKey<CreativeModeTab> TAB =
      ResourceKey.create(
          Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(MOD_ID, "general"));

  public static void init() {
    ModCoreItems.registerItems();
    ModCoreItemGroup.registerItemGroup();
    ModCoreItems.addTabItems(TAB);
    KeyEditorMenus.registerScreenHandlers();
  }
}
