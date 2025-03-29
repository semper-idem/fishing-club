package net.semperidem.fishingclub.screen.old_card;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

public class TabSlot extends Slot {
    final OldFishingCardScreenHandler parent;

    public TabSlot(Inventory inventory, int index, int x, int y, OldFishingCardScreenHandler parent) {
        super(inventory, index, x, y);
        this.parent = parent;
    }

}
