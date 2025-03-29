package net.semperidem.fishingclub.screen.card;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.network.payload.CardPayload;
import org.jetbrains.annotations.Nullable;

public class CardScreenFactory implements ExtendedScreenHandlerFactory<CardPayload> {
    @Override
    public Text getDisplayName() {
        return Text.literal("Fishing Card");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new CardScreenHandler(syncId, inv);
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
