package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
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
            client.execute(() -> client.setScreen(new FishGameScreen(Text.empty(), fishingRod, fish, boatFishing)));
        });

        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_F_DATA_SEND, (client, handler, buf, responseSender) -> {
            NbtCompound fisherTag = buf.readNbt();
            client.execute( () -> {
                if (fisherTag == null) return;
                FishingClubClient.CLIENT_INFO.fromNbt(fisherTag);
                FishingClubClient.CLIENT_INFO.setClientEntity(client);
            });
        });
    }
}
