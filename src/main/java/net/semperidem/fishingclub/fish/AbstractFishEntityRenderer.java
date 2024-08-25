package net.semperidem.fishingclub.fish;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.fish.specimen.SpecimenComponent;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;


public abstract class AbstractFishEntityRenderer<M extends AbstractFishEntity, T extends EntityModel<M>> extends MobEntityRenderer<M, T> {

	public AbstractFishEntityRenderer(EntityRendererFactory.Context context, T entityModel, float f) {
		super(context, entityModel, f);
	}

	public abstract Identifier getTexture();
	public abstract Identifier getAlternateTexture();
	public Identifier getTexture(M entity) {
		SpecimenData data = SpecimenComponent.of(entity).get();
		return data == null ? getTexture() : data.isAlbino() ? getAlternateTexture() : getTexture();

	}
}
