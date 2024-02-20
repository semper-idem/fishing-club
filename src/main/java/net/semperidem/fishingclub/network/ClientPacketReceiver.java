package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.semperidem.fishingclub.client.screen.game.FishGameScreen;

public class ClientPacketReceiver {
    public static void registerClientPacketHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_FISH_GAME_UPDATE, (client, handler, buf, responseSender) -> {
            PacketByteBuf copy = PacketByteBufs.copy(buf);
            client.execute(() -> {
                if (!(client.currentScreen instanceof FishGameScreen fishGameScreen)) {
                    return;
                }
                fishGameScreen.sync(copy);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_FISH_GAME_INITIAL, (client, handler, buf, responseSender) -> {
            PacketByteBuf copy = PacketByteBufs.copy(buf);
            client.execute(() -> {
                if (!(client.currentScreen instanceof FishGameScreen fishGameScreen)) {
                    return;
                }
                fishGameScreen.syncInit(copy);
            });
        });
    }
}
