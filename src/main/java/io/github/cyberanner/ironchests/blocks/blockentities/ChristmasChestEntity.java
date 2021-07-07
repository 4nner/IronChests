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

public class ChristmasChestEntity extends GenericChestEntity {

    public ChristmasChestEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityType.CHRISTMAS_CHEST, pos, state, ChestTypes.CHRISTMAS);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
        return new ChestScreenHandler(ModScreenHandlerType.CHRISTMAS_CHEST, ChestTypes.CHRISTMAS, syncId, inventory, ScreenHandlerContext.create(world, pos));
    }
}
