package net.semperidem.fishingclub.mixin.common;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.fish.specimen.SpecimenComponent;
import net.semperidem.fishingclub.item.FishingNetItem;
import net.semperidem.fishingclub.world.ChunkQuality;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(WaterCreatureEntity.class)
public class WaterCreatureEntityMixin extends PathAwareEntity {


    protected WaterCreatureEntityMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @SuppressWarnings("All")
    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.getWorld().isClient) {
            return ActionResult.PASS;
        }
        ItemStack stackInHand = player.getStackInHand(Hand.MAIN_HAND);
        if (!(stackInHand.getItem() instanceof FishingNetItem fishingNetItem)) {
            return ActionResult.PASS;
        }
        SpecimenComponent fishComponent = SpecimenComponent.of((WaterCreatureEntity) (Object)this);
        if (fishComponent == null) {
            return ActionResult.PASS;
        }
        if (fishingNetItem.insertStack(stackInHand, FishUtil.getStackFromFish(fishComponent.get()), player)) {
            this.discard();
        }
        return ActionResult.SUCCESS;
    }

    @Inject(
            method = "canSpawn(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)Z",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void fishing_club$canSpawn(
            EntityType<? extends WaterCreatureEntity> type,
            WorldAccess world,
            SpawnReason reason,
            BlockPos pos,
            Random random,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (type == EntityType.DOLPHIN) {
            return;
        }

        if (ChunkQuality.isAboveMinimumQuality(Species.Library.fromName(type.getUntranslatedName()), world, pos)) {
            return;
        }
        cir.setReturnValue(false);
    }
}
