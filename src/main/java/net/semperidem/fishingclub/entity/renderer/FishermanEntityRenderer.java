package net.semperidem.fishingclub.entity.renderer;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.ChestBoatEntityModel;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ModelWithWaterPatch;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.renderer.model.FishermanEntityModel;
import net.semperidem.fishingclub.mixin.client.model.BoatEntityModelAccessor;
import net.semperidem.fishingclub.registry.FCModels;

public class FishermanEntityRenderer extends MobEntityRenderer<FishermanEntity, FishermanEntityModel<FishermanEntity>> {
    private static final Identifier TEXTURE = FishingClub.identifier("textures/entity/fisherman.png");
    private static final Identifier BOAT_TEXTURE = FishingClub.identifier("textures/entity/chest_boat/mangrove.png");

    private final CompositeEntityModel<BoatEntity> boatModel;

    public FishermanEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FishermanEntityModel<>(context.getPart(FCModels.MODEL_FISHERMAN_LAYER)), 0.5F);
        this.boatModel =  new ChestBoatEntityModel(context.getPart(EntityModelLayers.createChestBoat(ChestBoatEntity.Type.MANGROVE)));
    }

    @Override
    public void render(FishermanEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        super.render(entity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, light);
        if (entity.getOutOfWaterTicks() > 20) {
            return;
        }
        renderBoat(entity, matrixStack, vertexConsumerProvider, light);
    }

    private void renderBoat(FishermanEntity entity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        matrixStack.push();
        matrixStack.translate(0.0D, 0.375D, 0.0D);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90 + entity.getBodyYaw()));
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(boatModel.getLayer(BOAT_TEXTURE));
        setAngles(entity, entity.limbAnimator.getPos());
        boatModel.render(matrixStack, vertexConsumer,  light, OverlayTexture.DEFAULT_UV);
        if (!entity.isSubmergedInWater() && boatModel instanceof ModelWithWaterPatch modelWithWaterPatch) {
            modelWithWaterPatch.getWaterPatch().render(
                    matrixStack,
                    vertexConsumerProvider.getBuffer(RenderLayer.getWaterMask()), light, OverlayTexture.DEFAULT_UV
            );
        }
        matrixStack.pop();
    }

    private void setAngles(FishermanEntity entity, float limbPosition) {
        setPaddleAngle(entity, 0, ((BoatEntityModelAccessor) boatModel).getLeftPaddle(), limbPosition);
        setPaddleAngle(entity, 1, ((BoatEntityModelAccessor) boatModel).getRightPaddle(), limbPosition);
    }

    private void setPaddleAngle(FishermanEntity entity, int sigma, ModelPart paddle, float limbPosition) {
        float f = entity.interpolatePaddlePhase(limbPosition);
        paddle.pitch = MathHelper.clampedLerp(-1.0471976F, -0.2617994F, (MathHelper.sin(-f) + 1.0F) / 2.0F);
        paddle.yaw = MathHelper.clampedLerp(-0.7853982F, 0.7853982F, (MathHelper.sin(-f + 1.0F) + 1.0F) / 2.0F);
        if (sigma == 1) {
            paddle.yaw = 3.1415927F - paddle.yaw;
        }
    }

    @Override
    public Identifier getTexture(FishermanEntity entity) {
        return TEXTURE;
    }

}
