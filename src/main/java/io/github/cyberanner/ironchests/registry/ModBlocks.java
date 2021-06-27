package io.github.cyberanner.ironchests.registry;

import io.github.cyberanner.ironchests.IronChests;
import io.github.cyberanner.ironchests.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block COPPER_CHEST = new CopperChestBlock();
    public static final Block IRON_CHEST = new IronChestBlock();
    public static final Block GOLD_CHEST = new GoldChestBlock();
    public static final Block DIAMOND_CHEST = new DiamondChestBlock();
    public static final Block OBSIDIAN_CHEST = new ObsidianChestBlock();


    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(IronChests.MOD_ID, "copper_chest"), COPPER_CHEST);
        Registry.register(Registry.BLOCK, new Identifier(IronChests.MOD_ID, "iron_chest"), IRON_CHEST);
        Registry.register(Registry.BLOCK, new Identifier(IronChests.MOD_ID, "gold_chest"), GOLD_CHEST);
        Registry.register(Registry.BLOCK, new Identifier(IronChests.MOD_ID, "diamond_chest"), DIAMOND_CHEST);
        Registry.register(Registry.BLOCK, new Identifier(IronChests.MOD_ID, "obsidian_chest"), OBSIDIAN_CHEST);
    }
}
