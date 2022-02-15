package io.github.cyberanner.ironchests.blocks;

import io.github.cyberanner.ironchests.blocks.blockentities.CrystalChestEntity;
import io.github.cyberanner.ironchests.blocks.blockentities.GenericChestEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.world.World;

public class CrystalChestBlock extends GenericChestBlock {
    public CrystalChestBlock() {
        super(FabricBlockSettings.of(Material.GLASS)
                .hardness(3.0F)
                .resistance(3.0F)
                .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                .requiresTool(),
                ChestTypes.CRYSTAL);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (type != this.getType().getBlockEntityType()){ return null; }
        return world.isClient ? (world1, pos, state1, blockEntity) -> ((GenericChestEntity)blockEntity).clientTick() : (world1, pos, state1, blockEntity) -> ((CrystalChestEntity)blockEntity).tick();
    }
}
