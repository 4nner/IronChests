package com.gathertocraft.ironcore.registry;

import com.gathertocraft.ironcore.IronCoreCommon;
import com.gathertocraft.ironcore.platform.Platforms;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ModCoreItemGroup {
  private ModCoreItemGroup() {}

  public static void registerItemGroup() {
    Platforms.registry()
        .register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            IronCoreCommon.TAB.identifier(),
            () ->
                Platforms.registry()
                    .creativeTab(
                        () -> new ItemStack(ModCoreItems.get("iron_upgrade")),
                        Component.translatable("itemGroup.ironcore.general")));
  }
}
