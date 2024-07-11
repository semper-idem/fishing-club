package net.semperidem.fishing_club.screen.fishing_card;

import net.minecraft.inventory.Inventory;
import net.semperidem.fishing_club.fisher.perks.Path;

public class UnlockableSlot extends TabSlot {
    private boolean isUnlocked = false;

    public UnlockableSlot(Inventory inventory, int index, int x, int y, FishingCardScreenHandler parent, Path tab, boolean isUnlocked) {
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
