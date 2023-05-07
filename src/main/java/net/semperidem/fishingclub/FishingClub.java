package net.semperidem.fishingclub;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.semperidem.fishingclub.network.ClientPacketReceiver;
import net.semperidem.fishingclub.network.ServerPacketReceiver;

public class FishingClub implements ModInitializer {
    public static final Identifier C2S_GRANT_EXP_ID = new Identifier("fishing-club", "c2s_grant_exp");
    public static final Identifier C2S_REQUEST_DATA_SYNC_ID = new Identifier("fishing-club", "c2s_request_data_sync");
    public static final Identifier S2C_SYNC_DATA_ID = new Identifier("fishing-club", "s2c_sync_data");

    @Override
    public void onInitialize() {
        ServerPacketReceiver.registerServerPacketHandlers();
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketReceiver.registerClientPacketHandlers();
        }
    }
}
