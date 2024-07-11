package net.semperidem.fishing_club.screen.member;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.network.payload.MemberPayload;
import org.jetbrains.annotations.Nullable;

public class MemberScreenHandlerFactory implements ExtendedScreenHandlerFactory<MemberPayload> {
    @Override
    public Text getDisplayName() {
        return Text.of("Member Screen");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new MemberScreenHandler(syncId, inv, FishingCard.of(player));
    }

    @Override
    public MemberPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new MemberPayload();
    }
}
