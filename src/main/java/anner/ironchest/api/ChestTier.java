package anner.ironchest.api;

import anner.ironchest.screenhandlers.ChestScreenHandler;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public interface ChestTier {
    int size();
    int rowLength();
    Identifier texture();
    
    Supplier<Block> block();
    Supplier<BlockEntityType<? extends ChestBlockEntity>> blockEntityType();
    Supplier<ScreenHandlerType<ChestScreenHandler>> screenHandlerType();
    Supplier<AbstractBlock.Settings> settings();

    default int rowCount() {
        return size() / rowLength();
    }

    default ChestBlockEntity makeEntity(BlockPos pos, BlockState state) {
        return blockEntityType().get().instantiate(pos, state);
    }
}