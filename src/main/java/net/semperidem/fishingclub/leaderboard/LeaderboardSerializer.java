package net.semperidem.fishingclub.leaderboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class LeaderboardSerializer {
    private static final String FILE_NAME = "fishing-club-leaderboards.json";

    public static void writePacket(LeaderboardTracker instance, PacketByteBuf packet) {
        packet.writeInt(instance.leaderboards.size());
        for(Leaderboard leaderboard : instance.leaderboards.values()) {
            Leaderboard.writeToPacket(packet, leaderboard);
        }
    }

    public static void readPacket(LeaderboardTracker instance, PacketByteBuf packet) {
        int leaderboardCount = packet.readInt();
        for(int i = 0; i < leaderboardCount; i++) {
            Leaderboard leaderboard = Leaderboard.fromPacket(packet);
            instance.leaderboards.put(leaderboard.getName(), leaderboard);
        }
    }

    public static void loadLeaderboards(LeaderboardTracker instance, MinecraftServer server) {
        try {
            if (!Files.exists(Path.of(getStoragePath(server)))) {
                return;
            }
            //TODO Possible performance hit on game load
            Gson gson = new GsonBuilder().registerTypeAdapter(Leaderboard.class, new LeaderboardInstanceCreator()).create();
            FileReader fileReader = new FileReader(getStoragePath(server));
            JsonReader jsonReader = new JsonReader(fileReader);
            HashMap<String, String> leaderboardsJSON = gson.fromJson(jsonReader, HashMap.class);
            for(String key : leaderboardsJSON.keySet()) {
                instance.leaderboards.put(key, gson.fromJson(gson.toJson(leaderboardsJSON.get(key)), Leaderboard.class));//workaround for type erasure
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

    public static void saveLeaderboards(LeaderboardTracker instance, MinecraftServer server) {
        try {
            Gson gson = new Gson();
            FileWriter fileWriter = new FileWriter(getStoragePath(server));
            gson.toJson(instance.leaderboards, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
