package anner.ironchest.mixin;

import anner.ironchest.screenhandlers.ChestScreenHandler;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.block.entity.ChestBlockEntity$1")
public abstract class ChestBlockEntityViewerCountMixin {
    @Shadow
    @Final
    private ChestBlockEntity field_27211;

    @Inject(method = "isPlayerViewing", at = @At("HEAD"), cancellable = true)
    private void ironchest$checkCustomScreenHandler(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (!(player.currentScreenHandler instanceof ChestScreenHandler handler)) {
            return;
        }

        Inventory inventory = handler.getBlockInventory();
        if (inventory == this.field_27211) {
            cir.setReturnValue(true);
            return;
        }

        if (inventory instanceof DoubleInventory doubleInventory && doubleInventory.isPart(this.field_27211)) {
            cir.setReturnValue(true);
        }
    }
}
