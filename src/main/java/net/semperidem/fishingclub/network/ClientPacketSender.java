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

    public static void sendFishGameLost(){
        ClientPlayNetworking.send(PacketIdentifiers.C2S_F_GAME_LOST, PacketByteBufs.empty());
    }
    public static void sendFishingRodRepairRequest(){
        ClientPlayNetworking.send(PacketIdentifiers.C2S_REPAIR_ROD, PacketByteBufs.empty());
    }

    public static void sendOpenSellShopRequest() {
        ClientPlayNetworking.send(PacketIdentifiers.C2S_F_SHOP_OPEN, PacketByteBufs.empty());
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


    public static void buyShopContainer(int containerValue, ArrayList<ItemStack> basket) {
        PacketByteBuf  buf = PacketByteBufs.create();
        buf.writeInt(containerValue);
        buf.writeInt(basket.size());
        for (ItemStack itemStack : basket) {
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

}
