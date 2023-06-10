package net.semperidem.fishingclub.network;

import net.minecraft.util.Identifier;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class PacketIdentifiers {
    public static final Identifier C2S_REQUEST_DATA_SYNC_ID = new Identifier(MOD_ID, "c2s_request_data_sync");
    public static final Identifier C2S_GRANT_REWARD = new Identifier(MOD_ID, "c2s_grant_reward");
    public static final Identifier S2C_SYNC_DATA_ID = new Identifier(MOD_ID, "s2c_sync_data");
    public static final Identifier S2C_START_GAME = new Identifier(MOD_ID, "s2c_start_game");
    public static final Identifier C2S_SELL_FISH = new Identifier(MOD_ID, "c2s_sell_fish");
    public static final Identifier C2S_OPEN_SELL_SHOP = new Identifier(MOD_ID, "c2s_open_sell_shop");
    public static final Identifier C2S_OPEN_BUY_SHOP = new Identifier(MOD_ID, "c2s_open_buy_shop");
}
