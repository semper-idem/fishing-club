package net.semperidem.fishingclub.screen.card;

import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.client.screen.old_fishing_card.OldFishingCardScreen;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.registry.Components;

public class InstantSellSlot extends UnlockableSlot {
    public InstantSellSlot(Inventory inventory, int index, int x, int y, OldFishingCardScreenHandler parent, boolean isUnlocked) {
        super(inventory, index, x, y, parent, isUnlocked);
    }

    @Override
    public void setStackNoCallbacks(ItemStack stack) {
        this.attemptSell();
        super.setStackNoCallbacks(stack);
    }

    private void attemptSell() {
        if (this.getStack().isEmpty()) {
            return;
        }
        if (!Species.Library.isSellable(this.getStack())) {
            return;
        }
        float sellRatio = parent.card.tradeSecretValue(TradeSecrets.INSTANT_FISH_CREDIT);
        parent.card.addCredit((int) (this.getStack().getOrDefault(Components.SPECIMEN, SpecimenData.DEFAULT).value() * sellRatio * this.getStack().getCount()));
        this.getStack().setCount(0);
        if (this.parent.isClient && MinecraftClient.getInstance().currentScreen instanceof OldFishingCardScreen cardScreen) {
            cardScreen.instantSellSlotButton.visible = true;
        }
    }
}
