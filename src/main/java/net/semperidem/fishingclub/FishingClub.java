package net.semperidem.fishingclub;

import net.fabricmc.api.ModInitializer;
import net.minecraft.SharedConstants;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.registry.Registry;
import net.semperidem.fishingclub.util.Commands;
import net.semperidem.fishingclub.util.ResourceUtil;

public class FishingClub implements ModInitializer {
    public static final String MOD_ID = "fishingclub";

    @Override
    public void onInitialize() {
        SharedConstants.isDevelopment = true;
        Commands.register();
        Registry.register();
        ResourceUtil.loadMessageInBottle();
    }

    public static Identifier identifier(String resource){
        return Identifier.of(MOD_ID, resource);
    }
}
