package io.github.cyberanner.ironchests.registry;

import io.github.cyberanner.ironchests.IronChests;
import io.github.cyberanner.ironchests.blocks.ChestTypes;
import io.github.cyberanner.ironchests.screenhandlers.ChestScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlerType {
    public static ScreenHandlerType<ChestScreenHandler> COPPER_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> IRON_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> GOLD_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> DIAMOND_CHEST;


    public static void registerScreenHandlers() {
        COPPER_CHEST = ScreenHandlerRegistry.registerSimple(new Identifier(IronChests.MOD_ID, "copper_chest"), (syncId, inventory) -> new ChestScreenHandler(COPPER_CHEST, ChestTypes.COPPER, syncId, inventory, ScreenHandlerContext.EMPTY));
        IRON_CHEST = ScreenHandlerRegistry.registerSimple(new Identifier(IronChests.MOD_ID, "iron_chest"), (syncId, inventory) -> new ChestScreenHandler(IRON_CHEST, ChestTypes.IRON, syncId, inventory, ScreenHandlerContext.EMPTY));
        GOLD_CHEST = ScreenHandlerRegistry.registerSimple(new Identifier(IronChests.MOD_ID, "gold_chest"), (syncId, inventory) -> new ChestScreenHandler(GOLD_CHEST, ChestTypes.GOLD, syncId, inventory, ScreenHandlerContext.EMPTY));
        DIAMOND_CHEST = ScreenHandlerRegistry.registerSimple(new Identifier(IronChests.MOD_ID, "diamond_chest"), (syncId, inventory) -> new ChestScreenHandler(DIAMOND_CHEST, ChestTypes.DIAMOND, syncId, inventory, ScreenHandlerContext.EMPTY));
    }
}
