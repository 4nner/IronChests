package anner.ironchest;

import anner.ironchest.registry.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;


public class IronChests implements ModInitializer {

    public static final String MOD_ID = "ironchest";
    public static final RegistryKey<ItemGroup> TAB = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(MOD_ID, "general"));

    @Override
    public void onInitialize() {
        ModItems.registerItems();
        ModBlocks.registerBlocks();
        ModItemGroup.registerItemGroup();
        ModBlockEntityType.registerBlockEntities();
        ModScreenHandlerType.registerScreenHandlers();
    }
}
