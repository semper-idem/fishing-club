package net.semperidem.fishingclub;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.PlayerManager;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.registry.FRegistry;

public class FishingClub implements ModInitializer {
    public static final String MOD_ID = "fishing-club";
    public static PlayerManager playerManager;//THIS BAD
    @Override
    public void onInitialize() {
        Commands.register();
        FRegistry.register();
        ServerTickEvents.END_SERVER_TICK.register(server ->{
            if (playerManager == null) {
                playerManager = server.getPlayerManager();
            }
        });
    }

    public static Identifier getIdentifier(String resource){
        return new Identifier(MOD_ID, resource);
    }
}
