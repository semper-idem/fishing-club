package net.semperidem.fishingclub.mixin.common;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends VehicleEntity {

	@Unique
	float angleBoost = 0;


	public BoatEntityMixin(EntityType<?> entityType, World world) {
		super(entityType, world);
	}

//	@WrapOperation(
//		method = "updatePaddles",
//		at = @At(
//			value = "INVOKE",
//			target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;"
//		)
//	)
//	private Vec3d add(Vec3d instance, double x, double y, double z, Operation<Vec3d> original) {
//		return original.call(instance, x * (0.8 + angleBoost), y, z * (0.8 + angleBoost));
//	}
//
//	@Inject(
//		method = "clampPassengerYaw",
//		at = @At("HEAD"),
//		cancellable = true
//	)
//	protected void fishingclub$clampPassengerYaw(Entity passenger, CallbackInfo ci) {
//
//		passenger.setBodyYaw(this.getYaw());
//		float f = MathHelper.wrapDegrees(passenger.getYaw() - this.getYaw());
//		float g = MathHelper.clamp(f, -180.0F, 180.0F);
//		if (Math.abs(f) > 105) {
//			passenger.setBodyYaw(this.getYaw() + 180);
//		}
//		passenger.lastYaw += g - f;
//		passenger.setYaw((passenger.getYaw() + g - f) % 360);
//		passenger.setHeadYaw(passenger.getYaw() % 360);
//		this.angleBoost = Math.abs(f / 180f) * 0.4f;
//		ci.cancel();
//	}

}
