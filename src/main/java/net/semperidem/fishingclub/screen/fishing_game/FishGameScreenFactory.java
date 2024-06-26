package net.semperidem.fishingclub.screen.fishing_game;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.network.payload.FishingGamePayload;
import org.jetbrains.annotations.Nullable;

public class FishGameScreenFactory  implements ExtendedScreenHandlerFactory<FishingGamePayload> {
    private final Fish fish;

    public FishGameScreenFactory(Fish fish) {
        this.fish = fish;
    }

    @Override
    public Text getDisplayName() {
        return Text.empty();
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new FishingGameScreenHandler(syncId, inv, this.fish);
    }

    @Override
    public FishingGamePayload getScreenOpeningData(ServerPlayerEntity player) {
        return new FishingGamePayload(this.fish.getNbt());
    }
}
