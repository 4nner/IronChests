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

    COPPER(45, 9, 176, 204, new Identifier(IronChests.MOD_ID, "models/block/copper_chest"), 256, 256),
    IRON(54, 9, 176, 222, new Identifier(IronChests.MOD_ID, "models/block/iron_chest"), 256, 256),
    GOLD(81, 9, 176, 276, new Identifier(IronChests.MOD_ID, "models/block/gold_chest"), 256, 276),
    DIAMOND(108, 12, 230, 276, new Identifier(IronChests.MOD_ID, "models/block/diamond_chest"), 256, 276),
    OBSIDIAN(108, 12, 230, 276, new Identifier(IronChests.MOD_ID, "models/block/obsidian_chest"), 256, 276),
    SILVER(72, 9, 184, 258, new Identifier(IronChests.MOD_ID, "models/block/silver_chest"), 256, 276),
    CRYSTAL(108, 12, 238, 276, new Identifier(IronChests.MOD_ID, "models/block/crystal_chest"), 256, 276),
    DIRT(1, 1, 184, 184, new Identifier(IronChests.MOD_ID, "models/block/dirt_chest"), 256, 256),
    CHRISTMAS(27, 9, 0, 0, new Identifier("models/block/christmas"), 0, 0),
    WOOD(0, 0, 0, 0, new Identifier(IronChests.MOD_ID, ("models/block/dirt_chest")), 0, 0);

    public final int size;
    public final int rowLength;
    public final int xSize;
    public final int ySize;
    public final Identifier texture;
    public final int textureXSize;
    public final int textureYSize;

    ChestTypes(int size, int rowLength, int xSize, int ySize, Identifier texture, int textureXSize, int textureYSize) {
        this.size = size;
        this.rowLength = rowLength;
        this.xSize = xSize;
        this.ySize = ySize;
        this.texture = texture;
        this.textureXSize = textureXSize;
        this.textureYSize = textureYSize;
    }

    public int getRowCount() {
        return this.size / this.rowLength;
    }

    public static Block get(ChestTypes type) {
        switch (type) {
            case COPPER:
                return ModBlocks.COPPER_CHEST;
            case IRON:
                return ModBlocks.IRON_CHEST;
            case GOLD:
                return ModBlocks.GOLD_CHEST;
            case DIAMOND:
                return ModBlocks.DIAMOND_CHEST;
            case OBSIDIAN:
                return ModBlocks.OBSIDIAN_CHEST;
            case CHRISTMAS:
                return ModBlocks.CHRISTMAS_CHEST;
            default:
                return Blocks.CHEST;
        }
    }


    // Used to implement Item Upgrades
    public GenericChestEntity makeEntity(BlockPos pos, BlockState state) {
        switch (this) {
            case COPPER:
                return new CopperChestEntity(pos, state);
            case IRON:
                return new IronChestEntity(pos, state);
            case GOLD:
                return new GoldChestEntity(pos, state);
            case DIAMOND:
                return new DiamondChestEntity(pos, state);
            case OBSIDIAN:
                return new ObsidianChestEntity(pos, state);
            case CHRISTMAS:
                return new ChristmasChestEntity(pos, state);
            default:
                return null;

        }
    }

}