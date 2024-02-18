package net.semperidem.fishingclub;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.registry.FishingClubRegistry;

public class FishingClub implements ModInitializer {
    public static final String MOD_ID = "fishing-club";
    @Override
    public void onInitialize() {
        Commands.register();
        FishingClubRegistry.register();
    }

    public static Identifier getIdentifier(String resource){
        return new Identifier(MOD_ID, resource);
    }
}
