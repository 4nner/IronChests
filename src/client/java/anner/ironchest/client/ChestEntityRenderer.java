package anner.ironchest.client;

import anner.ironchest.blocks.GenericChestBlock;
import anner.ironchest.blocks.blockentities.CrystalChestEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.ChestBlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ChestEntityRenderer<T extends ChestBlockEntity> extends ChestBlockEntityRenderer<T> {
    private final SpriteHolder spriteHolder;

    private final ChestModel chestModel;

    public ChestEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
        this.spriteHolder = context.spriteHolder();

        ModelPart modelPart = context.getLayerModelPart(EntityModelLayers.CHEST);
        this.chestModel = new ChestModel(modelPart);
    }

    @Override
    public ChestBlockEntityRenderState createRenderState() {
        return new GenericChestRenderState();
    }

    @Override
    public void updateRenderState(T entity, ChestBlockEntityRenderState state, float tickDelta, Vec3d cameraPos, ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        super.updateRenderState(entity, state, tickDelta, cameraPos, crumblingOverlay);
        if (entity instanceof anner.ironchest.blocks.blockentities.GenericChestEntity chest) {
            state.lidAnimationProgress = chest.getAnimationProgress(tickDelta);
        }
        GenericChestRenderState renderState = (GenericChestRenderState) state;
        BlockState blockState = entity.getWorld() != null
            ? entity.getCachedState()
            : Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        Block block = blockState.getBlock();
        if (block instanceof GenericChestBlock chest) {
            renderState.textureId = new SpriteIdentifier(TexturedRenderLayers.CHEST_ATLAS_TEXTURE, chest.getType().texture);
        } else {
            renderState.textureId = TexturedRenderLayers.getChestTextureId(state.variant, state.chestType);
        }
        if (entity instanceof CrystalChestEntity crystal) {
            renderState.topStacks = crystal.getTopStacks();
            renderState.renderItems = true;
        } else {
            renderState.topStacks = null;
            renderState.renderItems = false;
        }
        renderState.seed = (int) entity.getPos().asLong();
    }

    @Override
    public void render(ChestBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState) {
        if (!(state.blockState.getBlock() instanceof GenericChestBlock)) {
            super.render(state, matrices, queue, cameraRenderState);
            return;
        }

        GenericChestRenderState renderState = (GenericChestRenderState) state;
        SpriteIdentifier spriteIdentifier = renderState.textureId;
        RenderLayer renderLayer = spriteIdentifier.getRenderLayer(id -> TexturedRenderLayers.getChest());
        Sprite sprite = this.spriteHolder.getSprite(spriteIdentifier);

        matrices.push();
        matrices.translate(0.5F, 0.5F, 0.5F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-state.yaw));
        matrices.translate(-0.5F, -0.5F, -0.5F);

        queue.submitModel(
            this.chestModel,
            state,
            matrices,
            renderLayer,
            state.lightmapCoordinates,
            OverlayTexture.DEFAULT_UV,
            -1,
            sprite,
            0,
            state.crumblingOverlay
        );

        if (renderState.renderItems && renderState.topStacks != null) {
            renderItems(matrices, renderState.topStacks, queue, state.lightmapCoordinates, renderState.seed);
        }

        matrices.pop();
    }

    private void renderItems(MatrixStack matrices, DefaultedList<ItemStack> inv, OrderedRenderCommandQueue queue, int light, int seed) {
        int counter = 0;
        for (int j = 0; j < 3; j++) {
            renderItem(0.55, 0.3 + (j * 0.5), 0.7, inv, counter, matrices, queue, light, seed);
            counter++;
        }
        for (int j = 0; j < 3; j++) {
            renderItem(1.4, 0.3 + (j * 0.5), 0.7, inv, counter, matrices, queue, light, seed);
            counter++;
        }
        for (int j = 0; j < 3; j++) {
            renderItem(0.55, 0.3 + (j * 0.5), 1.4, inv, counter, matrices, queue, light, seed);
            counter++;
        }
        for (int j = 0; j < 3; j++) {
            renderItem(1.4, 0.3 + (j * 0.5), 1.4, inv, counter, matrices, queue, light, seed);
            counter++;
        }
    }

    private void renderItem(double x, double y, double z, DefaultedList<ItemStack> inv, int counter, MatrixStack matrices, OrderedRenderCommandQueue queue, int light, int seed) {
        matrices.push();
        ItemStack item = inv.get(counter);
        if (item.isEmpty()) {
            matrices.pop();
            return;
        }
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.translate(x, y, z);
        World world = MinecraftClient.getInstance().world;
        float tickDelta = MinecraftClient.getInstance().getRenderTickCounter().getDynamicDeltaTicks();
        long time = world != null ? world.getTime() : 0L;
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time + tickDelta));
        if (world != null) {
            ItemRenderState itemRenderState = new ItemRenderState();
            MinecraftClient.getInstance().getItemModelManager().clearAndUpdate(itemRenderState, item, ItemDisplayContext.GROUND, world, null, seed);
            itemRenderState.render(matrices, queue, light, OverlayTexture.DEFAULT_UV, 0);
        }
        matrices.pop();
    }


    private static final class GenericChestRenderState extends ChestBlockEntityRenderState {
        private SpriteIdentifier textureId;
        private boolean renderItems;
        private int seed;
        @Nullable
        private DefaultedList<ItemStack> topStacks;
    }

    private static final class ChestModel extends net.minecraft.client.model.Model<ChestBlockEntityRenderState> {
        private final ModelPart lid;
        private final ModelPart lock;

        private ChestModel(ModelPart root) {
            super(root, id -> TexturedRenderLayers.getChest());
            this.lid = root.getChild("lid");
            this.lock = root.getChild("lock");
        }

        @Override
        public void setAngles(ChestBlockEntityRenderState state) {
            this.resetTransforms();
            float openFactor = 1.0F - state.lidAnimationProgress;
            openFactor = 1.0F - openFactor * openFactor * openFactor;
            this.lid.pitch = -openFactor * 1.5707964F;
            this.lock.pitch = this.lid.pitch;
        }
    }
}
