package io.github.cyberanner.ironchests.registry;

import io.github.cyberanner.ironchests.IronChests;
import io.github.cyberanner.ironchests.blocks.ChestTypes;
import io.github.cyberanner.ironchests.screenhandlers.ChestScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlerType {
    public static ScreenHandlerType<ChestScreenHandler> WOODEN_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> COPPER_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> IRON_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> GOLD_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> DIAMOND_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> EMERALD_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> CRYSTAL_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> OBSIDIAN_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> CHRISTMAS_CHEST;


    public static void registerScreenHandlers() {
        WOODEN_CHEST = ScreenHandlerRegistry.registerSimple(new Identifier(IronChests.MOD_ID, "wooden_chest"), (syncId, inventory) -> new ChestScreenHandler(WOODEN_CHEST, ChestTypes.WOOD, syncId, inventory, ScreenHandlerContext.EMPTY));
        COPPER_CHEST = ScreenHandlerRegistry.registerSimple(new Identifier(IronChests.MOD_ID, "copper_chest"), (syncId, inventory) -> new ChestScreenHandler(COPPER_CHEST, ChestTypes.COPPER, syncId, inventory, ScreenHandlerContext.EMPTY));
        IRON_CHEST = ScreenHandlerRegistry.registerSimple(new Identifier(IronChests.MOD_ID, "iron_chest"), (syncId, inventory) -> new ChestScreenHandler(IRON_CHEST, ChestTypes.IRON, syncId, inventory, ScreenHandlerContext.EMPTY));
        GOLD_CHEST = ScreenHandlerRegistry.registerSimple(new Identifier(IronChests.MOD_ID, "gold_chest"), (syncId, inventory) -> new ChestScreenHandler(GOLD_CHEST, ChestTypes.GOLD, syncId, inventory, ScreenHandlerContext.EMPTY));
        DIAMOND_CHEST = ScreenHandlerRegistry.registerSimple(new Identifier(IronChests.MOD_ID, "diamond_chest"), (syncId, inventory) -> new ChestScreenHandler(DIAMOND_CHEST, ChestTypes.DIAMOND, syncId, inventory, ScreenHandlerContext.EMPTY));
        EMERALD_CHEST = ScreenHandlerRegistry.registerSimple(new Identifier(IronChests.MOD_ID, "emerald_chest"), (syncId, inventory) -> new ChestScreenHandler(EMERALD_CHEST, ChestTypes.EMERALD, syncId, inventory, ScreenHandlerContext.EMPTY));
        CRYSTAL_CHEST = ScreenHandlerRegistry.registerSimple(new Identifier(IronChests.MOD_ID, "crystal_chest"), (syncId, inventory) -> new ChestScreenHandler(CRYSTAL_CHEST, ChestTypes.CRYSTAL, syncId, inventory, ScreenHandlerContext.EMPTY));
        OBSIDIAN_CHEST = ScreenHandlerRegistry.registerSimple(new Identifier(IronChests.MOD_ID, "obsidian_chest"), (syncId, inventory) -> new ChestScreenHandler(OBSIDIAN_CHEST, ChestTypes.OBSIDIAN, syncId, inventory, ScreenHandlerContext.EMPTY));
        CHRISTMAS_CHEST = ScreenHandlerRegistry.registerSimple(new Identifier(IronChests.MOD_ID, "christmas_chest"), (syncId, inventory) -> new ChestScreenHandler(CHRISTMAS_CHEST, ChestTypes.CHRISTMAS, syncId, inventory, ScreenHandlerContext.EMPTY));
    }
}
