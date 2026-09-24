package anner.ironchest.registry;

import anner.ironchest.IronChestsCommon;
import anner.ironchest.blocks.ChestTypes;
import anner.ironchest.platform.Platforms;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ModItemGroup {
  private ModItemGroup() {}

  public static void registerItemGroup() {
    Platforms.registry()
        .register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            IronChestsCommon.TAB.identifier(),
            () ->
                Platforms.registry()
                    .creativeTab(
                        () -> new ItemStack(ChestTypes.IRON.getBlock()),
                        Component.translatable("itemGroup.ironchest.general")));
  }
}
