package io.github.cyberanner.ironchests.registry;

import io.github.cyberanner.ironchests.IronChests;
import io.github.cyberanner.ironchests.blocks.blockentities.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntityType {;

    public static BlockEntityType<CopperChestBlockEntity> COPPER_CHEST_ENTITY;

    public static void registerBlockEntities() {
        COPPER_CHEST_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(IronChests.MOD_ID, "copper_chest_entity"), FabricBlockEntityTypeBuilder.create(CopperChestBlockEntity::new, ModBlocks.COPPER_CHEST).build(null));
    }
}
