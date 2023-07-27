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
    public final static int SLOTS_PER_ROW = 9;
    public final static int SLOT_SIZE = 18;
    public static ScreenHandlerType<ShopScreenHandler> SHOP_SCREEN;


    public static void register(){
        //TODO don't use deprecated method
        SHOP_SCREEN = ScreenHandlerRegistry.registerSimple(new Identifier(FishingClub.MOD_ID, "sell_screen"), ShopScreenHandler::new);
  }
    public static void registerClient(){
        HandledScreens.register(SHOP_SCREEN, ShopScreen::new);
    }

    public static void openShopScreen(PlayerEntity player){
        openShopScreen(player, ShopScreen.ScreenType.SELL);
    }
    public static void openShopScreen(PlayerEntity player, ShopScreen.ScreenType screenType){
        if(player.world != null && !player.world.isClient) {
            player.openHandledScreen(new NamedScreenHandlerFactory() {

                @Override
                public Text getDisplayName() {
                    return Text.translatable(screenType.toString().toLowerCase());
                }

                @Override
                public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new ShopScreenHandler(syncId, inv);
                }
            });
        }
    }

}
