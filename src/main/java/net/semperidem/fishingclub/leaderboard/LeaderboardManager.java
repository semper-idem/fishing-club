package net.semperidem.fishingclub.leaderboard;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.WorldSavePath;
import net.minecraft.world.level.storage.LevelStorage;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LeaderboardManager {
    public static final String LFC = "Longest Fish Caught";
    private static final Map<String, Leaderboard> leaderboards = new HashMap<>();

    public static void initLeaderboards() {
        leaderboards.put(LFC, new Leaderboard(LFC));
    }

    private static Leaderboard getLeaderboard(String name) {
        return leaderboards.get(name);
    }

    public static void record(String name, UUID holder, double value) {
        if (leaderboards.containsKey(name)) {
            leaderboards.get(name).consume(holder, value);
        }
    }

    public static void loadLeaderboards(MinecraftServer server) {
        initLeaderboards();
        try {
            FileInputStream fis = new FileInputStream(server.getSavePath(WorldSavePath.ROOT).toString() + "f_lb");
            if(fis.available() == 0) {
                return;
            }
            ObjectInputStream ois = new ObjectInputStream(fis);
            while(ois.available() > 0) {
                Leaderboard leaderboard = (Leaderboard) ois.readObject();
                leaderboards.put(leaderboard.getName(), leaderboard);
            }
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveLeaderboards(MinecraftServer server) {
        try {
            FileOutputStream fos = new FileOutputStream(server.getSavePath(WorldSavePath.ROOT).toString() + "f_lb");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for(String name : leaderboards.keySet()) {
                oos.writeObject(leaderboards.get(name));
            }
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
