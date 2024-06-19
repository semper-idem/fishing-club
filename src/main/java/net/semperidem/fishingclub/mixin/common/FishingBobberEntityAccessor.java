package net.semperidem.fishingclub.mixin.common;


import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FishingBobberEntity.class)
public interface FishingBobberEntityAccessor {
    @Invoker("updateHookedEntityId")
    void invokeUpdateHookedEntityId(Entity entity);
}
