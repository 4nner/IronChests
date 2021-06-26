/*
package io.github.cyberanner.ironchests.blocks.blockentities;

import io.github.cyberanner.ironchests.blocks.ChestTypes;
import io.github.cyberanner.ironchests.registry.ModBlockEntityType;
import io.github.cyberanner.ironchests.registry.ModScreenHandlerType;
import io.github.cyberanner.ironchests.screenhandlers.ChestScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

public class IronChestEntity extends GenericChestEntity {

    public IronChestEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityType.IRON_CHEST, pos, state);
    }


    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
        return new ChestScreenHandler(ModScreenHandlerType.IRON_CHEST, type, syncId, inventory, ScreenHandlerContext.create(world, pos));
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory inventory) {
        return new ExtraChestScreenHandler(ModScreenHandlerType.COPPER_CHEST, type, syncId, inventory, ScreenHandlerContext.create(world, pos));
    }

}

 */