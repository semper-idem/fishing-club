package net.semperidem.fishingclub.screen;

import net.minecraft.inventory.Inventory;

public class UnlockableSlot extends TabSlot {
    private boolean isUnlocked = false;

    public UnlockableSlot(Inventory inventory, int index, int x, int y, FishingCardScreenHandler parent, FishingCardTab tab, boolean isUnlocked) {
        super(inventory, index, x, y, parent, tab);
        this.isUnlocked = isUnlocked;
    }

    public void unlock() {
        isUnlocked = true;
    }

    @Override
    public boolean isEnabled() {
        return isUnlocked && super.isEnabled();
    }
}
