package net.semperidem.fishingclub.fish.fishingskill;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.UUID;

public class FishingSkillManager {
    private static final HashMap<UUID, FishingSkill> SERVER_PLAYER_FISHING_SKILLS = new HashMap<>();

    public static void set(UUID uuid, FishingSkill fishingSkill){
        SERVER_PLAYER_FISHING_SKILLS.put(uuid, fishingSkill);
    }

    public static FishingSkill get(UUID uuid){
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
}
