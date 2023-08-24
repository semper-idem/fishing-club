package net.semperidem.fishingclub.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.registry.FEntityRegistry;

public class HarpoonEntityRenderer extends EntityRenderer<HarpoonEntity> {
    public static final Identifier TEXTURE = FishingClub.getIdentifier("textures/entity/harpoon_rod.png");
    private final HarpoonEntityModel model;
    public HarpoonEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new HarpoonEntityModel(context.getPart(FEntityRegistry.MODEL_HARPOON_LAYER));
    }


    @Override
    public void render(HarpoonEntity harpoonEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, harpoonEntity.prevYaw, harpoonEntity.getYaw()) - 90.0f));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, harpoonEntity.prevPitch, harpoonEntity.getPitch()) + 90.0f));
        VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, this.model.getLayer(this.getTexture(harpoonEntity)), false, false);
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(HarpoonEntity tridentEntity) {
        return TEXTURE;
    }
}
