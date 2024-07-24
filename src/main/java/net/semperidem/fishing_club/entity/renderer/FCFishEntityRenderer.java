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
    public static final Identifier TEXTURE = FishingClub.getIdentifier("textures/entity/fish/fc_fish.png");
    private final static HashMap<String, Pair<FCFishEntityModel<FCFishEntity>, Pair<Identifier, Identifier>>> SPECIES_TO_MODEL_AND_TEXTURE = new HashMap<>();
    private void init(EntityRendererFactory.Context context) {
        if (!SPECIES_TO_MODEL_AND_TEXTURE.isEmpty()) {
            return;
        }
        SPECIES_TO_MODEL_AND_TEXTURE.put(SpeciesLibrary.BUTTERFISH.name, new Pair<>(
          new FCFishEntityModel<>(context.getPart(FCEntityTypes.MODEL_BUTTERFISH_LAYER)),
            new Pair<> (
              FishingClub.getIdentifier("textures/entity/fish/butterfish.png"),
              FishingClub.getIdentifier("textures/entity/fish/butterfish_albino.png")
            )
          )
        );

    }

    public static Pair<FCFishEntityModel<FCFishEntity>, Identifier> getModelAndTexture(FishRecord fishRecord) {
        Pair<FCFishEntityModel<FCFishEntity>, Pair<Identifier, Identifier>> entry = SPECIES_TO_MODEL_AND_TEXTURE.get(fishRecord.speciesName());
        return new Pair<>(entry.getLeft(), fishRecord.isAlbino() ? entry.getRight().getRight() : entry.getRight().getLeft());
    }


    public FCFishEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FCFishEntityModel<>(context.getPart(FCEntityTypes.MODEL_FC_FISH_LAYER)), 0.3F);
        init(context);
    }

    @Override
    public void render(FCFishEntity fishEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        FishComponent fishComponent = FishComponent.of(fishEntity);
        this.model = fishComponent.getModelAndTexture().getLeft();
        float lengthRating = fishComponent.lengthScale();
        float weightRating = fishComponent.weightScale();

        matrixStack.push();
        matrixStack.scale(weightRating, weightRating, lengthRating);
        super.render(fishEntity, f, g, matrixStack, vertexConsumerProvider, i);//new Color(0, 0, 0).getRGB());
        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(FCFishEntity fishEntity) {
        return FishComponent.of(fishEntity).getModelAndTexture().getRight();
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
