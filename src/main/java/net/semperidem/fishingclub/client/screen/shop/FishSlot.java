package net.semperidem.fishingclub.client.screen.shop;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.fish.FishUtil;

public class FishSlot extends Slot {
    public FishSlot(Inventory inventory, int i, int j, int k) {
        super(inventory, i, j, k);
    }

    @Override
    public boolean canInsert(ItemStack itemStack) {
        return itemStack.isOf(FishUtil.FISH_ITEM);
    }

    @Override
    public boolean isEnabled() {
        Screen currentScreen = MinecraftClient.getInstance().currentScreen;
        return currentScreen instanceof ShopScreen && ((ShopScreen) currentScreen).screenType == ShopScreen.ScreenType.SELL;
    }
}