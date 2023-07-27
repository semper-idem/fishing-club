package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.client.game.fish.Fish;
import net.semperidem.fishingclub.client.game.fish.FishUtil;
import net.semperidem.fishingclub.client.screen.FishGameScreen;

public class ClientPacketReceiver {
    public static void registerClientPacketHandlers() {

        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_F_GAME_START, (client, handler, buf, responseSender) -> {
            Fish fish = FishUtil.fishFromPacketBuf(buf);
            ItemStack fishingRood = buf.readItemStack();
            client.execute(() -> client.setScreen(new FishGameScreen(Text.empty(), fishingRood, fish)));
        });

        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.S2C_F_SYNC_INFO, (client, handler, buf, responseSender) -> {

            NbtCompound playerTag = client.player.writeNbt(new NbtCompound());
            NbtCompound fisherInfoTag = buf.readNbt();
            playerTag.put("fisher_info", fisherInfoTag);
            client.execute(() -> client.player.readNbt(playerTag));
        });
    }
}
