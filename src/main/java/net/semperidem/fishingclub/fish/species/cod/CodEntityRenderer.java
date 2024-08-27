package net.semperidem.fishingclub.fish.species.cod;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.CodEntityModel;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.fish.AbstractFishEntity;
import net.semperidem.fishingclub.fish.AbstractFishEntityRenderer;
import net.semperidem.fishingclub.fish.Species;

public class CodEntityRenderer <T extends AbstractFishEntity> extends AbstractFishEntityRenderer<T, CodEntityModel<T>> {
    private static final Identifier TEXTURE = Species.Library.COD.texture(false);
    private static final Identifier ALBINO_TEXTURE = Species.Library.COD.texture(true);

    private static final float SHADOW_RADIUS = 0.3f;

    public CodEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CodEntityModel<>(context.getPart(Species.Library.COD.getLayerId())), SHADOW_RADIUS);
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
