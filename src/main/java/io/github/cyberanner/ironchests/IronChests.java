package io.github.cyberanner.ironchests;

import io.github.cyberanner.ironchests.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;



public class IronChests implements ModInitializer {

    public static final String MOD_ID = "ironchests";
    public static final Identifier UPDATE_INV_PACKET_ID = new Identifier(MOD_ID, "update");
    public static final RegistryKey<ItemGroup> TAB = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MOD_ID, "general"));

    @Override
    public void onInitialize() {
        ModItems.registerItems();
        ModBlocks.registerBlocks();
        ModItemGroup.registerItemGroup();
        ModBlockEntityType.registerBlockEntities();
        ModScreenHandlerType.registerScreenHandlers();
    }
}
