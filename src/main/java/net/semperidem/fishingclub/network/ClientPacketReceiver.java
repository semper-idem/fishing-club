package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.client.game.fish.Fish;
import net.semperidem.fishingclub.client.game.fish.FishUtil;
import net.semperidem.fishingclub.client.screen.FishGameScreen;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.fisher.FisherInfos;
import net.semperidem.fishingclub.item.FishingRodPartItem;
import net.semperidem.fishingclub.item.FishingRodPartItems;

import java.util.HashMap;

public class ClientPacketReceiver {
    public static void registerClientPacketHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_F_DATA_SYNC, (client, handler, buf, responseSender) -> {
            FisherInfo clientFS = FisherInfos.readFisherInfoFromBuf(buf);
            client.execute(() -> FisherInfos.setClientInfo(clientFS));
        });


        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_F_GAME_START, (client, handler, buf, responseSender) -> {
            HashMap<FishingRodPartItem.PartType, ItemStack> rodParts = new HashMap<>();
            Fish fish = FishUtil.fishFromPacketBuf(buf);
            int partCount = buf.readInt();
            for(int i = 0; i < partCount; i++) {
                ItemStack part = buf.readItemStack();
                rodParts.put(((FishingRodPartItem)part.getItem()).getPartType(), part);
            }
            client.execute(() -> client.setScreen(new FishGameScreen(Text.empty(), rodParts, fish)));
        });
    }
}
