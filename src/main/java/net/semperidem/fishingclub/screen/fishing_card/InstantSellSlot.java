package net.semperidem.fishingclub.screen.fishing_card;

import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.client.screen.fishing_card.FishingCardScreen;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.registry.FCComponents;

public class InstantSellSlot extends UnlockableSlot {
    public InstantSellSlot(Inventory inventory, int index, int x, int y, FishingCardScreenHandler parent, boolean isUnlocked) {
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
        float sellRatio = parent.fishingCard.tradeSecretValue(TradeSecrets.FREE_SHOP_SUMMON);
        parent.fishingCard.addCredit((int) (this.getStack().getOrDefault(FCComponents.SPECIMEN, SpecimenData.DEFAULT).value() * sellRatio * this.getStack().getCount()));
        this.getStack().setCount(0);
        if (this.parent.isClient && MinecraftClient.getInstance().currentScreen instanceof FishingCardScreen cardScreen) {
            cardScreen.instantSellSlotButton.visible = true;
        }
    }
}
