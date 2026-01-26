package anner.ironchest.mixin;

import anner.ironchest.screenhandlers.ChestScreenHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityViewingMixin {
    @Inject(method = "isViewingContainerAt", at = @At("HEAD"), cancellable = true)
    private void ironchest$matchChestScreen(ViewerCountManager manager, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (!(player.currentScreenHandler instanceof ChestScreenHandler handler)) {
            return;
        }

        World world = player.getEntityWorld();
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof ChestBlockEntity)) {
            cir.setReturnValue(false);
            return;
        }

        Inventory inventory = handler.getBlockInventory();
        if (inventory == blockEntity) {
            cir.setReturnValue(true);
            return;
        }

        if (inventory instanceof DoubleInventory doubleInventory && doubleInventory.isPart((Inventory) blockEntity)) {
            cir.setReturnValue(true);
            return;
        }

        cir.setReturnValue(false);
    }
}
