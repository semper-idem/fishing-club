package net.semperidem.fishingclub.screen.fishing_game;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fisher.FishingCard;
import org.jetbrains.annotations.Nullable;

public class FishGameScreenFactory  implements ExtendedScreenHandlerFactory {
    private final FishingCard fishingCard;
    private final Fish fish;

    public FishGameScreenFactory(FishingCard fishingCard, Fish fish) {
        this.fishingCard = fishingCard;
        this.fish = fish;
    }
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeNbt(fishingCard.toNbt());
        buf.writeNbt(fish.getNbt());
    }

    @Override
    public Text getDisplayName() {
        return Text.empty();
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new FishingGameScreenHandler(syncId, inv, fishingCard, fish);
    }
}
