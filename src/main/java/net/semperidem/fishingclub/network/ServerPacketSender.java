package net.semperidem.fishingclub.network;

import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

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

}
