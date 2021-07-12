package io.github.cyberanner.ironchests;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.cyberanner.ironchests.blocks.ChestTypes;
import io.github.cyberanner.ironchests.client.ChestEntityRenderer;
import io.github.cyberanner.ironchests.registry.ModBlockEntityType;
import io.github.cyberanner.ironchests.registry.ModScreenHandlerType;
import io.github.cyberanner.ironchests.screenhandlers.ChestScreenHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.render.TexturedRenderLayers;


@Environment(EnvType.CLIENT)
public class IronChestsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.COPPER_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));
        ScreenRegistry.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.IRON_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));
        ScreenRegistry.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.GOLD_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));
        ScreenRegistry.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.DIAMOND_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));
        ScreenRegistry.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.OBSIDIAN_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));
        ScreenRegistry.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.CHRISTMAS_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));

        BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntityType.COPPER_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntityType.IRON_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntityType.GOLD_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntityType.DIAMOND_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntityType.OBSIDIAN_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntityType.CHRISTMAS_CHEST, ChestEntityRenderer::new);

        //Register Textures to Chest Atlas
        ClientSpriteRegistryCallback.event(TexturedRenderLayers.CHEST_ATLAS_TEXTURE).register((texture, registry) -> {
            registry.register(ChestTypes.COPPER.texture);
            registry.register(ChestTypes.IRON.texture);
            registry.register(ChestTypes.GOLD.texture);
            registry.register(ChestTypes.DIAMOND.texture);
            registry.register(ChestTypes.OBSIDIAN.texture);
            registry.register(ChestTypes.CRYSTAL.texture);
            registry.register(ChestTypes.CHRISTMAS.texture);
        });
    }
}
