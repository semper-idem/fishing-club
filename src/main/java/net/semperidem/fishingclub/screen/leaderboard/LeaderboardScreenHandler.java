package net.semperidem.fishingclub.screen.leaderboard;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.registry.ScreenHandlerRegistry;
import org.jetbrains.annotations.Nullable;

public class LeaderboardScreenHandler extends ScreenHandler {

    public LeaderboardScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(ScreenHandlerRegistry.LEADERBOARD_SCREEN, syncId);
    }
    public LeaderboardScreenHandler(int syncId, PlayerInventory playerInventory, FishingCard fishingCard) {
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
