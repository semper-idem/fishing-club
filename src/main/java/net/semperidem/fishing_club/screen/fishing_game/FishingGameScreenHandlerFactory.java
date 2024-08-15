package net.semperidem.fishing_club.screen.fishing_game;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishing_club.fish.specimen.SpecimenData;
import net.semperidem.fishing_club.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishing_club.network.payload.FishingGamePayload;
import org.jetbrains.annotations.Nullable;

public class FishingGameScreenHandlerFactory implements ExtendedScreenHandlerFactory<FishingGamePayload> {
    private final SpecimenData fish;
    private final RodConfiguration rodConfiguration;

    public FishingGameScreenHandlerFactory(SpecimenData fish, RodConfiguration rodConfiguration) {
        this.fish = fish;
        this.rodConfiguration = rodConfiguration;
    }

    @Override
    public Text getDisplayName() {
        return Text.empty();
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
    return new FishingGameScreenHandler(syncId, inv, new FishingGamePayload(this.fish, this.rodConfiguration));
    }

    @Override
    public FishingGamePayload getScreenOpeningData(ServerPlayerEntity player) {
        return new FishingGamePayload(this.fish, this.rodConfiguration);
    }
}
