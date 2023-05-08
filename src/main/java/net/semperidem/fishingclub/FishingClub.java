package net.semperidem.fishingclub;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.semperidem.fishingclub.network.ClientPacketReceiver;
import net.semperidem.fishingclub.network.ServerPacketReceiver;

public class FishingClub implements ModInitializer {
    public static final String MODID = "fishing-club";

    @Override
    public void onInitialize() {
        ServerPacketReceiver.registerServerPacketHandlers();
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketReceiver.registerClientPacketHandlers();
        }
    }
}
