package anner.ironchest.registry;

import anner.ironchest.IronChests;
import anner.ironchest.blocks.ChestTypes;
import anner.ironchest.blocks.GenericChestBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static final Block COPPER_CHEST = create(ChestTypes.COPPER);
    public static final Block IRON_CHEST = create(ChestTypes.IRON);
    public static final Block GOLD_CHEST = create(ChestTypes.GOLD);
    public static final Block DIAMOND_CHEST = create(ChestTypes.DIAMOND);
    public static final Block EMERALD_CHEST = create(ChestTypes.EMERALD);
    public static final Block CRYSTAL_CHEST = create(ChestTypes.CRYSTAL);
    public static final Block OBSIDIAN_CHEST = create(ChestTypes.OBSIDIAN);
    public static final Block NETHERITE_CHEST = create(ChestTypes.NETHERITE);
    public static final Block CHRISTMAS_CHEST = create(ChestTypes.CHRISTMAS);

    static {
        ChestTypes.COPPER.bindBlock(COPPER_CHEST);
        ChestTypes.IRON.bindBlock(IRON_CHEST);
        ChestTypes.GOLD.bindBlock(GOLD_CHEST);
        ChestTypes.DIAMOND.bindBlock(DIAMOND_CHEST);
        ChestTypes.EMERALD.bindBlock(EMERALD_CHEST);
        ChestTypes.CRYSTAL.bindBlock(CRYSTAL_CHEST);
        ChestTypes.OBSIDIAN.bindBlock(OBSIDIAN_CHEST);
        ChestTypes.NETHERITE.bindBlock(NETHERITE_CHEST);
        ChestTypes.CHRISTMAS.bindBlock(CHRISTMAS_CHEST);
    }

    private static Block create(ChestTypes type) {
        return new GenericChestBlock(blockProperties(type), type);
    }

    public static void registerBlocks() {
        for (ChestTypes type : ChestTypes.PLAYABLE) {
            Registry.register(
                BuiltInRegistries.BLOCK,
                Identifier.fromNamespaceAndPath(IronChests.MOD_ID, type.registryId),
                type.getBlock()
            );
        }
    }

    private static BlockBehaviour.Properties blockProperties(ChestTypes type) {
        return type.blockProperties()
            .setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(IronChests.MOD_ID, type.registryId)));
    }
}
