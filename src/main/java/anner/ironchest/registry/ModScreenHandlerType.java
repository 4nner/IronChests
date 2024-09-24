package anner.ironchest.registry;

import anner.ironchest.IronChests;
import anner.ironchest.blocks.ChestTypes;
import anner.ironchest.screenhandlers.ChestScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlerType {
    public static ScreenHandlerType<ChestScreenHandler> COPPER_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> IRON_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> GOLD_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> DIAMOND_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> EMERALD_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> CRYSTAL_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> OBSIDIAN_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> NETHERITE_CHEST;
    public static ScreenHandlerType<ChestScreenHandler> CHRISTMAS_CHEST;

    public static void registerScreenHandlers() {
        COPPER_CHEST = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(IronChests.MOD_ID, "copper_chest"), new ScreenHandlerType<>((syncId, inventory) -> new ChestScreenHandler(COPPER_CHEST, ChestTypes.COPPER, syncId, inventory, ScreenHandlerContext.EMPTY), FeatureSet.empty()));
        IRON_CHEST = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(IronChests.MOD_ID, "iron_chest"), new ScreenHandlerType<>((syncId, inventory) -> new ChestScreenHandler(IRON_CHEST, ChestTypes.IRON, syncId, inventory, ScreenHandlerContext.EMPTY), FeatureSet.empty()));
        GOLD_CHEST = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(IronChests.MOD_ID, "gold_chest"), new ScreenHandlerType<>((syncId, inventory) -> new ChestScreenHandler(GOLD_CHEST, ChestTypes.GOLD, syncId, inventory, ScreenHandlerContext.EMPTY), FeatureSet.empty()));
        DIAMOND_CHEST = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(IronChests.MOD_ID, "diamond_chest"), new ScreenHandlerType<>((syncId, inventory) -> new ChestScreenHandler(DIAMOND_CHEST, ChestTypes.DIAMOND, syncId, inventory, ScreenHandlerContext.EMPTY), FeatureSet.empty()));
        EMERALD_CHEST = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(IronChests.MOD_ID, "emerald_chest"), new ScreenHandlerType<>((syncId, inventory) -> new ChestScreenHandler(EMERALD_CHEST, ChestTypes.EMERALD, syncId, inventory, ScreenHandlerContext.EMPTY), FeatureSet.empty()));
        CRYSTAL_CHEST = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(IronChests.MOD_ID, "crystal_chest"), new ScreenHandlerType<>((syncId, inventory) -> new ChestScreenHandler(CRYSTAL_CHEST, ChestTypes.CRYSTAL, syncId, inventory, ScreenHandlerContext.EMPTY), FeatureSet.empty()));
        OBSIDIAN_CHEST = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(IronChests.MOD_ID, "obsidian_chest"), new ScreenHandlerType<>((syncId, inventory) -> new ChestScreenHandler(OBSIDIAN_CHEST, ChestTypes.OBSIDIAN, syncId, inventory, ScreenHandlerContext.EMPTY), FeatureSet.empty()));
        NETHERITE_CHEST = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(IronChests.MOD_ID, "netherite_chest"), new ScreenHandlerType<>((syncId, inventory) -> new ChestScreenHandler(NETHERITE_CHEST, ChestTypes.NETHERITE, syncId, inventory, ScreenHandlerContext.EMPTY), FeatureSet.empty()));
        CHRISTMAS_CHEST = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(IronChests.MOD_ID, "christmas_chest"), new ScreenHandlerType<>((syncId, inventory) -> new ChestScreenHandler(CHRISTMAS_CHEST, ChestTypes.CHRISTMAS, syncId, inventory, ScreenHandlerContext.EMPTY), FeatureSet.empty()));
    }
}
