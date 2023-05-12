package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fish.fisher.FisherInfo;
import net.semperidem.fishingclub.fish.fisher.FisherInfos;
import net.semperidem.fishingclub.screen.FishingScreen;

public class ClientPacketReceiver {
    public static void registerClientPacketHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_SYNC_DATA_ID, (client, handler, buf, responseSender) -> {
            FisherInfo clientFS = FisherInfos.readFisherInfoFromBuf(buf);
            client.execute(() -> FisherInfos.setClientInfo(clientFS));
        });


        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_START_GAME, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                client.setScreen(new FishingScreen(Text.of("Fish")));
            });
        });
    }
}
