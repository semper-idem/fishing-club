package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.fisher.FisherInfos;
import net.semperidem.fishingclub.client.screen.FishingScreen;

public class ClientPacketReceiver {
    public static void registerClientPacketHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_F_DATA_SYNC, (client, handler, buf, responseSender) -> {
            FisherInfo clientFS = FisherInfos.readFisherInfoFromBuf(buf);
            client.execute(() -> FisherInfos.setClientInfo(clientFS));
        });


        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_F_GAME_START, (client, handler, buf, responseSender) -> {
            client.execute(() -> client.setScreen(new FishingScreen(Text.of("Fishing..."))));
        });
    }
}
