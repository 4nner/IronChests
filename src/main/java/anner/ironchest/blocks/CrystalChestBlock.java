package anner.ironchest.blocks;

import anner.ironchest.blocks.blockentities.CrystalChestEntity;
import anner.ironchest.blocks.blockentities.GenericChestEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.world.World;

public class CrystalChestBlock extends GenericChestBlock {
    public CrystalChestBlock() {
        super(AbstractBlock.Settings.create()
                        .hardness(3.0F)
                        .resistance(3.0F)
                        .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                        .requiresTool(),
                ChestTypes.CRYSTAL);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (type != this.getType().getBlockEntityType()) {
            return null;
        }
        return world.isClient ? (world1, pos, state1, blockEntity) -> ((GenericChestEntity) blockEntity).clientTick() : (world1, pos, state1, blockEntity) -> ((CrystalChestEntity) blockEntity).tick();
    }
}
