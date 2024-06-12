package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.semperidem.fishingclub.client.FishingClubClient;
import net.semperidem.fishingclub.client.screen.game.FishingGameScreen;
import net.semperidem.fishingclub.client.screen.leaderboard.LeaderboardScreen;
import net.semperidem.fishingclub.client.screen.member.MemberScreen;
import net.semperidem.fishingclub.fisher.FishingCard;

import java.util.UUID;

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
                memberScreen.getScreenHandler().addTossEntry(tossResult);
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
            UUID capeHolderUUID = buf.readUuid();
            String capeHolder = buf.readString();
            int minCapePrice = buf.readInt();
            client.execute(() -> {
                FishingClubClient.FISHING_KING_UUID = capeHolderUUID;
                if ((client.currentScreen instanceof MemberScreen memberScreen)) {
                    memberScreen.getScreenHandler().setCapeHolder(capeHolder, minCapePrice);
                }
            });
        });
    }
}
