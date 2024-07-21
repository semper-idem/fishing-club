package net.semperidem.fishing_club.screen.fishing_card;

import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.semperidem.fishing_club.client.screen.fishing_card.FishingCardScreen;
import net.semperidem.fishing_club.fish.FishRecord;
import net.semperidem.fishing_club.fisher.perks.Path;
import net.semperidem.fishing_club.registry.FCComponents;

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
            parent.fishingCard.addCredit(currentStack.getOrDefault(FCComponents.FISH, FishRecord.DEFAULT).value());
            currentStack.setCount(0);
        }
    }

    private void attemptHideSellButton(ItemStack stack) {
        if (parent.isClient && MinecraftClient.getInstance().currentScreen instanceof FishingCardScreen cardScreen) {
            cardScreen.instantSellSlotButton.visible = stack.isEmpty();
        }
    }
}
