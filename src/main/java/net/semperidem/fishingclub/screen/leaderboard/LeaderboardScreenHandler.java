package net.semperidem.fishingclub.screen.leaderboard;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.semperidem.fishingclub.leaderboard.Leaderboard;
import net.semperidem.fishingclub.leaderboard.LeaderboardSerializer;
import net.semperidem.fishingclub.leaderboard.LeaderboardTracker;
import net.semperidem.fishingclub.registry.ScreenHandlerRegistry;

import java.util.ArrayList;

public class LeaderboardScreenHandler extends ScreenHandler {
    LeaderboardTracker tracker = new LeaderboardTracker();
    ArrayList<Leaderboard<?>> leaderboards;
    int leaderboardIndex = 0;

    public LeaderboardScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(ScreenHandlerRegistry.LEADERBOARD_SCREEN, syncId);
        LeaderboardSerializer.readPacket(tracker, buf);
        leaderboards = new ArrayList<>(tracker.getLeaderboards());
    }
    public LeaderboardScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlerRegistry.LEADERBOARD_SCREEN, syncId);
    }

    public ArrayList<Leaderboard<?>> getLeaderboards() {
        return leaderboards;
    }

    public Leaderboard<?> getCurrentLeaderboard() {
        return leaderboards.get(leaderboardIndex);
    }
    public Leaderboard<?> getNextLeaderboard() {
        leaderboardIndex++;
        if (leaderboardIndex >= leaderboards.size()) {
            leaderboardIndex = 0;
        }
        return leaderboards.get(leaderboardIndex);
    }
    public Leaderboard<?> getPreviousLeaderboard() {
        leaderboardIndex--;
        if (leaderboardIndex < 0) {
            leaderboardIndex = leaderboards.size() - 1;
        }
        return leaderboards.get(leaderboardIndex);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
