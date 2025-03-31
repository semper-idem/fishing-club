package net.semperidem.fishingclub.screen.configuration;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.network.payload.RodConfigurationPayload;
import org.jetbrains.annotations.Nullable;

public class RodConfigurationScreenHandlerFactory implements ExtendedScreenHandlerFactory<RodConfigurationPayload> {
    @Override
    public Text getDisplayName() {
        return Text.literal("Configuration");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new RodConfigurationScreenHandler(syncId, inv, new RodConfigurationPayload());
    }

    @Override
    public RodConfigurationPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new RodConfigurationPayload();
    }
}