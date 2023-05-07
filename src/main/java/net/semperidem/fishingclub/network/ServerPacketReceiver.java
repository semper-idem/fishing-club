package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.fishingskill.FishingSkillManager;

public class ServerPacketReceiver {
    public static void registerServerPacketHandlers() {
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, FishingClub.C2S_GRANT_EXP_ID, (server1, player, handler1, buf, responseSender) -> {
                int expGained = buf.readInt();

                server1.execute(() -> {
                    FishingSkillManager.grantExperience(player.getUuid(), expGained);
                });
            });
        });


        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(handler, FishingClub.C2S_REQUEST_DATA_SYNC_ID, (server1, player, handler1, buf, responseSender) -> {
                server1.execute(() -> {
                    ServerPacketSender.sendFishingSkillSyncPacket(player);
                });
            });
        });
    }
}
