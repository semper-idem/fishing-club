package net.semperidem.fishingclub;

import net.fabricmc.api.ClientModInitializer;
import net.semperidem.fishingclub.fish.fishingskill.FishingSkill;


public class FishingClubClient implements ClientModInitializer {
    public static FishingSkill clientFishingSkill = new FishingSkill();

    @Override
    public void onInitializeClient() {
    }
}
