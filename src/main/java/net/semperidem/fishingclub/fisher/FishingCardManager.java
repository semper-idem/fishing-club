package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.FishingDatabase;

public class FishingCardManager {
    public static FishingCard getPlayerCard(PlayerEntity user){
        return FishingDatabase.getCard(user.getUuid());
    }

    public static void addExperience(ServerPlayerEntity playerEntity, int expGained){
        getPlayerCard(playerEntity).grantExperience(expGained);
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
