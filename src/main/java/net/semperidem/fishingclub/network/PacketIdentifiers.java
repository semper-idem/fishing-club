package net.semperidem.fishingclub.network;

import net.minecraft.util.Identifier;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class PacketIdentifiers {
    public static final Identifier C2S_F_DATA_PERK_ADD = new Identifier(MOD_ID, "c2s_f_data_perk_add");
    public static final Identifier S2C_F_SYNC_INFO = new Identifier(MOD_ID, "s2c_f_sync_info");
    public static final Identifier S2C_F_GAME_START = new Identifier(MOD_ID, "s2c_f_game_start");
    public static final Identifier C2S_F_GAME_WON = new Identifier(MOD_ID, "c2s_f_game_won");
    public static final Identifier C2S_F_SHOP_OPEN = new Identifier(MOD_ID, "c2s_f_shop_open");
    public static final Identifier C2S_F_INFO_OPEN = new Identifier(MOD_ID, "c2s_f_info_open");
    public static final Identifier C2S_F_SHOP_SELL = new Identifier(MOD_ID, "c2s_f_shop_sell");
    public static final Identifier C2S_F_SHOP_BUY = new Identifier(MOD_ID, "c2s_f_shop_buy");

}
