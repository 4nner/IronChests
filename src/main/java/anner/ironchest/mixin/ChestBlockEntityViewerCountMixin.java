package anner.ironchest.mixin;

import anner.ironchest.screenhandlers.ChestScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.level.block.entity.ChestBlockEntity$1")
public abstract class ChestBlockEntityViewerCountMixin {
    @Inject(method = "isOwnContainer", at = @At("RETURN"), cancellable = true)
    private void ironchest$countCustomChestScreen(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            return;
        }

        if (!(player.containerMenu instanceof ChestScreenHandler handler)) {
            return;
        }

        ChestBlockEntity chest = ((ChestBlockEntityOpenersCounterAccessor) this).ironchest$getChestBlockEntity();
        if (handler.getBlockInventory() == chest) {
            cir.setReturnValue(true);
            return;
        }

        BlockPos menuPos = handler.getChestBlockPos();
        if (menuPos != null && menuPos.equals(chest.getBlockPos())) {
            cir.setReturnValue(true);
        }
    }
}
