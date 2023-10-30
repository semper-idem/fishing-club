package net.semperidem.fishingclub.item.fishing_rod;

public enum FishingRodPartType {
    CORE(1),
    BOBBER(2),
    LINE(3),
    HOOK(4),
    BAIT(5);

    public int slotIndex;

    FishingRodPartType(int slotIndex) {
        this.slotIndex = slotIndex;
    }
}
