package anner.ironchest.mixin;

import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.world.level.block.entity.ChestBlockEntity$1")
public interface ChestBlockEntityOpenersCounterAccessor {
    @Accessor("this$0")
    ChestBlockEntity ironchest$getChestBlockEntity();
}
