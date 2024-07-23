package net.semperidem.fishing_club.entity.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.entity.FCFishEntity;
import net.semperidem.fishing_club.entity.renderer.model.FCFishEntityModel;
import net.semperidem.fishing_club.fish.FishComponent;
import net.semperidem.fishing_club.fish.FishRecord;
import net.semperidem.fishing_club.fish.SpeciesLibrary;
import net.semperidem.fishing_club.registry.FCEntityTypes;

import java.util.HashMap;

public class FCFishEntityRenderer extends MobEntityRenderer<FCFishEntity, FCFishEntityModel<FCFishEntity>> {
    private static final Identifier TEXTURE = FishingClub.getIdentifier("textures/entity/fish/fc_fish.png");
    private final HashMap<String, Pair<FCFishEntityModel<FCFishEntity>, Identifier>> speciesToTexture = new HashMap<>();
    private void init(EntityRendererFactory.Context context) {
        speciesToTexture.put(SpeciesLibrary.BUTTERFISH.name, new Pair<>(
          new FCFishEntityModel<>(context.getPart(FCEntityTypes.MODEL_BUTTERFISH_LAYER)), FishingClub.getIdentifier("textures/entity/fish/butterfish.png")));

    }

    public FCFishEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FCFishEntityModel<>(context.getPart(FCEntityTypes.MODEL_FC_FISH_LAYER)), 0.3F);
        init(context);
    }

    @Override
    public void render(FCFishEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (!(FishComponent.of(livingEntity).record() instanceof FishRecord fishRecord)) {
            return;
        }

        if (speciesToTexture.get(fishRecord.speciesName()) instanceof Pair<FCFishEntityModel<FCFishEntity>, Identifier> pair) {
            this.model = pair.getLeft();
        }
        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(FCFishEntity entity) {
        if (!(FishComponent.of(entity).record() instanceof FishRecord fishRecord)) {
            return TEXTURE;
        }
        return speciesToTexture.getOrDefault(fishRecord.speciesName(), new Pair<>(null, TEXTURE)).getRight();
    }


    protected void setupTransforms(FCFishEntity fishEntity, MatrixStack matrixStack, float f, float g, float h, float i) {
        super.setupTransforms(fishEntity, matrixStack, f, g, h, i);
        float j = 4.3F * MathHelper.sin(0.6F * f);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
        if (!fishEntity.isTouchingWater()) {
            matrixStack.translate(0.1F, 0.1F, -0.1F);
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
        }
    }
}
