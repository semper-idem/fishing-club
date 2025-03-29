package net.semperidem.fishingclub.screen.fishing;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishingclub.network.payload.FishingStartS2CPayload;
import org.jetbrains.annotations.Nullable;

public class FishingScreenHandlerFactory implements ExtendedScreenHandlerFactory<FishingStartS2CPayload> {
    private final SpecimenData fish;
    private final RodConfiguration rodConfiguration;

    public FishingScreenHandlerFactory(SpecimenData fish, RodConfiguration rodConfiguration) {
        this.fish = fish;
        this.rodConfiguration = rodConfiguration;
    }

    @Override
    public Text getDisplayName() {
        return Text.empty();
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
    return new FishingScreenHandler(syncId, inv, new FishingStartS2CPayload(this.fish, this.rodConfiguration));
    }

    @Override
    public FishingStartS2CPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new FishingStartS2CPayload(this.fish, this.rodConfiguration);
    }
}
