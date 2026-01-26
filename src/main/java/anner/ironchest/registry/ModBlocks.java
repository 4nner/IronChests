package anner.ironchest.registry;

import anner.ironchest.IronChests;
import anner.ironchest.blocks.ChestTypes;
import anner.ironchest.blocks.CrystalChestBlock;
import anner.ironchest.blocks.GenericChestBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block COPPER_CHEST = new GenericChestBlock(settings("copper_chest", ChestTypes.COPPER), ChestTypes.COPPER);
    public static final Block IRON_CHEST = new GenericChestBlock(settings("iron_chest", ChestTypes.IRON), ChestTypes.IRON);
    public static final Block GOLD_CHEST = new GenericChestBlock(settings("gold_chest", ChestTypes.GOLD), ChestTypes.GOLD);
    public static final Block DIAMOND_CHEST = new GenericChestBlock(settings("diamond_chest", ChestTypes.DIAMOND), ChestTypes.DIAMOND);
    public static final Block EMERALD_CHEST = new GenericChestBlock(settings("emerald_chest", ChestTypes.EMERALD), ChestTypes.EMERALD);
    public static final Block CRYSTAL_CHEST = new CrystalChestBlock(settings("crystal_chest", ChestTypes.CRYSTAL));
    public static final Block OBSIDIAN_CHEST = new GenericChestBlock(settings("obsidian_chest", ChestTypes.OBSIDIAN), ChestTypes.OBSIDIAN);
    public static final Block NETHERITE_CHEST = new GenericChestBlock(settings("netherite_chest", ChestTypes.NETHERITE), ChestTypes.NETHERITE);
    public static final Block CHRISTMAS_CHEST = new GenericChestBlock(settings("christmas_chest", ChestTypes.CHRISTMAS), ChestTypes.CHRISTMAS);

    public static void registerBlocks() {
        Registry.register(Registries.BLOCK, Identifier.of(IronChests.MOD_ID, "copper_chest"), COPPER_CHEST);
        Registry.register(Registries.BLOCK, Identifier.of(IronChests.MOD_ID, "iron_chest"), IRON_CHEST);
        Registry.register(Registries.BLOCK, Identifier.of(IronChests.MOD_ID, "gold_chest"), GOLD_CHEST);
        Registry.register(Registries.BLOCK, Identifier.of(IronChests.MOD_ID, "diamond_chest"), DIAMOND_CHEST);
        Registry.register(Registries.BLOCK, Identifier.of(IronChests.MOD_ID, "emerald_chest"), EMERALD_CHEST);
        Registry.register(Registries.BLOCK, Identifier.of(IronChests.MOD_ID, "crystal_chest"), CRYSTAL_CHEST);
        Registry.register(Registries.BLOCK, Identifier.of(IronChests.MOD_ID, "obsidian_chest"), OBSIDIAN_CHEST);
        Registry.register(Registries.BLOCK, Identifier.of(IronChests.MOD_ID, "netherite_chest"), NETHERITE_CHEST);
        Registry.register(Registries.BLOCK, Identifier.of(IronChests.MOD_ID, "christmas_chest"), CHRISTMAS_CHEST);
    }

    private static AbstractBlock.Settings settings(String name, ChestTypes type) {
        return type.setting().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(IronChests.MOD_ID, name)));
    }
}
