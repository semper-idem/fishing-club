package net.semperidem.fishingclub.client.screen.dialog;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import org.jetbrains.annotations.Nullable;

public class DialogScreenHandlerFactory implements ExtendedScreenHandlerFactory {
    FishingCard fishingCard;
    FishermanEntity fishermanEntity;

    public DialogScreenHandlerFactory(FishingCard fishingCard, FishermanEntity fishermanEntity) {
        this.fishingCard = fishingCard;
        this.fishermanEntity = fishermanEntity;
    }
    @Override
    public Text getDisplayName() {
        return Text.empty();
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new DialogScreenHandler(syncId, inv, fishingCard);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeNbt(fishingCard.toNbt());
    }
}
