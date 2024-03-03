package net.semperidem.fishingclub.entity.renderer;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.renderer.model.FishermanEntityModel;
import net.semperidem.fishingclub.registry.EntityTypeRegistry;

public class FishermanEntityRenderer extends MobEntityRenderer<FishermanEntity, FishermanEntityModel<FishermanEntity>> {
    private static final Identifier TEXTURE = new Identifier(FishingClub.MOD_ID, "textures/entity/fisherman.png");

    public FishermanEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new FishermanEntityModel<>(context.getPart(EntityTypeRegistry.MODEL_FISHERMAN_LAYER)), 0.5F);
    }


    @Override
    public Identifier getTexture(FishermanEntity entity) {
        return TEXTURE;
    }

}
