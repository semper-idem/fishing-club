package net.semperidem.fishingclub.mixin.client.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.SalmonEntityRenderer;
import net.minecraft.client.render.entity.model.SalmonEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.specimen.SpecimenComponent;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SalmonEntityRenderer.class)
public class SalmonEntityRendererMixin extends MobEntityRenderer<SalmonEntity, SalmonEntityModel<SalmonEntity>> {


    @Unique
    private static final Identifier TEXTURE = FishingClub.identifier("textures/entity/fish/salmon/salmon.png");
    @Unique
    private static final Identifier ALTERNATIVE_TEXTURE = FishingClub.identifier("textures/entity/fish/salmon/salmon_albino.png");

    public SalmonEntityRendererMixin(EntityRendererFactory.Context context, SalmonEntityModel<SalmonEntity> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Unique
    public void renderWithSpecimenData(
            SpecimenData specimenData,
            SalmonEntity livingEntity,
            float entityYaw,
            float partialTicks,
            MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumerProvider,
            int light
    ) {

        matrixStack.push();
        float weightRating = specimenData.weightScale();
        matrixStack.scale(weightRating, weightRating, specimenData.lengthScale());
        super.render(livingEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, light);
        matrixStack.pop();

    }
    @Override
    public void render(
            SalmonEntity livingEntity,
            float entityYaw,
            float partialTicks,
            MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumerProvider,
            int light
    ) {

        SpecimenData specimenData = SpecimenComponent.of(livingEntity).get();
        if (specimenData != null) {
            this.renderWithSpecimenData(specimenData, livingEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, light);
            return;
        }
        super.render(livingEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, light);
    }


    @Override
    public Identifier getTexture(SalmonEntity entity) {
        SpecimenData data = SpecimenComponent.of(entity).get();
        return data == null ? TEXTURE : SpecimenData.isAlbino(data) ? ALTERNATIVE_TEXTURE : TEXTURE;
    }
}
