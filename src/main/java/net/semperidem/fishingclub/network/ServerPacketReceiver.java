package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fish.fisher.FisherInfos;

public class ServerPacketReceiver {
    public static void registerServerPacketHandlers() {
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, PacketIdentifiers.C2S_GRANT_REWARD, (server1, player, handler1, buf, responseSender) -> {
                int expGained = buf.readInt();
                float fishWeight = buf.readFloat();
                float fishLength = buf.readFloat();
                String fishName = buf.readString();

                server1.execute(() -> {
                    FisherInfos.grantExperience(player.getUuid(), expGained);
                    player.addExperience(Math.max(1, expGained/10));
                    ItemStack fishReward = FishUtil.prepareFishItemStack(fishName, fishWeight, fishLength);
                    if (player.getInventory().getEmptySlot() == -1) {
                        player.dropItem(fishReward, false);
                    } else {
                        player.giveItemStack(fishReward);
                    }
                });
            });
        });


        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, PacketIdentifiers.C2S_REQUEST_DATA_SYNC_ID, (server1, player, handler1, buf, responseSender) -> {
                server1.execute(() -> {
                    ServerPacketSender.sendFisherInfoSyncPacket(player);
                });
            });
        });
    }
}
