package net.semperidem.fishingclub.client.screen.shop;

import net.minecraft.entity.player.PlayerEntity;

public class ShopScreenUtil {
    public final static int SLOTS_PER_ROW = 9;
    public final static int SLOT_SIZE = 18;
    public final static int PLAYER_INVENTORY_ROWS = 3;

    public static void openShopScreen(PlayerEntity player){
        if(player.world != null && !player.world.isClient) {
            player.openHandledScreen(new ShopScreenFactory());
        }
    }

}
