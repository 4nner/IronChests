package anner.ironchest.api;

import anner.ironchest.screenhandlers.ChestScreenHandler;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class SimpleChestTier implements ChestTier {
    
    private final int size;
    private final int rowLength;
    private final Identifier texture;
    private final Supplier<Block> block;
    private final Supplier<BlockEntityType<? extends ChestBlockEntity>> blockEntityType;
    private final Supplier<ScreenHandlerType<ChestScreenHandler>> screenHandlerType;
    private final Supplier<AbstractBlock.Settings> settings;

    public SimpleChestTier(
            int size,
            int rowLength,
            Identifier texture,
            Supplier<Block> block,
            Supplier<BlockEntityType<? extends ChestBlockEntity>> blockEntityType,
            Supplier<ScreenHandlerType<ChestScreenHandler>> screenHandlerType,
            Supplier<AbstractBlock.Settings> settings) {
        this.size = size;
        this.rowLength = rowLength;
        this.texture = texture;
        this.block = block;
        this.blockEntityType = blockEntityType;
        this.screenHandlerType = screenHandlerType;
        this.settings = settings;
    }

    @Override
    public int size() { return this.size; }

    @Override
    public int rowLength() { return this.rowLength; }

    @Override
    public Identifier texture() { return this.texture; }

    @Override
    public Supplier<Block> block() { return this.block; }

    @Override
    public Supplier<BlockEntityType<? extends ChestBlockEntity>> blockEntityType() { return this.blockEntityType; }

    @Override
    public Supplier<ScreenHandlerType<ChestScreenHandler>> screenHandlerType() { return this.screenHandlerType; }

    @Override
    public Supplier<AbstractBlock.Settings> settings() { return this.settings; }
}