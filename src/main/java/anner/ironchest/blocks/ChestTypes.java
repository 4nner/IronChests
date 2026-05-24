package anner.ironchest.blocks;

import anner.ironchest.IronChests;
import anner.ironchest.api.ChestTier;
import anner.ironchest.api.SimpleChestTier;
import anner.ironchest.registry.ModBlockEntityType;
import anner.ironchest.registry.ModBlocks;
import anner.ironchest.registry.ModScreenHandlerType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ChestTypes {

    public static final ChestTier NETHERITE = new SimpleChestTier(
            126, 14, Identifier.of(IronChests.MOD_ID, "entity/chest/netherite_chest"),
            () -> ModBlocks.NETHERITE_CHEST,
            () -> ModBlockEntityType.NETHERITE_CHEST,
            () -> ModScreenHandlerType.NETHERITE_CHEST,
            () -> AbstractBlock.Settings.create().hardness(50.0F).resistance(1200.0F).sounds(BlockSoundGroup.NETHERITE).requiresTool()
    );

    public static final ChestTier OBSIDIAN = new SimpleChestTier(
            108, 12, Identifier.of(IronChests.MOD_ID, "entity/chest/obsidian_chest"),
            () -> ModBlocks.OBSIDIAN_CHEST,
            () -> ModBlockEntityType.OBSIDIAN_CHEST,
            () -> ModScreenHandlerType.OBSIDIAN_CHEST,
            () -> AbstractBlock.Settings.create().hardness(50.0F).resistance(1200.0F).sounds(BlockSoundGroup.STONE).requiresTool()
    );

    public static final ChestTier CRYSTAL = new SimpleChestTier(
            108, 12, Identifier.of(IronChests.MOD_ID, "entity/chest/crystal_chest"),
            () -> ModBlocks.CRYSTAL_CHEST,
            () -> ModBlockEntityType.CRYSTAL_CHEST,
            () -> ModScreenHandlerType.CRYSTAL_CHEST,
            () -> AbstractBlock.Settings.create().hardness(3.0F).resistance(3.0F).sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool()
    );

    public static final ChestTier DIAMOND = new SimpleChestTier(
            108, 12, Identifier.of(IronChests.MOD_ID, "entity/chest/diamond_chest"),
            () -> ModBlocks.DIAMOND_CHEST,
            () -> ModBlockEntityType.DIAMOND_CHEST,
            () -> ModScreenHandlerType.DIAMOND_CHEST,
            () -> AbstractBlock.Settings.create().hardness(5.0F).resistance(6.0F).sounds(BlockSoundGroup.STONE).requiresTool()
    );

    public static final ChestTier EMERALD = new SimpleChestTier(
            108, 12, Identifier.of(IronChests.MOD_ID, "entity/chest/emerald_chest"),
            () -> ModBlocks.EMERALD_CHEST,
            () -> ModBlockEntityType.EMERALD_CHEST,
            () -> ModScreenHandlerType.EMERALD_CHEST,
            () -> AbstractBlock.Settings.create().hardness(5.0F).resistance(6.0F).sounds(BlockSoundGroup.STONE).requiresTool()
    );

    public static final ChestTier GOLD = new SimpleChestTier(
            81, 9, Identifier.of(IronChests.MOD_ID, "entity/chest/gold_chest"),
            () -> ModBlocks.GOLD_CHEST,
            () -> ModBlockEntityType.GOLD_CHEST,
            () -> ModScreenHandlerType.GOLD_CHEST,
            () -> AbstractBlock.Settings.create().hardness(3.0F).resistance(6.0F).sounds(BlockSoundGroup.COPPER).requiresTool()
    );

    public static final ChestTier IRON = new SimpleChestTier(
            54, 9, Identifier.of(IronChests.MOD_ID, "entity/chest/iron_chest"),
            () -> ModBlocks.IRON_CHEST,
            () -> ModBlockEntityType.IRON_CHEST,
            () -> ModScreenHandlerType.IRON_CHEST,
            () -> AbstractBlock.Settings.create().hardness(5.0F).resistance(6.0F).sounds(BlockSoundGroup.IRON).requiresTool()
    );

    public static final ChestTier COPPER = new SimpleChestTier(
            45, 9, Identifier.of(IronChests.MOD_ID, "entity/chest/copper_chest"),
            () -> ModBlocks.COPPER_CHEST,
            () -> ModBlockEntityType.COPPER_CHEST,
            () -> ModScreenHandlerType.COPPER_CHEST,
            () -> AbstractBlock.Settings.create().hardness(3.0F).resistance(6.0F).sounds(BlockSoundGroup.COPPER).requiresTool()
    );

    public static final ChestTier CHRISTMAS = new SimpleChestTier(
            27, 9, Identifier.of("entity/chest/christmas"),
            () -> ModBlocks.CHRISTMAS_CHEST,
            () -> ModBlockEntityType.CHRISTMAS_CHEST,
            () -> ModScreenHandlerType.CHRISTMAS_CHEST,
            () -> AbstractBlock.Settings.create().hardness(3.0F).resistance(3.0F).sounds(BlockSoundGroup.WOOD)
    );

    public static final ChestTier WOOD = new SimpleChestTier(
            27, 9, Identifier.of("entity/chest/normal"),
            () -> Blocks.CHEST,
            () -> BlockEntityType.CHEST,
            () -> ModScreenHandlerType.CHRISTMAS_CHEST,
            () -> AbstractBlock.Settings.create().hardness(3.0F).resistance(3.0F).sounds(BlockSoundGroup.WOOD)
    );

    public static ChestTier[] values() {
        return new ChestTier[]{
                NETHERITE, OBSIDIAN, CRYSTAL, DIAMOND, 
                EMERALD, GOLD, IRON, COPPER, CHRISTMAS, WOOD
        };
    }
}