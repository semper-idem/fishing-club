package net.semperidem.fishingclub.mixin.common;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.world.World;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.registry.FEntityRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTraderEntity.class)
public class WanderingTraderEntityMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(EntityType<? extends WanderingTraderEntity> entityType, World world, CallbackInfo info) {
        if (entityType != FEntityRegistry.FISHERMAN) {
            FishermanEntity fishermanEntity = FEntityRegistry.FISHERMAN.create(world);
            world.spawnEntity(fishermanEntity);
        }
    }

}
