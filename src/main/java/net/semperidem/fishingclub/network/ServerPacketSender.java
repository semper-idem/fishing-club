package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.game.FishingGameController;

import java.util.UUID;

public class ServerPacketSender {

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

    public static void sendInitialFishingGameData(ServerPlayerEntity player, FishingGameController fishingGameController
    ){
        PacketByteBuf fishMovementPacket = PacketByteBufs.create();
        fishingGameController.writeInitialPacket(fishMovementPacket);
        ServerPlayNetworking.send(player, PacketIdentifiers.S2C_FISH_GAME_INITIAL, fishMovementPacket);
    }

    public static void sendFishingGameData(ServerPlayerEntity player, FishingGameController fishingGameController
    ){
        PacketByteBuf fishMovementPacket = PacketByteBufs.create();
        fishingGameController.writeUpdatePacket(fishMovementPacket);
        ServerPlayNetworking.send(player, PacketIdentifiers.S2C_FISH_GAME_UPDATE, fishMovementPacket);
    }

    public static void sendCardUpdate(ServerPlayerEntity player, FishingCard fishingCard) {
        PacketByteBuf cardPacket = PacketByteBufs.create();
        cardPacket.writeNbt(fishingCard.toNbt());
        ServerPlayNetworking.send(player, PacketIdentifiers.S2C_UPDATE_CARD, cardPacket);
    }
    public static void sendCapeDetails(ServerPlayerEntity player, UUID capeHolderUUID, String capeHolder, int minCapePrice) {
        PacketByteBuf capeDetailsPacket = PacketByteBufs.create();
        capeDetailsPacket.writeUuid(capeHolderUUID);
        capeDetailsPacket.writeString(capeHolder);
        capeDetailsPacket.writeInt(minCapePrice);
        ServerPlayNetworking.send(player, PacketIdentifiers.S2C_SET_CAPE_DETAILS, capeDetailsPacket);
    }
}
