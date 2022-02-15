package io.github.cyberanner.ironchests.registry;

import io.github.cyberanner.ironchests.client.ChestEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class ModBlockEntityRenderer {
    public static void registerBlockEntityRenderer() {
        BlockEntityRendererRegistry.register(ModBlockEntityType.COPPER_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntityType.IRON_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntityType.GOLD_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntityType.DIAMOND_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntityType.EMERALD_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntityType.CRYSTAL_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntityType.OBSIDIAN_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntityType.CHRISTMAS_CHEST, ChestEntityRenderer::new);
    }
}
