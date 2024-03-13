package net.semperidem.fishingclub.network;

import net.minecraft.util.Identifier;

import static net.semperidem.fishingclub.FishingClub.getIdentifier;

public class PacketIdentifiers {
    public static final Identifier C2S_F_DATA_PERK_ADD = getIdentifier("c2s_f_data_perk_add");
    public static final Identifier C2S_REPAIR_ROD = getIdentifier("c2s_repair_rod");
    public static final Identifier C2S_ACCEPT_DEREK = getIdentifier("c2s_accept_derek");
    public static final Identifier C2S_REFUSE_DEREK = getIdentifier("c2s_refuse_derek");
    public static final Identifier C2S_F_GAME_WON = getIdentifier("c2s_f_game_won");
    public static final Identifier C2S_F_GAME_LOST = getIdentifier("c2s_f_game_lost");
    public static final Identifier C2S_OPEN_MEMBER_SCREEN = getIdentifier("c2s_open_member_screen");
    public static final Identifier C2S_F_INFO_OPEN = getIdentifier("c2s_f_info_open");
    public static final Identifier C2S_F_SHOP_SELL = getIdentifier("c2s_f_shop_sell");
    public static final Identifier C2S_F_SHOP_BUY = getIdentifier("c2s_f_shop_buy");
    public static final Identifier C2S_CAST_SPELL = getIdentifier("c2s_cast_spell");
    public static final Identifier C2S_F_SLOT_SELL = getIdentifier("c2s_f_slot_sell");
    public static final Identifier C2S_SUMMON_ACCEPT = getIdentifier("c2s_summon_accept");
    public static final Identifier C2S_BOBBER_MOVEMENT = getIdentifier("c2s_bobber_movement");
    public static final Identifier S2C_FISH_GAME_INITIAL = getIdentifier("s2c_fish_game_initial");
    public static final Identifier S2C_FISH_GAME_UPDATE = getIdentifier("s2c_fish_game_update");
    public static final Identifier C2S_TOSS_COIN = getIdentifier("c2s_toss_coin");
    public static final Identifier S2C_TOSS_RESULT = getIdentifier("s2s_toss_result");
    public static final Identifier S2C_UPDATE_CARD = getIdentifier("s2c_update_card");
    public static final Identifier C2S_RESET_PERKS = getIdentifier("c2s_reset_perks");
    public static final Identifier C2S_CLAIM_CAPE = getIdentifier("c2s_claim_cape");
    public static final Identifier C2S_GET_CAPE_DETAILS = getIdentifier("c2s_get_cape_details");
    public static final Identifier S2C_SET_CAPE_DETAILS = getIdentifier("s2c_set_cape_details");

}
