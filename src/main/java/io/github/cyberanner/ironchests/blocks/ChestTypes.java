package io.github.cyberanner.ironchests.blocks;

import io.github.cyberanner.ironchests.IronChests;
import io.github.cyberanner.ironchests.blocks.blockentities.*;
import io.github.cyberanner.ironchests.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public enum ChestTypes {

    COPPER(45, 9, 176, 204, new Identifier(IronChests.MOD_ID, "models/block/copper_chest")),
    IRON(54, 9, 176, 222, new Identifier(IronChests.MOD_ID, "models/block/iron_chest")),
    GOLD(81, 9, 176, 276, new Identifier(IronChests.MOD_ID, "models/block/gold_chest")),
    DIAMOND(108, 12, 230, 276, new Identifier(IronChests.MOD_ID, "models/block/diamond_chest")),
    OBSIDIAN(108, 12, 230, 276, new Identifier(IronChests.MOD_ID, "models/block/obsidian_chest")),
    SILVER(72, 9, 184, 258, new Identifier(IronChests.MOD_ID, "models/block/silver_chest")),
    CRYSTAL(108, 12, 238, 276, new Identifier(IronChests.MOD_ID, "models/block/crystal_chest")),
    CHRISTMAS(27, 9, 0, 0, new Identifier("models/block/christmas")),
    WOOD(27, 9, 0, 0, new Identifier("entity/chest/normal"));

    public final int size;
    public final int rowLength;
    public final int xSize;
    public final int ySize;
    public final Identifier texture;

    ChestTypes(int size, int rowLength, int xSize, int ySize, Identifier texture) {
        this.size = size;
        this.rowLength = rowLength;
        this.xSize = xSize;
        this.ySize = ySize;
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
            case OBSIDIAN -> ModBlocks.OBSIDIAN_CHEST;
            case CHRISTMAS -> ModBlocks.CHRISTMAS_CHEST;
            default -> Blocks.CHEST;
        };
    }

    // Used to implement Item Upgrades
    public GenericChestEntity makeEntity(BlockPos pos, BlockState state) {
        return switch (this) {
            case COPPER -> new CopperChestEntity(pos, state);
            case IRON -> new IronChestEntity(pos, state);
            case GOLD -> new GoldChestEntity(pos, state);
            case DIAMOND -> new DiamondChestEntity(pos, state);
            case OBSIDIAN -> new ObsidianChestEntity(pos, state);
            case CHRISTMAS -> new ChristmasChestEntity(pos, state);
            default -> null;
        };
    }
}