package net.semperidem.fishingclub.network;

import net.minecraft.util.Identifier;

import static net.semperidem.fishingclub.FishingClub.MODID;

public class PacketIdentifiers {
    public static final Identifier C2S_REQUEST_DATA_SYNC_ID = new Identifier(MODID, "c2s_request_data_sync");
    public static final Identifier C2S_GRANT_REWARD = new Identifier(MODID, "c2s_grant_reward");
    public static final Identifier S2C_SYNC_DATA_ID = new Identifier(MODID, "s2c_sync_data");
    public static final Identifier S2C_START_GAME = new Identifier(MODID, "s2c_start_game");
}
