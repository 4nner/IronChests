package io.github.cyberanner.ironchests.registry;

import io.github.cyberanner.ironchests.IronChests;
import io.github.cyberanner.ironchests.screenhandlers.ChestScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlerType {
    public static ScreenHandlerType<ChestScreenHandler> COPPER_CHEST;


    public static void registerScreenHandlers() {
        COPPER_CHEST = ScreenHandlerRegistry.registerSimple((new Identifier(IronChests.MOD_ID, "copper_chest")), (syncId, inventory) -> new ChestScreenHandler(syncId, inventory, ScreenHandlerContext.EMPTY));
    }
}
