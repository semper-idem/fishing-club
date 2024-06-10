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

import java.util.Map;

public class LeaderboardScreenHandler extends ScreenHandler {
    LeaderboardTracker tracker = new LeaderboardTracker();

    public LeaderboardScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(ScreenHandlerRegistry.LEADERBOARD_SCREEN, syncId);
        LeaderboardSerializer.readPacket(tracker, buf);
    }
    public LeaderboardScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlerRegistry.LEADERBOARD_SCREEN, syncId);
    }

    public Map<String, Leaderboard> getLeaderboards() {
        return tracker.getLeaderboards();
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
