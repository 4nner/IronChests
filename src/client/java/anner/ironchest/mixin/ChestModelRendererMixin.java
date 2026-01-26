package anner.ironchest.mixin;

import java.util.function.Function;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.item.model.special.ChestModelRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(ChestModelRenderer.class)
public abstract class ChestModelRendererMixin {
    @Redirect(
        method = "render(Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IIZI)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/util/SpriteIdentifier;getRenderLayer(Ljava/util/function/Function;)Lnet/minecraft/client/render/RenderLayer;"
        )
    )
    private RenderLayer ironchest$useCutoutRenderLayer(SpriteIdentifier spriteIdentifier, Function<Identifier, RenderLayer> original) {
        if ("ironchest".equals(spriteIdentifier.getTextureId().getNamespace())) {
            return spriteIdentifier.getRenderLayer(id -> TexturedRenderLayers.getChest());
        }
        return spriteIdentifier.getRenderLayer(original);
    }
}
