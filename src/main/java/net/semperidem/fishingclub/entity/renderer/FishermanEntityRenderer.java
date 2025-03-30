package net.semperidem.fishingclub.entity.renderer;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.renderer.model.FishermanEntityModel;
import net.semperidem.fishingclub.registry.EntityModelLayers;

public class FishermanEntityRenderer extends MobEntityRenderer<FishermanEntity, FishermanEntityRenderState, FishermanEntityModel> {
    private static final Identifier TEXTURE = FishingClub.identifier("textures/entity/fisherman.png");
    private static final Identifier BOAT_TEXTURE = FishingClub.identifier("textures/entity/chest_boat/mangrove.png");

//    private final BoatEntityModel boatModel;

    public FishermanEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FishermanEntityModel(context.getPart(EntityModelLayers.FISHERMAN)), 0.5F);
//        this.boatModel =  new BoatEntityModel(context.getPart(net.minecraft.client.render.entity.model.EntityModelLayers.BOAT));
    }

    @Override
    public FishermanEntityRenderState createRenderState() {
        return new FishermanEntityRenderState();
    }

    @Override
    public void render(FishermanEntityRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        super.render(state, matrixStack, vertexConsumerProvider, light);
//        if (state.getOutOfWaterTicks() > 20) {
//            return;
//        }
//        renderBoat(entity, matrixStack, vertexConsumerProvider, light);
    }

    private void renderBoat(FishermanEntityRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
//        matrixStack.push();
//        matrixStack.translate(0.0F, 0.375F, 0.0F);
//        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - state.yaw));
//
//        matrixStack.scale(-1.0F, -1.0F, 1.0F);
//        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
//        EntityModel<BoatEntityRenderState> entityModel = this.boatModel;
//        entityModel.setAngles(state);
//        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.getRenderLayer());
//        entityModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
//        this.renderWaterMask(state, matrixStack, vertexConsumerProvider, i);
//        matrixStack.pop();
//        super.render(state, matrixStack, vertexConsumerProvider, i);


    }

//    private void setAngles(FishermanEntity entity, float limbPosition) {
//        setPaddleAngle(entity, 0, ((BoatEntityModelAccessor) boatModel).getLeftPaddle(), limbPosition);
//        setPaddleAngle(entity, 1, ((BoatEntityModelAccessor) boatModel).getRightPaddle(), limbPosition);
//    }

//    private void setPaddleAngle(FishermanEntity entity, int sigma, ModelPart paddle, float limbPosition) {
//        float f = entity.interpolatePaddlePhase(limbPosition);
//        paddle.pitch = MathHelper.clampedLerp(-1.0471976F, -0.2617994F, (MathHelper.sin(-f) + 1.0F) / 2.0F);
//        paddle.yaw = MathHelper.clampedLerp(-0.7853982F, 0.7853982F, (MathHelper.sin(-f + 1.0F) + 1.0F) / 2.0F);
//        if (sigma == 1) {
//            paddle.yaw = 3.1415927F - paddle.yaw;
//        }
//    }

    @Override
    public Identifier getTexture(FishermanEntityRenderState state) {
       return TEXTURE;
    }

    public void updateRenderState(FishermanEntity entity, FishermanEntityRenderState state, float delta) {
        super.updateRenderState(entity, state, delta);
        state.yaw = entity.getLerpedYaw(delta);
        state.outOfWaterTicks = entity.getOutOfWaterTicks();
        state.submergedInWater = entity.isSubmergedInWater();
        state.leftPaddleAngle = entity.lerpPaddlePhase(0, delta);
        state.rightPaddleAngle = entity.lerpPaddlePhase(1, delta);
    }
}
