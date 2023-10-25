package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.semperidem.fishingclub.client.game.fish.Fish;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;

public class FishingCardManager {
    public static FishingCard getPlayerCard(ServerPlayerEntity user){
        return new FishingCard(user);
    }
    public static FishingCard getPlayerCard(PlayerEntity user){
        return new FishingCard(user);
    }

    public static void addExperience(ServerPlayerEntity playerEntity, int expGained){
        getPlayerCard(playerEntity).grantExperience(expGained);
    }

    public static void fishCaught(ServerPlayerEntity playerEntity, Fish fish){
        int expGained = fish.experience;
        FishingCard fishingCard = getPlayerCard(playerEntity);
        if (playerEntity.hasStatusEffect(FStatusEffectRegistry.EXP_BUFF)) {
            float multiplier = (float) (1 + 0.1 * (playerEntity.getStatusEffect(FStatusEffectRegistry.EXP_BUFF).getAmplifier() + 1));
            expGained *= multiplier;
        }

        Box box = new Box(playerEntity.getBlockPos());
        box.expand(3);
        float passivExpMultiplier = 1;
        for(Entity entity : playerEntity.getEntityWorld().getOtherEntities(null, box)) {
            if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                if (getPlayerCard(serverPlayerEntity).hasPerk(FishingPerks.PASSIVE_FISHING_XP)) {
                    passivExpMultiplier += 0.1;
                }
            }
        }
        expGained *= Math.min(passivExpMultiplier, 2);

        fishingCard.fishCaught(fish, expGained);
    }


    public static void addPerk(ServerPlayerEntity playerEntity, String perkName) {
        getPlayerCard(playerEntity).addPerk(perkName);
    }

    public static boolean addCredit(ServerPlayerEntity playerEntity, int credit){
        return getPlayerCard(playerEntity).addCredit(credit);
    }

    public static void setCredit(ServerPlayerEntity playerEntity, int credit){
        getPlayerCard(playerEntity).setCredit(credit);
    }

    public static void setSkillPoint(ServerPlayerEntity playerEntity, int count){
        getPlayerCard(playerEntity).setSkillPoints(count);
    }
    public static void addSkillPoint(ServerPlayerEntity playerEntity, int count){
        getPlayerCard(playerEntity).addSkillPoints(count);
    }

    public static void removePerk(ServerPlayerEntity playerEntity, String perkName){
        getPlayerCard(playerEntity).removePerk(perkName);
    }
}
