package net.semperidem.fishingclub.client.screen.leaderboard;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.leaderboard.LeaderboardSerializer;
import net.semperidem.fishingclub.screen.leaderboard.LeaderboardScreenHandler;
import org.jetbrains.annotations.Nullable;

public class LeaderboardScreenFactory implements ExtendedScreenHandlerFactory {

    @Override
    public Text getDisplayName() {
        return Text.literal("Leaderboard");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new LeaderboardScreenHandler(syncId, inv);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        LeaderboardSerializer.writePacket(buf);
    }
}
