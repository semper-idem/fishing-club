package net.semperidem.fishingclub.screen.member;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fisher.FishingCard;
import org.jetbrains.annotations.Nullable;

public class MemberScreenHandlerFactory implements ExtendedScreenHandlerFactory {

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeNbt(FishingCard.getPlayerCard(player).toNbt());
    }
    @Override
    public Text getDisplayName() {
        return Text.translatable("Sell");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeNbt(FishingCard.getPlayerCard(player).toNbt());
        return new MemberScreenHandler(syncId, inv, buf);
    }
}
