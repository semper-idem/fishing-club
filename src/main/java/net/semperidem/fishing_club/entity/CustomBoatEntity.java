package net.semperidem.fishing_club.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.world.World;

public class CustomBoatEntity extends BoatEntity {

	public CustomBoatEntity(EntityType<CustomBoatEntity> entityType, World world) {
		super(entityType, world);
	}


	@Override
	public boolean damage(DamageSource source, float amount) {
		return false;
	}

	@Override
	public void pushAwayFrom(Entity entity) {
		if (entity instanceof CustomBoatEntity) {
			return;
		}
		super.pushAwayFrom(entity);
	}

	@Override
	public boolean collidesWith(Entity other) {
		return !(other instanceof CustomBoatEntity) && super.collidesWith(other);
	}

	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return false;
	}

}
