package net.semperidem.fishingclub.fish.fishingskill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class FishingSkillManager {
    private static final HashMap<UUID, FishingSkill> SERVER_PLAYER_FISHING_SKILLS = new HashMap<>();
    private static final String DELIMITER = ",";

    public static void set(UUID uuid, FishingSkill fishingSkill){
        SERVER_PLAYER_FISHING_SKILLS.put(uuid, fishingSkill);
    }

    public static FishingSkill getPlayerFishingSkill(UUID uuid){
        if (!SERVER_PLAYER_FISHING_SKILLS.containsKey(uuid)) {
            set(uuid, new FishingSkill());
        }
        return SERVER_PLAYER_FISHING_SKILLS.get(uuid);
    }


    public static void grantExperience(UUID uuid, int expGained){
        FishingSkill fishingSkill = SERVER_PLAYER_FISHING_SKILLS.get(uuid);
        fishingSkill.grantExperience(expGained);
        SERVER_PLAYER_FISHING_SKILLS.put(uuid, fishingSkill);
    }

    public static String getStringFromPlayerPerks(ArrayList<FishingPerk> perks){
        StringBuilder sb = new StringBuilder();
        for(FishingPerk perk : perks) {
            sb.append(perk.name).append(DELIMITER);
        }
        return sb.toString();
    }

    public static ArrayList<FishingPerk> getPlayerPerksFromString(String fishingPerksString){
        ArrayList<FishingPerk> fishingPerks = new ArrayList<>();
        String[] fishingPerksStringArray = fishingPerksString.split(DELIMITER);
        for(String fishingPerkName : fishingPerksStringArray) {
            FishingPerks.getPerkFromName(fishingPerkName).ifPresent(fishingPerks::add);
        }
        return fishingPerks;
    }
}
