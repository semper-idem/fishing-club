package net.semperidem.fishingclub.screen.fishing_card;

import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.client.screen.fishing_card.FishingCardScreen;
import net.semperidem.fishingclub.fish.FishComponent;
import net.semperidem.fishingclub.fisher.perks.Path;
import net.semperidem.fishingclub.registry.FCComponents;

public class InstantSellSlot extends UnlockableBoundSlot {
    public InstantSellSlot(Inventory inventory, int index, int x, int y, FishingCardScreenHandler parent, Path tab, boolean isUnlocked, Item boundItem) {
        super(inventory, index, x, y, parent, tab, isUnlocked, boundItem);
    }

    @Override
    public int getMaxItemCount () {
        return 1;
    }

    @Override
    public void setStackNoCallbacks (ItemStack stack){
        attemptSell();
        attemptHideSellButton(stack);
        super.setStackNoCallbacks(stack);
    }

    private void attemptSell() {
        ItemStack currentStack = getStack();
        if (!currentStack.isEmpty()) {
            parent.fishingCard.addCredit(currentStack.getOrDefault(FCComponents.FISH, FishComponent.DEFAULT).value());
            currentStack.setCount(0);
        }
    }

    private void attemptHideSellButton(ItemStack stack) {
        if (parent.isClient && MinecraftClient.getInstance().currentScreen instanceof FishingCardScreen cardScreen) {
            cardScreen.instantSellSlotButton.visible = stack.isEmpty();
        }
    }
}
