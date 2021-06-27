package io.github.cyberanner.ironchests.registry;


import io.github.cyberanner.ironchests.IronChests;
import io.github.cyberanner.ironchests.blocks.blockentities.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModBlockEntityType {;

    // Declaring Chest Block Entities
    public static final BlockEntityType<CopperChestEntity> COPPER_CHEST = FabricBlockEntityTypeBuilder.create(CopperChestEntity::new, ModBlocks.COPPER_CHEST).build(null);
    public static final BlockEntityType<IronChestEntity> IRON_CHEST = FabricBlockEntityTypeBuilder.create(IronChestEntity::new, ModBlocks.IRON_CHEST).build(null);
    public static final BlockEntityType<IronChestEntity> GOLD_CHEST = FabricBlockEntityTypeBuilder.create(IronChestEntity::new, ModBlocks.GOLD_CHEST).build(null);
    public static final BlockEntityType<IronChestEntity> DIAMOND_CHEST = FabricBlockEntityTypeBuilder.create(IronChestEntity::new, ModBlocks.DIAMOND_CHEST).build(null);

    public static void registerBlockEntities() {
        // Registering Chest Block Entities
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(IronChests.MOD_ID, "copper_chest"), COPPER_CHEST);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(IronChests.MOD_ID, "iron_chest"), IRON_CHEST);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(IronChests.MOD_ID, "gold_chest"), GOLD_CHEST);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(IronChests.MOD_ID, "diamond_chest"), DIAMOND_CHEST);
    }
}
