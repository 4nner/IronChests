package anner.ironchest.registry;

import anner.ironchest.client.ChestEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class ModBlockEntityRenderer {
    public static void registerBlockEntityRenderer() {
        BlockEntityRendererFactories.register(ModBlockEntityType.COPPER_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityType.IRON_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityType.GOLD_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityType.DIAMOND_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityType.EMERALD_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityType.CRYSTAL_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityType.OBSIDIAN_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityType.NETHERITE_CHEST, ChestEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityType.CHRISTMAS_CHEST, ChestEntityRenderer::new);
    }
}
