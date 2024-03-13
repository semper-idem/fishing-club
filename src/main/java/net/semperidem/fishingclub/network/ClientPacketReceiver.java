package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.semperidem.fishingclub.client.screen.game.FishingGameScreen;
import net.semperidem.fishingclub.client.screen.member.MemberScreen;
import net.semperidem.fishingclub.fisher.FishingCard;

public class ClientPacketReceiver {
    public static void registerClientPacketHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_FISH_GAME_UPDATE, (client, handler, buf, responseSender) -> {
            PacketByteBuf copy = PacketByteBufs.copy(buf);
            client.execute(() -> {
                if (!(client.currentScreen instanceof FishingGameScreen fishGameScreen)) {
                    return;
                }
                fishGameScreen.sync(copy);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_FISH_GAME_INITIAL, (client, handler, buf, responseSender) -> {
            PacketByteBuf copy = PacketByteBufs.copy(buf);
            client.execute(() -> {
                if (!(client.currentScreen instanceof FishingGameScreen fishGameScreen)) {
                    return;
                }
                fishGameScreen.syncInit(copy);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_TOSS_RESULT, (client, handler, buf, responseSender) -> {
            String tossResult = buf.readString();
            client.execute(() -> {
                if (!(client.currentScreen instanceof MemberScreen memberScreen)) {
                    return;
                }
                memberScreen.getScreenHandler().addTossResult(tossResult);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_UPDATE_CARD, (client, handler, buf, responseSender) -> {
            FishingCard playerCard = new FishingCard(client.player, buf.readNbt());
            client.execute(() -> {
                if (!(client.currentScreen instanceof MemberScreen memberScreen)) {
                    return;
                }
                memberScreen.getScreenHandler().updateCard(playerCard);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_SET_CAPE_DETAILS, (client, handler, buf, responseSender) -> {
            String capeHolder = buf.readString();
            int minCapePrice = buf.readInt();
            client.execute(() -> {
                if (!(client.currentScreen instanceof MemberScreen memberScreen)) {
                    return;
                }
                memberScreen.getScreenHandler().setCapeHolder(capeHolder, minCapePrice);
            });
        });
    }
}
