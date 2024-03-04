package net.semperidem.fishingclub.entity.renderer;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.renderer.model.FishermanEntityModel;
import net.semperidem.fishingclub.registry.EntityTypeRegistry;

public class FishermanEntityRenderer extends MobEntityRenderer<FishermanEntity, FishermanEntityModel<FishermanEntity>> {
    private static final Identifier TEXTURE = new Identifier(FishingClub.MOD_ID, "textures/entity/fisherman.png");
    private final BoatEntityModel boatEntityModel;
    private final Identifier boatIdentifier = new Identifier("textures/entity/chest_boat/mangrove.png");
    private final ModelPart leftPaddle;
    private final ModelPart rightPaddle;

    public FishermanEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FishermanEntityModel<>(context.getPart(EntityTypeRegistry.MODEL_FISHERMAN_LAYER)), 0.5F);
        EntityModelLayer entityModelLayer = EntityModelLayers.createChestBoat(BoatEntity.Type.MANGROVE);
        boatEntityModel = new BoatEntityModel(context.getPart(entityModelLayer), true);
        this.leftPaddle = boatEntityModel.getParts().get(5);
        this.rightPaddle =  boatEntityModel.getParts().get(6);
    }

    @Override
    public void render(FishermanEntity fishermanEntity, float entityYaw, float partialTicks, MatrixStack matrixStack, net.minecraft.client.render.VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(fishermanEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, i);
        if (!fishermanEntity.isInWater()) {
            return;
        }
        matrixStack.push();
        matrixStack.translate(0.0D, 0.375D, 0.0D);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90 + fishermanEntity.getBodyYaw()));
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(boatEntityModel.getLayer(boatIdentifier));
        setAngles(fishermanEntity, fishermanEntity.limbAngle);
        boatEntityModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!fishermanEntity.isSubmergedInWater()) {
            VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getWaterMask());
            boatEntityModel.getWaterPatch().render(matrixStack, vertexConsumer2, i, OverlayTexture.DEFAULT_UV);
        }
        matrixStack.pop();
    }

    private void setAngles(FishermanEntity entity, float f) {
        setPaddleAngle(entity, 0, this.leftPaddle, f);
        setPaddleAngle(entity, 1, this.rightPaddle, f);

    }

    private void setPaddleAngle(FishermanEntity entity, int sigma, ModelPart part, float angle) {
        float f = entity.interpolatePaddlePhase(angle);
        part.pitch = MathHelper.clampedLerp(-1.0471976F, -0.2617994F, (MathHelper.sin(-f) + 1.0F) / 2.0F);
        part.yaw = MathHelper.clampedLerp(-0.7853982F, 0.7853982F, (MathHelper.sin(-f + 1.0F) + 1.0F) / 2.0F);
        if (sigma == 1) {
            part.yaw = 3.1415927F - part.yaw;
        }
    }

    @Override
    public Identifier getTexture(FishermanEntity entity) {
        return TEXTURE;
    }

}
