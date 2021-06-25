package io.github.cyberanner.ironchests.blocks;

import io.github.cyberanner.ironchests.blocks.blockentities.CopperChestBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;


public class CopperChestBlock extends GenericChestBlock implements BlockEntityProvider {

    public CopperChestBlock() {
        super(FabricBlockSettings.of(Material.METAL)
                .strength(3.0F, 6.0F)
                .breakByTool(FabricToolTags.PICKAXES, 1)
                .requiresTool()
                .sounds(BlockSoundGroup.COPPER));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CopperChestBlockEntity(pos, state);
    }
}
