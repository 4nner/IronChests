package io.github.cyberanner.ironchests.blocks;


import io.github.cyberanner.ironchests.blocks.blockentities.ObsidianChestEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;


public class ObsidianChestBlock extends GenericChestBlock{
    public ObsidianChestBlock() {
        super(FabricBlockSettings.of(Material.STONE)
                .hardness(3.0F)
                .resistance(6.0F)
                .sounds(BlockSoundGroup.STONE)
                .breakByTool(FabricToolTags.PICKAXES, 3)
                .requiresTool());
    }
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ObsidianChestEntity(pos, state);
    }
}