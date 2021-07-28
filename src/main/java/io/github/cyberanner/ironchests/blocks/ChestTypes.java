package io.github.cyberanner.ironchests.blocks;

import io.github.cyberanner.ironchests.IronChests;
import io.github.cyberanner.ironchests.registry.ModBlockEntityType;
import io.github.cyberanner.ironchests.registry.ModBlocks;
import io.github.cyberanner.ironchests.registry.ModScreenHandlerType;
import io.github.cyberanner.ironchests.screenhandlers.ChestScreenHandler;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public enum ChestTypes {

    COPPER(45, 9, new Identifier(IronChests.MOD_ID, "model/copper_chest")),
    IRON(54, 9, new Identifier(IronChests.MOD_ID, "model/iron_chest")),
    GOLD(81, 9, new Identifier(IronChests.MOD_ID, "model/gold_chest")),
    DIAMOND(108, 12, new Identifier(IronChests.MOD_ID, "model/diamond_chest")),
    EMERALD(108, 12, new Identifier(IronChests.MOD_ID, "model/emerald_chest")),
    OBSIDIAN(108, 12, new Identifier(IronChests.MOD_ID, "model/obsidian_chest")),
    CRYSTAL(108, 12, new Identifier(IronChests.MOD_ID, "model/crystal_chest")),
    CHRISTMAS(27, 9, new Identifier("entity/chest/christmas")),
    WOOD(27, 9, new Identifier("entity/chest/normal"));

    public final int size;
    public final int rowLength;
    public final Identifier texture;

    ChestTypes(int size, int rowLength, Identifier texture) {
        this.size = size;
        this.rowLength = rowLength;
        this.texture = texture;
    }

    public int getRowCount() {
        return this.size / this.rowLength;
    }

    public static Block get(ChestTypes type) {
        return switch (type) {
            case COPPER -> ModBlocks.COPPER_CHEST;
            case IRON -> ModBlocks.IRON_CHEST;
            case GOLD -> ModBlocks.GOLD_CHEST;
            case DIAMOND -> ModBlocks.DIAMOND_CHEST;
            case EMERALD -> ModBlocks.EMERALD_CHEST;
            case CRYSTAL -> ModBlocks.CRYSTAL_CHEST;
            case OBSIDIAN -> ModBlocks.OBSIDIAN_CHEST;
            case CHRISTMAS -> ModBlocks.CHRISTMAS_CHEST;
            default -> Blocks.CHEST;
        };
    }

    // Used to implement Item Upgrades
    public ChestBlockEntity makeEntity(BlockPos pos, BlockState state) {
        return switch (this) {
            case COPPER -> ModBlockEntityType.COPPER_CHEST.instantiate(pos, state);
            case IRON -> ModBlockEntityType.IRON_CHEST.instantiate(pos, state);
            case GOLD -> ModBlockEntityType.GOLD_CHEST.instantiate(pos, state);
            case DIAMOND -> ModBlockEntityType.DIAMOND_CHEST.instantiate(pos, state);
            case EMERALD -> ModBlockEntityType.EMERALD_CHEST.instantiate(pos, state);
            case CRYSTAL -> ModBlockEntityType.CRYSTAL_CHEST.instantiate(pos, state);
            case OBSIDIAN -> ModBlockEntityType.OBSIDIAN_CHEST.instantiate(pos, state);
            case CHRISTMAS -> ModBlockEntityType.CHRISTMAS_CHEST.instantiate(pos, state);
            default -> new ChestBlockEntity(pos, state);
        };
    }

    public ScreenHandlerType<ChestScreenHandler> getScreenHandlerType() {
        return switch (this) {
            case COPPER -> ModScreenHandlerType.COPPER_CHEST;
            case IRON -> ModScreenHandlerType.IRON_CHEST;
            case GOLD -> ModScreenHandlerType.GOLD_CHEST;
            case DIAMOND -> ModScreenHandlerType.DIAMOND_CHEST;
            case EMERALD -> ModScreenHandlerType.EMERALD_CHEST;
            case CRYSTAL -> ModScreenHandlerType.CRYSTAL_CHEST;
            case OBSIDIAN -> ModScreenHandlerType.OBSIDIAN_CHEST;
            default -> ModScreenHandlerType.CHRISTMAS_CHEST;
        };
    }

    public BlockEntityType<? extends ChestBlockEntity> getBlockEntityType() {
        return switch (this) {
            case COPPER -> ModBlockEntityType.COPPER_CHEST;
            case IRON -> ModBlockEntityType.IRON_CHEST;
            case GOLD -> ModBlockEntityType.GOLD_CHEST;
            case DIAMOND -> ModBlockEntityType.DIAMOND_CHEST;
            case EMERALD -> ModBlockEntityType.EMERALD_CHEST;
            case CRYSTAL -> ModBlockEntityType.CRYSTAL_CHEST;
            case OBSIDIAN -> ModBlockEntityType.OBSIDIAN_CHEST;
            case CHRISTMAS -> ModBlockEntityType.CHRISTMAS_CHEST;
            default -> BlockEntityType.CHEST;
        };
    }
}