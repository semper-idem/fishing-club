package net.semperidem.fishingclub.fish.species.butterfish;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import net.semperidem.fishingclub.fish.AbstractFishEntity;
import net.semperidem.fishingclub.fish.AbstractSchoolingFishEntity;

public class ButterfishEntity extends AbstractSchoolingFishEntity {

	public ButterfishEntity(EntityType<? extends AbstractFishEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected SoundEvent getFlopSound() {
		return null;
	}

	@Override
	public ItemStack getBucketItem() {
		return null;
	}

}
