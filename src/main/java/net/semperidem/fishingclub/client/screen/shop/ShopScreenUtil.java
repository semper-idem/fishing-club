package net.semperidem.fishingclub.client.screen.shop;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import org.jetbrains.annotations.Nullable;

public class ShopScreenUtil {
    public static ScreenHandlerType<SellShopScreenHandler> FISH_SHOP_SCREEN_HANDLER;


    public static void register(){
        //TODO don't use deprecated method
        FISH_SHOP_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(FishingClub.MOD_ID, "sell_shop_screen"), SellShopScreenHandler::new);
    }
    public static void registerClient(){
        HandledScreens.register(FISH_SHOP_SCREEN_HANDLER, SellShopScreen::new);
    }

    public static void openSellScreen(PlayerEntity player){
        if(player.world != null && !player.world.isClient) {
            player.openHandledScreen(new NamedScreenHandlerFactory() {

                @Override
                public Text getDisplayName() {
                    return Text.translatable("Value:");
                }

                @Override
                public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new SellShopScreenHandler(syncId, inv);
                }
            });
        }
    }
}
