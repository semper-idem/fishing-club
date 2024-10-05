package net.semperidem.fishingclub.screen.fishing_game_post;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.network.payload.FishingGamePostS2CPayload;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FishingGamePostScreenHandlerFactory implements ExtendedScreenHandlerFactory<FishingGamePostS2CPayload> {
    SpecimenData fish;
    List<ItemStack> rewards;

    public FishingGamePostScreenHandlerFactory(SpecimenData fish, List<ItemStack> rewards) {
        this.fish = fish;
        this.rewards = rewards;
    }

    @Override
    public Text getDisplayName() {
        return Text.empty();
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
    return new FishingGamePostScreenHandler(syncId, inv, new FishingGamePostS2CPayload(this.fish, this.rewards));
    }

    @Override
    public FishingGamePostS2CPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new FishingGamePostS2CPayload(this.fish, this.rewards);
    }
}