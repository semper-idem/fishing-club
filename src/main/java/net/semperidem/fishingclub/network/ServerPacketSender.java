package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.client.game.fish.Fish;
import net.semperidem.fishingclub.client.game.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FisherInfo;

import static net.semperidem.fishingclub.network.PacketIdentifiers.S2C_F_DATA_SEND;
import static net.semperidem.fishingclub.network.PacketIdentifiers.S2C_F_GAME_START;

public class ServerPacketSender {

    private static void sendPacket(ServerPlayerEntity player, Identifier identifier, PacketByteBuf buf){
        if (player != null && player.networkHandler != null) {
            ServerPlayNetworking.send( player, identifier, buf);
        }
    }

    public static void sendShopScreenInventorySyncPacket(ServerPlayerEntity player){
        player.networkHandler.sendPacket(
                new InventoryS2CPacket(
                        player.playerScreenHandler.syncId,
                        player.playerScreenHandler.getRevision(),
                        player.playerScreenHandler.getStacks(),
                        player.playerScreenHandler.getCursorStack()
                )
        );
    }

    public static void sendFishingStartPacket(ServerPlayerEntity player, ItemStack fishingRod, Fish fish, boolean boatFishing){
        PacketByteBuf fishGameStartPacket =  FishUtil.fishToPacketBuf(fish);
        fishGameStartPacket.writeItemStack(fishingRod);
        fishGameStartPacket.writeBoolean(boatFishing);
        sendPacket(player, S2C_F_GAME_START, fishGameStartPacket);
    }

    public static void sendFisherInfo(ServerPlayerEntity playerEntity, FisherInfo fisherInfo){
        PacketByteBuf packet = PacketByteBufs.create();
        packet.writeNbt(fisherInfo.toNbt());
        sendPacket(playerEntity, S2C_F_DATA_SEND, packet);
    }
    public static void sendFisherInfo(ServerPlayerEntity playerEntity){
        PacketByteBuf packet = PacketByteBufs.create();
        NbtCompound playerCustomNbt = new NbtCompound();
        playerEntity.writeCustomDataToNbt(playerCustomNbt);
        if (!playerCustomNbt.contains(FisherInfo.TAG)) return;
        packet.writeNbt(playerCustomNbt.getCompound(FisherInfo.TAG));
        sendPacket(playerEntity, S2C_F_DATA_SEND, packet);
    }
}
