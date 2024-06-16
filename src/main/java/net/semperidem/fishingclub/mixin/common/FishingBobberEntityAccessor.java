package net.semperidem.fishingclub.mixin.common;


import net.minecraft.entity.Entity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FishingBobberEntity.class)
public interface FishingBobberEntityAccessor {
    @Accessor("HOOK_ENTITY_ID")
    public static TrackedData<Integer> getHookedEntityId() {
        throw new AssertionError();
    }

    @Accessor("hookedEntity")
    public void setHookedEntity(Entity entity);
}
