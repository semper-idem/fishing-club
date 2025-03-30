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
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.semperidem.fishingclub.block.FishDisplayBlock;
import net.semperidem.fishingclub.entity.FishDisplayBlockEntity;
import net.semperidem.fishingclub.registry.EntityModelLayers;

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
              signType -> signType, signType -> new FishDisplayEntityRenderer.SignModel(ctx.getLayerModelPart(EntityModelLayers.FISH_DISPLAY))
            )
          );
        this.textRenderer = ctx.getTextRenderer();
        this.entityRenderDispatcher = ctx.getEntityRenderDispatcher();
    }


    public float getSignScale() {
        return 0.6666667F;
    }

    public float getTextScale() {
        return 0.6666667F;
    }
    public void render(FishDisplayBlockEntity entity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        BlockState state = entity.getCachedState();
        FishDisplayBlock block = (FishDisplayBlock)state.getBlock();
        WoodType woodType = FishDisplayBlock.getWoodType(block);
        FishDisplayEntityRenderer.SignModel model = this.typeToModel.get(woodType);
        model.stick.visible = state.getBlock() instanceof SignBlock;

        matrices.push();
        this.setAngles(matrices, -block.getRotationDegrees(state), state);
        this.renderSign(matrices, vertexConsumers, light, overlay, woodType, model);
        if (entity.getWaterCreatureEntity() == null) {
            matrices.pop();
            return;
        }
        //r(entity, 0.0, 0.0, 0.0, 0.0F, tickDelta, matrices, vertexConsumers, light);
        matrices.push();
        matrices.translate(0,0.3, 0.05);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) (Math.PI / 2f)));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotation((float) (Math.PI / 2f)));
        this.entityRenderDispatcher.render(
                entity.getWaterCreatureEntity(),
                0,
                0,
                0,
                0,
                matrices,
                vertexConsumers,
                light
        );
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
            super(root, RenderLayer::getEntityCutoutNoCull);
            this.root = root;
            this.stick = root.getChild("stick");
        }

    }
}
