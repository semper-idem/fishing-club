package net.semperidem.fishingclub.screen.leaderboard;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.semperidem.fishingclub.leaderboard.LeaderboardManager;
import net.semperidem.fishingclub.registry.ScreenHandlerRegistry;

public class LeaderboardScreenHandler extends ScreenHandler {

    public LeaderboardScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(ScreenHandlerRegistry.LEADERBOARD_SCREEN, syncId);
        LeaderboardManager.readPacket(buf);
    }
    public LeaderboardScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlerRegistry.LEADERBOARD_SCREEN, syncId);
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
