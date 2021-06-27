package io.github.cyberanner.ironchests.blocks;

import io.github.cyberanner.ironchests.blocks.blockentities.DiamondChestEntity;
import io.github.cyberanner.ironchests.registry.ModBlockEntityType;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;

public class DiamondChestBlock extends GenericChestBlock{
        public DiamondChestBlock() {
            super(FabricBlockSettings.of(Material.METAL)
                    .hardness(3.0F)
                    .resistance(6.0F)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                    .breakByTool(FabricToolTags.PICKAXES, 1)
                    .requiresTool());
        }
        @Override
        public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
            return new DiamondChestEntity(pos, state);
        }
    }