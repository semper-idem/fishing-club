package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClubClient;
import net.semperidem.fishingclub.fish.fishingskill.FishingSkill;
import net.semperidem.fishingclub.screen.FishingScreen;

public class ClientPacketReceiver {
    public static void registerClientPacketHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_SYNC_DATA_ID, (client, handler, buf, responseSender) -> {
            int level = buf.readInt();
            int exp = buf.readInt();

            client.execute(() -> {
                FishingClubClient.clientFishingSkill = new FishingSkill(level, exp);
            });
        });


        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_START_GAME, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                client.setScreen(new FishingScreen(Text.of("Fish")));
            });
        });
    }
}
