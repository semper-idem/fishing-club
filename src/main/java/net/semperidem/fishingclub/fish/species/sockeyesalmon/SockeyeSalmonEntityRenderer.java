package net.semperidem.fishingclub.fish.species.sockeyesalmon;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.CodEntityModel;
import net.minecraft.client.render.entity.model.SalmonEntityModel;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.fish.AbstractFishEntity;
import net.semperidem.fishingclub.fish.AbstractFishEntityRenderer;
import net.semperidem.fishingclub.fish.Species;

public class SockeyeSalmonEntityRenderer <T extends AbstractFishEntity> extends AbstractFishEntityRenderer<T, SalmonEntityModel<T>> {
    private static final Identifier TEXTURE = Species.Library.COD.texture(false);
    private static final Identifier ALBINO_TEXTURE = Species.Library.COD.texture(true);

    private static final float SHADOW_RADIUS = 0.3f;

    public SockeyeSalmonEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new SalmonEntityModel<>(context.getPart(Species.Library.SOCKEYE_SALMON.getLayerId())), SHADOW_RADIUS);
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE;
    }

    @Override
    public Identifier getAlternateTexture() {
        return ALBINO_TEXTURE;
    }
}
