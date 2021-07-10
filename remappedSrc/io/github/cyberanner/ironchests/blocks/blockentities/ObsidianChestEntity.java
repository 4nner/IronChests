package io.github.cyberanner.ironchests.blocks.blockentities;

import io.github.cyberanner.ironchests.blocks.ChestTypes;
import io.github.cyberanner.ironchests.registry.ModBlockEntityType;
import io.github.cyberanner.ironchests.registry.ModScreenHandlerType;
import io.github.cyberanner.ironchests.screenhandlers.ChestScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.BlockPos;

public class ObsidianChestEntity extends GenericChestEntity {

    public ObsidianChestEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityType.OBSIDIAN_CHEST, pos, state, ChestTypes.OBSIDIAN);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
        return new ChestScreenHandler(ModScreenHandlerType.OBSIDIAN_CHEST, ChestTypes.OBSIDIAN, syncId, inventory, ScreenHandlerContext.create(world, pos));
    }
}