package net.semperidem.fishingclub.screen;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;

public class FisherSlot extends Slot {
    private final FishingCardScreenHandler fishingCardScreenHandler;
    FishingPerk requiredPerk;

    public FisherSlot(FishingCardScreenHandler fishingCardScreenHandler, Inventory inventory, int index, int x, int y, FishingPerk requiredPerk) {
        super(inventory, index, x, y);
        this.fishingCardScreenHandler = fishingCardScreenHandler;
        this.requiredPerk = requiredPerk;
    }

    @Override
    public boolean isEnabled() {
        return fishingCardScreenHandler.rootPerk == null && fishingCardScreenHandler.fishingCard.hasPerk(requiredPerk);
    }

}
