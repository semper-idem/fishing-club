package net.semperidem.fishingclub.entity.renderer;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.renderer.model.FishermanEntityModel;
import net.semperidem.fishingclub.registry.EntityTypeRegistry;

public class FishermanEntityRenderer extends MobEntityRenderer<FishermanEntity, FishermanEntityModel<FishermanEntity>> {
    private static final Identifier TEXTURE = new Identifier(FishingClub.MOD_ID, "textures/entity/fisherman.png");
    private BoatEntityRenderer boatEntityRenderer;
    private BoatEntityModel boatEntityModel;
    private Identifier boatIdentifier = new Identifier("textures/entity/boat/oak.png");
//public BoatEntityRenderer(EntityRendererFactory.Context ctx, boolean chest) {

    public FishermanEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FishermanEntityModel<>(context.getPart(EntityTypeRegistry.MODEL_FISHERMAN_LAYER)), 0.5F);
        boatEntityRenderer = new BoatEntityRenderer(context, true);
        EntityModelLayer entityModelLayer = EntityModelLayers.createBoat(BoatEntity.Type.OAK);
        boatEntityModel = new BoatEntityModel(context.getPart(entityModelLayer), false);
    }

    @Override
    public void render(FishermanEntity fishermanEntity, float entityYaw, float partialTicks, MatrixStack matrixStack, net.minecraft.client.render.VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(fishermanEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, i);

        // Render boat model attached to the Fisherman entity
        matrixStack.push();
        matrixStack.translate(0.0D, 0.1D, 0.0D); // Adjust position as needed
        matrixStack.translate(0.0, 0.375, 0.0);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(boatEntityModel.getLayer(boatIdentifier));
        boatEntityModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(FishermanEntity entity) {
        return TEXTURE;
    }

}
