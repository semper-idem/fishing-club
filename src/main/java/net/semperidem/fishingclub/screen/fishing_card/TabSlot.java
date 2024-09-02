package net.semperidem.fishingclub.screen.fishing_card;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

public class TabSlot extends Slot {
    final FishingCardScreenHandler parent;

    public TabSlot(Inventory inventory, int index, int x, int y, FishingCardScreenHandler parent) {
        super(inventory, index, x, y);
        this.parent = parent;
    }

}
