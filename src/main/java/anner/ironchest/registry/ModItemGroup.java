package anner.ironchest.registry;

import anner.ironchest.IronChests;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ModItemGroup {
    public static void registerItemGroup() {
        Registry.register(Registries.ITEM_GROUP, IronChests.TAB, FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModBlocks.IRON_CHEST))
            .displayName(Text.translatable("itemGroup.ironchest.general"))
            .build());
    }
}
