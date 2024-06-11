package net.semperidem.fishingclub.leaderboard;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

public class LeaderboardSerializer {
    private static final String FOLDER_NAME = "fishing-club-leaderboards/";

    public static void writePacket(LeaderboardTracker instance, PacketByteBuf packet) {
        Leaderboard.writeToPacket(packet, instance.bestWeight);
        Leaderboard.writeToPacket(packet, instance.worstWeight);
        Leaderboard.writeToPacket(packet, instance.bestLength);
        Leaderboard.writeToPacket(packet, instance.worstLength);
        Leaderboard.writeToPacket(packet, instance.highestCredit);
        Leaderboard.writeToPacket(packet, instance.highestLevel);
        Leaderboard.writeToPacket(packet, instance.longestCapeClaimTotal);
    }

    public static void readPacket(LeaderboardTracker instance, PacketByteBuf packet) {
        Leaderboard.readStandings(packet, instance.bestWeight);
        Leaderboard.readStandings(packet, instance.worstWeight);
        Leaderboard.readStandings(packet, instance.bestLength);
        Leaderboard.readStandings(packet, instance.worstLength);
        Leaderboard.readStandings(packet, instance.highestCredit);
        Leaderboard.readStandings(packet, instance.highestLevel);
        Leaderboard.readStandings(packet, instance.longestCapeClaimTotal);
    }

    public static void loadLeaderboards(LeaderboardTracker instance, MinecraftServer server) {
        String path = getStoragePath(server);
        instance.bestWeight.loadFromPath(path);
        instance.worstWeight.loadFromPath(path);
        instance.bestLength.loadFromPath(path);
        instance.worstLength.loadFromPath(path);
        instance.highestCredit.loadFromPath(path);
        instance.highestLevel.loadFromPath(path);
        instance.longestCapeClaimTotal.loadFromPath(path);
    }

    private static String getStoragePath(MinecraftServer server) {
        String saveRootPath = server.getSavePath(WorldSavePath.ROOT).toString();
        return saveRootPath.substring(0, saveRootPath.lastIndexOf(".")) + FOLDER_NAME;
    }

    public static void saveLeaderboards(LeaderboardTracker instance, MinecraftServer server) {
        String path = getStoragePath(server);
        instance.bestWeight.saveToPath(path);
        instance.worstWeight.saveToPath(path);
        instance.bestLength.saveToPath(path);
        instance.worstLength.saveToPath(path);
        instance.highestCredit.saveToPath(path);
        instance.highestLevel.saveToPath(path);
        instance.longestCapeClaimTotal.saveToPath(path);
    }
}
