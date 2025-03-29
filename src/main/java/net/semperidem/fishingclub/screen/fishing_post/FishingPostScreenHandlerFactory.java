package net.semperidem.fishingclub.screen.fishing_post;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.network.payload.FishingPostPayload;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FishingPostScreenHandlerFactory implements ExtendedScreenHandlerFactory<FishingPostPayload> {
    SpecimenData fish;
    List<ItemStack> rewards;
    int exp;

    public FishingPostScreenHandlerFactory(SpecimenData fish, List<ItemStack> rewards, int exp) {
        this.fish = fish;
        this.rewards = rewards;
        this.exp = exp;
    }

    @Override
    public Text getDisplayName() {
        return Text.empty();
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
    return new FishingPostScreenHandler(syncId, inv, new FishingPostPayload(this.fish, this.rewards, this.exp));
    }

    @Override
    public FishingPostPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new FishingPostPayload(this.fish, this.rewards, this.exp);
    }
}