package net.semperidem.fishingclub.client.screen.old_fishing_card;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.network.payload.CardPayload;
import net.semperidem.fishingclub.screen.card.OldFishingCardScreenHandler;
import org.jetbrains.annotations.Nullable;

public class FishingCardScreenFactory implements ExtendedScreenHandlerFactory<CardPayload> {
    @Override
    public Text getDisplayName() {
        return Text.literal("Fishing Club");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new OldFishingCardScreenHandler(syncId, inv, Card.of(inv.player));
    }

    @Override
    public boolean shouldCloseCurrentScreen() {
        return false;
    }

    @Override
    public CardPayload getScreenOpeningData(ServerPlayerEntity player) {
    return new CardPayload();
    }
}
