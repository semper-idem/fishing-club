package net.semperidem.fishingclub.screen.configuration;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.network.payload.ConfigurationPayload;
import org.jetbrains.annotations.Nullable;

public class ConfigurationScreenHandlerFactory implements ExtendedScreenHandlerFactory<ConfigurationPayload> {
    private final ConfigurationPayload payload;

    public ConfigurationScreenHandlerFactory(ConfigurationPayload payload) {
        this.payload = payload;
    }
    @Override
    public Text getDisplayName() {
        return Text.literal("Configuration");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ConfigurationScreenHandler(syncId, inv, new ConfigurationPayload(payload.isMainHand(), payload.fishingRod()));
    }

    @Override
    public ConfigurationPayload getScreenOpeningData(ServerPlayerEntity player) {
        return new ConfigurationPayload(payload.isMainHand(), payload.fishingRod());
    }
}