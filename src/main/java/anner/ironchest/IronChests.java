package anner.ironchest;

import anner.ironchest.registry.ModBlockEntityType;
import anner.ironchest.registry.ModBlocks;
import anner.ironchest.registry.ModItemGroup;
import anner.ironchest.registry.ModItems;
import anner.ironchest.registry.ModScreenHandlerType;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;

public class IronChests implements ModInitializer {
    public static final String MOD_ID = "ironchest";
    public static final ResourceKey<CreativeModeTab> TAB = ResourceKey.create(
        Registries.CREATIVE_MODE_TAB,
        Identifier.fromNamespaceAndPath(MOD_ID, "general")
    );

    @Override
    public void onInitialize() {
        ModBlocks.registerBlocks();
        ModItems.registerItems();
        ModItemGroup.registerItemGroup();
        ModBlockEntityType.registerBlockEntities();
        ModScreenHandlerType.registerScreenHandlers();
    }
}
