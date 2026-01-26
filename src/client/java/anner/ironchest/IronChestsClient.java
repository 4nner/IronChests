package anner.ironchest;

import anner.ironchest.registry.ModBlockEntityRenderer;
import anner.ironchest.registry.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;


@Environment(EnvType.CLIENT)
public class IronChestsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModScreenHandlers.registerScreenHandlers();
        ModBlockEntityRenderer.registerBlockEntityRenderer();
    }
}
