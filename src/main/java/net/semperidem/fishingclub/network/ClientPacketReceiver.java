package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.client.game.fish.Fish;
import net.semperidem.fishingclub.client.game.fish.FishUtil;
import net.semperidem.fishingclub.client.screen.FishGameScreen;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.fisher.FisherInfos;

public class ClientPacketReceiver {
    public static void registerClientPacketHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_F_DATA_SYNC, (client, handler, buf, responseSender) -> {
            FisherInfo clientFS = FisherInfos.readFisherInfoFromBuf(buf);
            client.execute(() -> FisherInfos.setClientInfo(clientFS));
        });


        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_F_GAME_START, (client, handler, buf, responseSender) -> {
            Fish fish = FishUtil.fishFromPacketBuf(buf);
            ItemStack fishingRood = buf.readItemStack();
            client.execute(() -> client.setScreen(new FishGameScreen(Text.empty(), fishingRood, fish)));
        });
    }
}
