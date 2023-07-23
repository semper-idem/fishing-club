package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.network.ServerPacketSender;

import java.util.HashMap;
import java.util.UUID;

//Hash map is kind of database no :D?
public class FisherInfoDB {
    private static final HashMap<UUID, FisherInfo> SERVER_FISHER_INFO = new HashMap<>();

    private static MinecraftServer linkedServer;

    public static void linkServer(MinecraftServer srv){
        linkedServer = srv;
        SERVER_FISHER_INFO.clear();
    }

    public static boolean contains(UUID key) {
        return SERVER_FISHER_INFO.containsKey(key);
    }

    public static void put(UUID key, FisherInfo value) {
        value.fisherUUID = key;
        SERVER_FISHER_INFO.put(key, value);
        ServerPacketSender.sendFisherInfoSyncPacket(
                linkedServer.getPlayerManager().getPlayer(key),
                FisherInfos.getFisherInfoBuf(value)
        );
    }

    public static void putDefault(UUID key) {
        FisherInfo value = new FisherInfo(key);
        SERVER_FISHER_INFO.put(key, value);
        ServerPacketSender.sendFisherInfoSyncPacket(
                linkedServer.getPlayerManager().getPlayer(key),
                FisherInfos.getFisherInfoBuf(value)
        );
    }

    public static boolean hasPerk(Entity fisher, FishingPerk perk){
        return get(fisher.getUuid()).hasPerk(perk);
    }

    public static ServerPlayerEntity getPlayer(UUID key){
        return linkedServer.getPlayerManager().getPlayer(key);
    }

    public static FisherInfo get(UUID key) {
        return SERVER_FISHER_INFO.get(key);
    }
}
