package net.semperidem.fishingclub;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.fish.fishingskill.FishingSkill;


public class FishingClubClient implements ClientModInitializer {
    public static FishingSkill clientFishingSkill = new FishingSkill( );
    public static final Identifier C2S_GRANT_EXP_ID = new Identifier("fishing-club", "c2s_grant_exp");
    public static final Identifier C2S_REQUEST_DATA_SYNC_ID = new Identifier("fishing-club", "c2s_request_data_sync");
    public static final Identifier S2C_SYNC_DATA_ID = new Identifier("fishing-club", "s2c_sync_data");
    public static final Identifier S2C_START_GAME = new Identifier("fishing-club", "s2c_start_game");

    @Override
    public void onInitializeClient() {
    }
}
