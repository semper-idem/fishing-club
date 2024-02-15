package net.semperidem.fishingclub.screen;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

public class TabSlot extends Slot {
    final FishingCardScreenHandler parent;
    private final FishingCardTab tab;

    public TabSlot(Inventory inventory, int index, int x, int y, FishingCardScreenHandler parent, FishingCardTab tab) {
        super(inventory, index, x, y);
        this.parent = parent;
        this.tab = tab;
    }

    @Override
    public boolean isEnabled() {
        return parent.getActiveTab() == tab;
    }
}
