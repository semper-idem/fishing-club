package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.player.PlayerEntity;

public class FisherInfoManager {
    public static FisherInfo getFisher(PlayerEntity user){
        return new FisherInfo(user);
    }

    public static void addExperience(PlayerEntity playerEntity, int expGained){
        getFisher(playerEntity).grantExperience(expGained);
    }

    public static void addPerk(PlayerEntity playerEntity, String perkName) {
        getFisher(playerEntity).addPerk(perkName);
    }

    public static boolean addCredit(PlayerEntity playerEntity, int credit){
        return getFisher(playerEntity).addCredit(credit);
    }

    public static void setSkillPoint(PlayerEntity playerEntity, int count){
        getFisher(playerEntity).setSkillPoints(count);
    }

    public static void removePerk(PlayerEntity playerEntity, String perkName){
        getFisher(playerEntity).removePerk(perkName);
    }
}
