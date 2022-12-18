package io.github.cyberanner.ironchests;

import io.github.cyberanner.ironchests.registry.ModBlockEntityType;
import io.github.cyberanner.ironchests.registry.ModBlocks;
import io.github.cyberanner.ironchests.registry.ModItems;
import io.github.cyberanner.ironchests.registry.ModScreenHandlerType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;



public class IronChests implements ModInitializer {

    public static final String MOD_ID = "ironchests";
    public static final Identifier UPDATE_INV_PACKET_ID = new Identifier(MOD_ID, "update");
    public static final ItemGroup TAB = FabricItemGroup.builder(new Identifier(MOD_ID, "general"))
	    .icon(() -> new ItemStack(ModBlocks.IRON_CHEST))
	    .build();

    @Override
    public void onInitialize() {
        ModItems.registerItems();
        ModBlocks.registerBlocks();
        ModBlockEntityType.registerBlockEntities();
        ModScreenHandlerType.registerScreenHandlers();
    }
}
