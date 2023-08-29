package net.semperidem.fishingclub.fisher;

import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;

public class FisherInfoManager {
    public static FisherInfo getFisher(ServerPlayerEntity user){
        return new FisherInfo(user);
    }

    public static void addExperience(ServerPlayerEntity playerEntity, int expGained){
        getFisher(playerEntity).grantExperience(expGained);
    }

    public static void fishCaught(ServerPlayerEntity playerEntity, int expGained){
        FisherInfo fisherInfo = getFisher(playerEntity);
        if (playerEntity.hasStatusEffect(FStatusEffectRegistry.EXP_BUFF)) {
            float multiplier = (float) (1 + 0.1 * (playerEntity.getStatusEffect(FStatusEffectRegistry.EXP_BUFF).getAmplifier() + 1));
            expGained *= multiplier;
        }
        fisherInfo.fishCaught(expGained);
    }

    public static void addPerk(ServerPlayerEntity playerEntity, String perkName) {
        getFisher(playerEntity).addPerk(perkName);
    }

    public static boolean addCredit(ServerPlayerEntity playerEntity, int credit){
        return getFisher(playerEntity).addCredit(credit);
    }

    public static void setSkillPoint(ServerPlayerEntity playerEntity, int count){
        getFisher(playerEntity).setSkillPoints(count);
    }

    public static void removePerk(ServerPlayerEntity playerEntity, String perkName){
        getFisher(playerEntity).removePerk(perkName);
    }
}
