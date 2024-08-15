package net.semperidem.fishing_club.entity.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.semperidem.fishing_club.fish.AbstractFishEntity;
import net.semperidem.fishing_club.fish.specimen.SpecimenComponent;
import net.semperidem.fishing_club.fish.specimen.SpecimenData;

public abstract class AbstractFishEntityRenderer<M extends EntityModel<FishEntity>> extends MobEntityRenderer<FishEntity, M> {

    public AbstractFishEntityRenderer(EntityRendererFactory.Context context, M entityModel, float shadowRadius) {
        super(context, entityModel, shadowRadius);
    }

    @Override
    public void render(FishEntity fishEntity, float entityYaw, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {

        SpecimenData specimenData = SpecimenComponent.of(fishEntity).get();

        matrixStack.push();
        float weightRating = specimenData.weightScale();
        matrixStack.scale(weightRating, weightRating, specimenData.lengthScale());
        super.render(fishEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, light);
        matrixStack.pop();
    }

    protected void setupTransforms(AbstractFishEntity fishEntity, MatrixStack matrixStack, float f, float g, float h, float i) {
        super.setupTransforms(fishEntity, matrixStack, f, g, h, i);
        float j = 4.3F * MathHelper.sin(0.6F * f);;
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
        if (!fishEntity.isTouchingWater()) {
            matrixStack.translate(0.1F, 0.1F, -0.1F);
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
        }
    }
}
