package net.semperidem.fishing_club;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.semperidem.fishing_club.registry.FCRegistry;
import net.semperidem.fishing_club.util.Commands;

public class FishingClub implements ModInitializer {
    public static final String MOD_ID = "fishing-club";

    @Override
    public void onInitialize() {
        Commands.register();
        FCRegistry.register();
    }

    public static Identifier getIdentifier(String resource){
        return Identifier.of(MOD_ID, resource);
    }
}
