package net.semperidem.fishingclub.network;

import net.minecraft.util.Identifier;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class PacketIdentifiers {
    public static final Identifier C2S_F_DATA_SYNC_REQ = new Identifier(MOD_ID, "C2S_F_DATA_SYNC_REQ");
    public static final Identifier S2C_F_DATA_SYNC = new Identifier(MOD_ID, "S2C_F_DATA_SYNC");
    public static final Identifier S2C_F_GAME_START = new Identifier(MOD_ID, "S2C_F_GAME_START");
    public static final Identifier C2S_F_GAME_FINISH = new Identifier(MOD_ID, "C2S_F_GAME_FINISH");
    public static final Identifier C2S_F_SHOP_OPEN = new Identifier(MOD_ID, "C2S_F_SHOP_OPEN");
    public static final Identifier C2S_F_SHOP_SELL = new Identifier(MOD_ID, "C2S_F_SHOP_SELL");
    public static final Identifier C2S_F_SHOP_BUY = new Identifier(MOD_ID, "C2S_F_SHOP_BUY");
}
