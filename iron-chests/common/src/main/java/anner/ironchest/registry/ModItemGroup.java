package anner.ironchest.registry;

import anner.ironchest.IronChestsCommon;
import anner.ironchest.platform.Platforms;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroup {
  public static void registerItemGroup() {
    CreativeModeTab tab =
        Platforms.registry()
            .creativeTab(
                () -> new ItemStack(ModBlocks.IRON_CHEST),
                Component.translatable("itemGroup.ironchest.general"));
    Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, IronChestsCommon.TAB, tab);
  }
}
