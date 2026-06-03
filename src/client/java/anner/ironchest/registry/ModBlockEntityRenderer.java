package anner.ironchest.registry;

import anner.ironchest.blocks.ChestTypes;
import anner.ironchest.client.ChestEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

public class ModBlockEntityRenderer {
    public static void registerBlockEntityRenderer() {
        for (ChestTypes type : ChestTypes.PLAYABLE) {
            register(type.getBlockEntityType());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends ChestBlockEntity> void register(BlockEntityType<? extends ChestBlockEntity> blockEntityType) {
        BlockEntityRenderers.register((BlockEntityType<T>) blockEntityType, ChestEntityRenderer::new);
    }
}
