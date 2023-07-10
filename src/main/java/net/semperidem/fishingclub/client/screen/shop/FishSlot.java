package net.semperidem.fishingclub.client.screen.shop;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.client.game.fish.FishUtil;

import java.util.Optional;

public class FishSlot extends Slot {
    public FishSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
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

    @Override
    public void markDirty() {
        super.markDirty();
        tryCalculateSellContainerValue();

    }

    private void tryCalculateSellContainerValue(){
        Optional.ofNullable(MinecraftClient.getInstance().currentScreen)
                .filter(ShopScreen.class::isInstance)
                .map(ShopScreen.class::cast)
                .ifPresent(shopScreen -> shopScreen.getScreenHandler().calculateSellContainer());
    }
}