package net.semperidem.fishing_club.entity.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.semperidem.fishing_club.entity.FCFishEntity;
import net.semperidem.fishing_club.entity.renderer.model.FCFishEntityModel;
import net.semperidem.fishing_club.fish.FishComponent;

import static net.semperidem.fishing_club.registry.FCModels.DEFAULT_MODEL_LAYER;

public class FCFishEntityRenderer extends MobEntityRenderer<FCFishEntity, FCFishEntityModel<FCFishEntity>> {



    public FCFishEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FCFishEntityModel<>(context.getPart(DEFAULT_MODEL_LAYER)), 0.3F);
    }

    @Override
    public void render(FCFishEntity fishEntity, float entityYaw, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {

        FishComponent fishComponent = FishComponent.of(fishEntity);
        this.model = fishComponent.model();


        matrixStack.push();
        float lengthRating = fishComponent.lengthScale();
        float weightRating = fishComponent.weightScale();
        matrixStack.scale(weightRating, weightRating, lengthRating);
        super.render(fishEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, light);
        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(FCFishEntity fishEntity) {
        return FishComponent.of(fishEntity).texture();
    }

    protected void setupTransforms(FCFishEntity fishEntity, MatrixStack matrixStack, float f, float g, float h, float i) {
        super.setupTransforms(fishEntity, matrixStack, f, g, h, i);
        float j = 4.3F * MathHelper.sin(0.6F * f);;
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
        if (!fishEntity.isTouchingWater()) {
            matrixStack.translate(0.1F, 0.1F, -0.1F);
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
        }
    }
}
