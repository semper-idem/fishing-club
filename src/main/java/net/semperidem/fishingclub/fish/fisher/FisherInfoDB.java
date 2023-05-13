package net.semperidem.fishingclub.fish.fisher;

import net.minecraft.server.MinecraftServer;
import net.semperidem.fishingclub.network.ServerPacketSender;

import java.util.HashMap;
import java.util.UUID;

//Hash map is kind of database no :D?
public class FisherInfoDB {
    private static final HashMap<UUID, FisherInfo> SERVER_FISHER_INFO = new HashMap<>();

    private static MinecraftServer linkedServer;

    public static void linkServer(MinecraftServer srv){
        linkedServer = srv;
    }

    public static boolean contains(UUID key) {
        return SERVER_FISHER_INFO.containsKey(key);
    }

    public static void put(UUID key, FisherInfo value) {
        SERVER_FISHER_INFO.put(key, value);
        ServerPacketSender.sendFisherInfoSyncPacket(
                linkedServer.getPlayerManager().getPlayer(key),
                FisherInfos.getFisherInfoBuf(value)
        );
    }

    public static FisherInfo get(UUID key) {
        return SERVER_FISHER_INFO.get(key);
    }
}
