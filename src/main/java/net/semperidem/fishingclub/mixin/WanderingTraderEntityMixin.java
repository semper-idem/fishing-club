package net.semperidem.fishingclub.mixin;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.world.World;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.FishermanEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTraderEntity.class)
public class WanderingTraderEntityMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(EntityType<? extends WanderingTraderEntity> entityType, World world, CallbackInfo info) {
        if (entityType != FishingClub.FISHERMAN_ENTITY) {
            FishermanEntity fishermanEntity = FishingClub.FISHERMAN_ENTITY.create(world);
            world.spawnEntity(fishermanEntity);
        }
    }

}
