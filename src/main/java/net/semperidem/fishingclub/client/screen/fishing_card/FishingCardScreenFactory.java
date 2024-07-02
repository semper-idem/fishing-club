package net.semperidem.fishingclub.client.screen.fishing_card;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.network.payload.FishingCardPayload;
import net.semperidem.fishingclub.screen.fishing_card.FishingCardScreenHandler;
import org.jetbrains.annotations.Nullable;

public class FishingCardScreenFactory implements ExtendedScreenHandlerFactory<FishingCardPayload> {
    @Override
    public Text getDisplayName() {
        return Text.literal("Fishing Club");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new FishingCardScreenHandler(syncId, inv, FishingCard.of(inv.player));
    }

    @Override
    public boolean shouldCloseCurrentScreen() {
        return false;
    }

    @Override
    public FishingCardPayload getScreenOpeningData(ServerPlayerEntity player) {
    return new FishingCardPayload();
    }
}
