package net.semperidem.fishingclub.leaderboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fisher.FishingCard;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LeaderboardManager {
    private static final String FILE_NAME = "fishing-club-leaderboards.json";
    public static final String LFC = "Longest Fish Caught";
    private static Map<String, Leaderboard> leaderboards = new HashMap<>();

    public static void initLeaderboards() {
        leaderboards.put(LFC, new Leaderboard(LFC));
    }

    public static Leaderboard getLeaderboard(String name) {
        return leaderboards.get(name);
    }

    public static void writePacket(PacketByteBuf packet) {
        packet.writeInt(leaderboards.size());
        for(Leaderboard leaderboard : leaderboards.values()) {
            Leaderboard.writeToPacket(packet, leaderboard);
        }
    }

    public static void readPacket(PacketByteBuf packet) {
        int leaderboardCount = packet.readInt();
        for(int i = 0; i < leaderboardCount; i++) {
            Leaderboard leaderboard = Leaderboard.fromPacket(packet);
            leaderboards.put(leaderboard.getName(), leaderboard);
        }
    }

    private static void record(String name, UUID holder, String holderName, double value) {
        if (leaderboards.containsKey(name)) {
            leaderboards.get(name).consume(holder, holderName, value);
        }
    }

    public static void record(PlayerEntity caughtBy, Fish fish) {
        record(LFC, caughtBy.getUuid(), caughtBy.getName().getString(), fish.length);
    }

    public static void loadLeaderboards(MinecraftServer server) {
        initLeaderboards();
        try {
            //TODO Possible performance hit on game load
            Gson gson = new GsonBuilder().registerTypeAdapter(Leaderboard.class, new LeaderboardInstanceCreator()).create();
            FileReader fileReader = new FileReader(getStoragePath(server));
            JsonReader jsonReader = new JsonReader(fileReader);
            HashMap<String, String> leaderboardsJSON = gson.fromJson(jsonReader, HashMap.class);
            for(String key : leaderboardsJSON.keySet()) {
                leaderboards.put(key, gson.fromJson(gson.toJson(leaderboardsJSON.get(key)), Leaderboard.class));//workaround for type erasure
            }
            fileReader.close();
            jsonReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getStoragePath(MinecraftServer server) {
        String saveRootPath = server.getSavePath(WorldSavePath.ROOT).toString();
        return saveRootPath.substring(0, saveRootPath.lastIndexOf(".")) + FILE_NAME;
    }

    public static void saveLeaderboards(MinecraftServer server) {
        try {
            Gson gson = new Gson();
            FileWriter fileWriter = new FileWriter(getStoragePath(server));
            gson.toJson(leaderboards, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
