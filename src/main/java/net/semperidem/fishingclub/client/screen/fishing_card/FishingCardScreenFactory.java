package net.semperidem.fishingclub.client.screen.fishing_card;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.screen.fishing_card.FishingCardScreenHandler;
import org.jetbrains.annotations.Nullable;

public class FishingCardScreenFactory implements ExtendedScreenHandlerFactory {
    FishingCard fishingCard;

    public FishingCardScreenFactory(FishingCard fishingCard) {
        this.fishingCard = fishingCard;
    }
    @Override
    public Text getDisplayName() {
        return Text.literal("Fishing Club");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new FishingCardScreenHandler(syncId, inv, fishingCard);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeNbt(fishingCard.toNbt());
    }
}
