package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.*;
import net.semperidem.fishingclub.client.FishingClubClient;
import net.semperidem.fishingclub.client.screen.FishGameScreen;
import net.semperidem.fishingclub.fisher.FishingCardSerializer;
import net.semperidem.fishingclub.game.HookedFish;

public class ClientPacketReceiver {
    public static void registerClientPacketHandlers() {

        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_F_GAME_START, (client, handler, buf, responseSender) -> {
            HookedFish hookedFish = HookedFish.fromPacket(buf);
            client.execute(() -> client.setScreen(new FishGameScreen(hookedFish)));
        });

        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_F_DATA_SEND, (client, handler, buf, responseSender) -> {
            NbtCompound fisherTag = buf.readNbt();
            client.execute( () -> {
                if (fisherTag == null) return;
                FishingCardSerializer.updateFromNbt(FishingClubClient.CLIENT_INFO, fisherTag);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_SUMMON_REQUEST, (client, handler, buf, responseSender) -> {
            String target =  buf.readString();
            client.execute( () -> {
               client.player.sendMessage(Text.of("Your friend:" + target + " send you summon request, You have 30s to accept"), false);
               client.player.sendMessage(MutableText.of(new LiteralTextContent("Accept?")).setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fishing-club summon_accept"))), false);
            });
        });
    }
}
