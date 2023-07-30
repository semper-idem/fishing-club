package net.semperidem.fishingclub.client.screen.shop;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class ShopScreenUtil {
    public final static int SLOTS_PER_ROW = 9;
    public final static int SLOT_SIZE = 18;


    public static void registerClient(){
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
