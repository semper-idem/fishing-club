package net.semperidem.fishingclub.entity.renderer;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.SignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.semperidem.fishingclub.block.FishDisplayBlock;
import net.semperidem.fishingclub.entity.FishDisplayBlockEntity;
import net.semperidem.fishingclub.registry.FCModels;

import java.util.Map;

public class FishDisplayEntityRenderer implements BlockEntityRenderer<FishDisplayBlockEntity> {
    private static final String STICK = "stick";
    private static final int GLOWING_BLACK_COLOR = -988212;
    private static final int RENDER_DISTANCE = MathHelper.square(16);
    private static final float SCALE = 0.6666667F;
    private static final Vec3d TEXT_OFFSET = new Vec3d(0.0, 0.33333334F, 0.046666667F);
    private final Map<WoodType, FishDisplayEntityRenderer.SignModel> typeToModel;
    private final TextRenderer textRenderer;
    EntityRenderDispatcher entityRenderDispatcher;
    String speciesName;
    ModelPart modelPart;

    public FishDisplayEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.typeToModel = WoodType.stream()
          .collect(
            ImmutableMap.toImmutableMap(
              signType -> signType, signType -> new FishDisplayEntityRenderer.SignModel(ctx.getLayerModelPart(FCModels.MODEL_FISH_DISPLAY_LAYER))
            )
          );
        this.textRenderer = ctx.getTextRenderer();
        this.entityRenderDispatcher = ctx.getEntityRenderDispatcher();
    }

    public void render(FishDisplayBlockEntity signBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        BlockState blockState = signBlockEntity.getCachedState();
        FishDisplayBlock abstractSignBlock = (FishDisplayBlock)blockState.getBlock();
        WoodType woodType = FishDisplayBlock.getWoodType(abstractSignBlock);
        FishDisplayEntityRenderer.SignModel signModel = this.typeToModel.get(woodType);
        signModel.stick.visible = blockState.getBlock() instanceof SignBlock;
        this.render(signBlockEntity, matrixStack, vertexConsumerProvider, i, j, blockState, abstractSignBlock, woodType, signModel);
    }

    public float getSignScale() {
        return 0.6666667F;
    }

    public float getTextScale() {
        return 0.6666667F;
    }

    void render(
      FishDisplayBlockEntity signBlockEntity,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      int overlay,
      BlockState state,
      FishDisplayBlock block,
      WoodType woodType,
      Model model
    ) {
        matrices.push();
        this.setAngles(matrices, -block.getRotationDegrees(state), state);
        this.renderSign(matrices, vertexConsumers, light, overlay, woodType, model);
        if (signBlockEntity.getWaterCreatureEntity() == null) {
            matrices.pop();
            return;
        }
        //r(entity, 0.0, 0.0, 0.0, 0.0F, tickDelta, matrices, vertexConsumers, light);
        matrices.push();
        matrices.translate(0,0.3, 0.05);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) (Math.PI / 2f)));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotation((float) (Math.PI / 2f)));
        this.entityRenderDispatcher.render(signBlockEntity.getWaterCreatureEntity(), 0,0,0,0, signBlockEntity.getDuration()/3f, matrices, vertexConsumers, light);
        matrices.pop();
        //this.renderText(entity.getPos(), entity.getFrontText(), matrices, vertexConsumers, light, entity.getTextLineHeight(), entity.getMaxTextWidth(), true);
        //this.renderText(entity.getPos(), entity.getBackText(), matrices, vertexConsumers, light, entity.getTextLineHeight(), entity.getMaxTextWidth(), false);
        matrices.pop();
    }

    void setAngles(MatrixStack matrices, float rotationDegrees, BlockState state) {
        matrices.translate(0.5F, 0.75F * this.getSignScale(), 0.5F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotationDegrees));
        if (!(state.getBlock() instanceof SignBlock)) {
            matrices.translate(0.0F, -0.3125F, -0.4375F);
        }
    }

    void renderSign(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, WoodType woodType, Model model) {
        matrices.push();
        float f = this.getSignScale();
        matrices.scale(f, -f, -f);
        SpriteIdentifier spriteIdentifier = this.getTextureId(woodType);
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, model::getLayer);
        this.renderSignModel(matrices, light, overlay, model, vertexConsumer);
        matrices.pop();
    }

    void renderSignModel(MatrixStack matrices, int light, int overlay, Model model, VertexConsumer vertexConsumers) {
        FishDisplayEntityRenderer.SignModel signModel = (FishDisplayEntityRenderer.SignModel)model;
        signModel.root.render(matrices, vertexConsumers, light, overlay);
    }

    SpriteIdentifier getTextureId(WoodType signType) {
        return TexturedRenderLayers.getSignTextureId(signType);
    }

    public static FishDisplayEntityRenderer.SignModel createSignModel(EntityModelLoader entityModelLoader, WoodType type) {
        return new FishDisplayEntityRenderer.SignModel(entityModelLoader.getModelPart(EntityModelLayers.createSign(type)));
    }



    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("sign", ModelPartBuilder.create().uv(0, 0).cuboid(-12.0F, -14.0F, -1.0F, 24.0F, 12.0F, 2.0F), ModelTransform.NONE);
        modelPartData.addChild("stick", ModelPartBuilder.create().uv(0, 14).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 14.0F, 2.0F), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Environment(EnvType.CLIENT)
    public static final class SignModel extends Model {
        public final ModelPart root;
        public final ModelPart stick;

        public SignModel(ModelPart root) {
            super(RenderLayer::getEntityCutoutNoCull);
            this.root = root;
            this.stick = root.getChild("stick");
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
            this.root.render(matrices, vertices, light, overlay, color);
        }
    }
}
