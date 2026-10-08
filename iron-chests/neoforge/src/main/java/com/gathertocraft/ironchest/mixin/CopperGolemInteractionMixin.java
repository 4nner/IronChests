package com.gathertocraft.ironchest.mixin;

import com.gathertocraft.ironchest.support.CopperGolemSupport;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.TransportItemsBetweenContainers;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Lets copper golems deposit into IronChests containers. Vanilla only targets {@code
 * minecraft:chest} and {@code minecraft:trapped_chest} here; the shared helper decides.
 */
@Mixin(TransportItemsBetweenContainers.class)
public abstract class CopperGolemInteractionMixin {
  @Inject(
      method =
          "isWantedBlock(Lnet/minecraft/world/entity/PathfinderMob;Lnet/minecraft/world/level/block/state/BlockState;)Z",
      at = @At("RETURN"),
      cancellable = true)
  private void ironchest$allowGolemDeposit(
      PathfinderMob mob, BlockState state, CallbackInfoReturnable<Boolean> cir) {
    if (!cir.getReturnValueZ() && CopperGolemSupport.shouldInteract(mob, state)) {
      cir.setReturnValue(true);
    }
  }
}
