package net.semperidem.fishingclub;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.registry.FCRegistry;
import net.semperidem.fishingclub.util.Commands;

public class FishingClub implements ModInitializer {
    public static final String MOD_ID = "fishing-club";

    @Override
    public void onInitialize() {
        Commands.register();
        FCRegistry.register();
    }

    public static Identifier identifier(String resource){
        return Identifier.of(MOD_ID, resource);
    }
}
