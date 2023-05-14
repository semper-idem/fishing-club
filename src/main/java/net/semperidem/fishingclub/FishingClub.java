package net.semperidem.fishingclub;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenHandler;
import net.semperidem.fishingclub.fish.fisher.FisherInfoDB;
import net.semperidem.fishingclub.network.ClientPacketReceiver;
import net.semperidem.fishingclub.network.ServerPacketReceiver;

public class FishingClub implements ModInitializer {
    public static final String MOD_ID = "fishing-club";
    public static final Identifier BOX = new Identifier(MOD_ID, "fish_shop");
    public static ScreenHandlerType<ShopScreenHandler> SHOP_SCREEN_HANDLER;
    @Override
    public void onInitialize() {
        ServerPacketReceiver.registerServerPacketHandlers();
        SHOP_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(BOX, ShopScreenHandler::new);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketReceiver.registerClientPacketHandlers();
        }
        FishingClubCommands.register();
        ServerLifecycleEvents.SERVER_STARTED.register(FisherInfoDB::linkServer);
    }
}
