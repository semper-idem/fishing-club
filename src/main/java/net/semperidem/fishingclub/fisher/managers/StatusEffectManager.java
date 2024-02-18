package net.semperidem.fishingclub.fisher.managers;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;

public class StatusEffectManager {
    FishingCard trackedFor;
    PlayerEntity holder;
    public StatusEffectManager(FishingCard trackedFor) {
        this.trackedFor = trackedFor;
        this.holder = trackedFor.getHolder();
    }

    public int getMinGrade() {
        int minGrade = 0;
        if (this.holder.hasStatusEffect(FStatusEffectRegistry.QUALITY_BUFF) && Math.random() > 0.25f) {
            minGrade++;
        } else if (this.holder.hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF)){
            minGrade++;
        }
        return minGrade;
    }
    public void fishCaught(ProgressionManager progressionManager, Fish fish){
        processOneTimeBuff(fish);
        prolongStatusEffects(progressionManager);
    }

    public float getExpMultiplier() {
        StatusEffectInstance xpBuffInstance;
        float multiplier = 1;
        if ((xpBuffInstance = holder.getStatusEffect(FStatusEffectRegistry.EXP_BUFF)) != null) {
             multiplier += 0.1f * (xpBuffInstance.getAmplifier() + 1);
        }
        return multiplier;
    }

    private boolean shouldSpeardQualityBuff(ProgressionManager progressionManager, Fish fish) {
        return
                fish.grade >= 4 &&
                progressionManager.hasPerk(FishingPerks.QUALITY_SHARING) &&
                !holder.hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF) &&
                !fish.consumeGradeBuff;
    }

    public int spreadStatusEffect(ProgressionManager progressionManager, Fish fish) {
        int xpBuffAffectedCount = 0;
        Box box = new Box(holder.getBlockPos());
        box.expand(3);
        boolean spreadQualityBuff = shouldSpeardQualityBuff(progressionManager, fish);
        for(Entity entity : holder.getEntityWorld().getOtherEntities(null, box)) {
            if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                if (FishingCard.getPlayerCard(serverPlayerEntity).hasPerk(FishingPerks.PASSIVE_FISHING_XP)) {
                    xpBuffAffectedCount++;
                }
                if (spreadQualityBuff && !serverPlayerEntity.hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF)) {
                    serverPlayerEntity.addStatusEffect(new StatusEffectInstance(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF, 2400));
                }
            }
        }
        return xpBuffAffectedCount;
    }


    private void prolongStatusEffects(ProgressionManager progressionManager){
        if (!progressionManager.hasPerk(FishingPerks.SHARED_BUFFS)) {
            return;
        }

        if (!this.holder.hasVehicle()) {
            return;
        }

        if (!(this.holder.getVehicle() instanceof BoatEntity boatEntity)) {
            return;
        }

        ImmutableList<Entity> passengers = (ImmutableList<Entity>) boatEntity.getPassengerList();

        for(Entity passenger : passengers) {
            if(!(passenger instanceof PlayerEntity playerPassenger)) continue;
            playerPassenger.getStatusEffects().forEach(
                    sei -> sei.upgrade(
                            new StatusEffectInstance(
                                    sei.getEffectType(),
                                    sei.getDuration() + 200,
                                    sei.getAmplifier()
                            )
                    )
            );
        }

    }

    private void processOneTimeBuff(Fish fish){
        if (!fish.consumeGradeBuff) {
            return;
        }
        if (!trackedFor.getHolder().hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF)) {
            return;
        }
        consumeOneTimeBuff();
    }

    private void consumeOneTimeBuff(){
        StatusEffectInstance sei = holder.getStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF);
        int effectPower = sei.getAmplifier();
        holder.removeStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF);
        if (effectPower > 0) {
            StatusEffectInstance lowerSei = new StatusEffectInstance(sei.getEffectType(), sei.getDuration(), effectPower - 1);
            holder.addStatusEffect(lowerSei);
        }
    }
}
