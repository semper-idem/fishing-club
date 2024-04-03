package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;

public class ClientPacketSender {
    public static void sendFishGameWon(ArrayList<ItemStack> treasureRewards) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(treasureRewards.size());
        for(ItemStack reward : treasureRewards) {
            buf.writeItemStack(reward);
        }
        ClientPlayNetworking.send(PacketIdentifiers.C2S_F_GAME_WON, buf);
    }

    public static void sendCoinTossRequest(int amount, String playerChoice) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(amount);
        buf.writeString(playerChoice);
        ClientPlayNetworking.send(PacketIdentifiers.C2S_TOSS_COIN, buf);
    }
    public static void sendFishGameLost(){
        ClientPlayNetworking.send(PacketIdentifiers.C2S_F_GAME_LOST, PacketByteBufs.empty());
    }

    public static void acceptDerekOffer(){
        ClientPlayNetworking.send(PacketIdentifiers.C2S_ACCEPT_DEREK, PacketByteBufs.empty());
    }

    public static void refuseDerekOffer(){
        ClientPlayNetworking.send(PacketIdentifiers.C2S_REFUSE_DEREK, PacketByteBufs.empty());
    }

    public static void sendFishingRodRepairRequest(){
        ClientPlayNetworking.send(PacketIdentifiers.C2S_REPAIR_ROD, PacketByteBufs.empty());
    }

    public static void sendOpenSellShopRequest() {
        ClientPlayNetworking.send(PacketIdentifiers.C2S_OPEN_MEMBER_SCREEN, PacketByteBufs.empty());
    }
    public static void sendOpenFisherInfoScreen() {
        ClientPlayNetworking.send(PacketIdentifiers.C2S_F_INFO_OPEN, PacketByteBufs.empty());
    }
    public static void sellShopContainer(int containerValue) {
        if (containerValue <= 0 ) return;
        PacketByteBuf  buf = PacketByteBufs.create();
        buf.writeInt(containerValue);
        ClientPlayNetworking.send(PacketIdentifiers.C2S_F_SHOP_SELL, buf);
    }
    public static void instantSellSlot() {
        ClientPlayNetworking.send(PacketIdentifiers.C2S_F_SLOT_SELL, PacketByteBufs.empty());
    }


    public static void unlockPerk(String perkName){
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(perkName);
        ClientPlayNetworking.send(PacketIdentifiers.C2S_F_DATA_PERK_ADD, buf);
    }


    public static void checkout(ArrayList<ItemStack> cart, int total) {
        PacketByteBuf  buf = PacketByteBufs.create();
        buf.writeInt(total);
        buf.writeInt(cart.size());
        for (ItemStack itemStack : cart) {
            buf.writeItemStack(itemStack);
        }
        ClientPlayNetworking.send(PacketIdentifiers.C2S_F_SHOP_BUY, buf);
    }

    public static void castSpell(String perkName, String uuid){
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(perkName);
        buf.writeString(uuid);
        ClientPlayNetworking.send(PacketIdentifiers.C2S_CAST_SPELL, buf);
    }

    public static void sendSummonAccept(){
        ClientPlayNetworking.send(PacketIdentifiers.C2S_SUMMON_ACCEPT, PacketByteBufs.create());
    }

    public static void sendBobberMovement(float reelForce, boolean isReeling, boolean isPulling) {
        PacketByteBuf movementPacket = PacketByteBufs.create();
        movementPacket.writeFloat(reelForce);
        movementPacket.writeBoolean(isReeling);
        movementPacket.writeBoolean(isPulling);
        ClientPlayNetworking.send(PacketIdentifiers.C2S_BOBBER_MOVEMENT, movementPacket);
    }

    public static void sendResetPerk() {
        ClientPlayNetworking.send(PacketIdentifiers.C2S_RESET_PERKS, PacketByteBufs.empty());
    }

    public static void sendClaimCape(int claimPrice) {
        PacketByteBuf capeClaimPacket = PacketByteBufs.create();
        capeClaimPacket.writeInt(claimPrice);
        ClientPlayNetworking.send(PacketIdentifiers.C2S_CLAIM_CAPE, capeClaimPacket);
    }

    public static void sendRequestCapeDetails() {
        ClientPlayNetworking.send(PacketIdentifiers.C2S_GET_CAPE_DETAILS, PacketByteBufs.empty());
    }

}
