package net.semperidem.fishingclub.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.WanderingTraderEntityRenderer;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;

public class FishermanEntityRenderer extends WanderingTraderEntityRenderer {
    private static final Identifier TEXTURE = new Identifier(FishingClub.MOD_ID, "textures/entity/fisherman.png");

    public FishermanEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }
    @Override
    public Identifier getTexture(WanderingTraderEntity wanderingTraderEntity) {
        return TEXTURE;
    }
}
