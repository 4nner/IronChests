package io.github.cyberanner.ironchests.registry;

import io.github.cyberanner.ironchests.IronChests;
import io.github.cyberanner.ironchests.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block COPPER_CHEST = new GenericChestBlock(ChestTypes.COPPER.setting(), ChestTypes.COPPER);
    public static final Block IRON_CHEST = new GenericChestBlock(ChestTypes.IRON.setting(), ChestTypes.IRON);
    public static final Block GOLD_CHEST = new GenericChestBlock(ChestTypes.GOLD.setting(), ChestTypes.GOLD);
    public static final Block DIAMOND_CHEST = new GenericChestBlock(ChestTypes.DIAMOND.setting(), ChestTypes.DIAMOND);
    public static final Block EMERALD_CHEST = new GenericChestBlock(ChestTypes.EMERALD.setting(), ChestTypes.EMERALD);
    public static final Block CRYSTAL_CHEST = new CrystalChestBlock();
    public static final Block OBSIDIAN_CHEST = new GenericChestBlock(ChestTypes.OBSIDIAN.setting(), ChestTypes.OBSIDIAN);
    public static final Block CHRISTMAS_CHEST = new GenericChestBlock(ChestTypes.CHRISTMAS.setting(), ChestTypes.CHRISTMAS);

    public static void registerBlocks() {
        Registry.register(Registries.BLOCK, new Identifier(IronChests.MOD_ID, "copper_chest"), COPPER_CHEST);
        Registry.register(Registries.BLOCK, new Identifier(IronChests.MOD_ID, "iron_chest"), IRON_CHEST);
        Registry.register(Registries.BLOCK, new Identifier(IronChests.MOD_ID, "gold_chest"), GOLD_CHEST);
        Registry.register(Registries.BLOCK, new Identifier(IronChests.MOD_ID, "diamond_chest"), DIAMOND_CHEST);
        Registry.register(Registries.BLOCK, new Identifier(IronChests.MOD_ID, "emerald_chest"), EMERALD_CHEST);
        Registry.register(Registries.BLOCK, new Identifier(IronChests.MOD_ID, "crystal_chest"), CRYSTAL_CHEST);
        Registry.register(Registries.BLOCK, new Identifier(IronChests.MOD_ID, "obsidian_chest"), OBSIDIAN_CHEST);
        Registry.register(Registries.BLOCK, new Identifier(IronChests.MOD_ID, "christmas_chest"), CHRISTMAS_CHEST);
    }
}
