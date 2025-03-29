package net.semperidem.fishingclub.screen.old_card;

import net.minecraft.inventory.Inventory;

public class UnlockableSlot extends TabSlot {
    private boolean isUnlocked = false;

    public UnlockableSlot(Inventory inventory, int index, int x, int y, OldFishingCardScreenHandler parent, boolean isUnlocked) {
        super(inventory, index, x, y, parent);
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
