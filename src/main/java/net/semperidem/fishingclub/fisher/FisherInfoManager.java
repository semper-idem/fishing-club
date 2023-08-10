package net.semperidem.fishingclub.fisher;

import net.minecraft.server.network.ServerPlayerEntity;

public class FisherInfoManager {
    public static FisherInfo getFisher(ServerPlayerEntity user){
        return new FisherInfo(user);
    }

    public static void addExperience(ServerPlayerEntity playerEntity, int expGained){
        getFisher(playerEntity).grantExperience(expGained);
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
