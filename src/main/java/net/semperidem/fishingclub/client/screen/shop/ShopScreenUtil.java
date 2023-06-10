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
    final static int SLOTS_PER_ROW = 9;
    final static int SLOT_SIZE = 18;
    public static ScreenHandlerType<ShopSellScreenHandler> SHOP_SELL_SCREEN;
    public static ScreenHandlerType<ShopBuyScreenHandler> SHOP_BUY_SCREEN;


    public static void register(){
        //TODO don't use deprecated method
        SHOP_SELL_SCREEN = ScreenHandlerRegistry.registerSimple(new Identifier(FishingClub.MOD_ID, "sell_shop_screen"), ShopSellScreenHandler::new);
        SHOP_BUY_SCREEN = ScreenHandlerRegistry.registerSimple(new Identifier(FishingClub.MOD_ID, "sell_buy_screen"), ShopBuyScreenHandler::new);
    }
    public static void registerClient(){
        HandledScreens.register(SHOP_SELL_SCREEN, ShopSellScreen::new);
        HandledScreens.register(SHOP_BUY_SCREEN, ShopBuyScreen::new);
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
                    return new ShopSellScreenHandler(syncId, inv);
                }
            });
        }
    }
    public static void openBuyScreen(PlayerEntity player){
        if(player.world != null && !player.world.isClient) {
            player.openHandledScreen(new NamedScreenHandlerFactory() {

                @Override
                public Text getDisplayName() {
                    return Text.translatable("Buy");
                }

                @Override
                public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new ShopBuyScreenHandler(syncId, inv);
                }
            });
        }
    }
}
