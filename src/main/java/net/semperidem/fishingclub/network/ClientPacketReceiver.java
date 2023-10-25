package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.*;
import net.minecraft.util.math.BlockPos;
import net.semperidem.fishingclub.client.FishingClubClient;
import net.semperidem.fishingclub.client.game.fish.Fish;
import net.semperidem.fishingclub.client.game.fish.FishUtil;
import net.semperidem.fishingclub.client.screen.FishGameScreen;

public class ClientPacketReceiver {
    public static void registerClientPacketHandlers() {

        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_F_GAME_START, (client, handler, buf, responseSender) -> {
            Fish fish = FishUtil.fishFromPacketBuf(buf);
            ItemStack fishingRod = buf.readItemStack();
            boolean boatFishing = buf.readBoolean();
            BlockPos bobberPos = buf.readBlockPos();
            client.execute(() -> client.setScreen(new FishGameScreen(Text.empty(), fishingRod, fish, boatFishing, bobberPos)));
        });

        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_F_DATA_SEND, (client, handler, buf, responseSender) -> {
            NbtCompound fisherTag = buf.readNbt();
            client.execute( () -> {
                if (fisherTag == null) return;
                FishingClubClient.CLIENT_INFO.fromNbt(fisherTag);
                FishingClubClient.CLIENT_INFO.setClientEntity(client);
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
