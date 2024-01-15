package net.semperidem.fishingclub.game;

import net.semperidem.fishingclub.fisher.perks.FishingPerks;

public class TreasureComponent {

    private static final float TREASURE_MIN_CHANCE = 0.05f;
    private static final float TREASURE_MIN_TRIGGER_POINT = 0.5f;
    private static final float TREASURE_MAX_TRIGGER_POINT = 0.25f;

    private final boolean isActive;
    private int pullTreasureTicks = 0;
    private float treasureTriggerPoint;

    private final FishGameController parent;


    public TreasureComponent(FishGameController parent) {
        float treasureChance = TREASURE_MIN_CHANCE;

        if (parent.fishingCard.isFishingFromBoat() && parent.fishingCard.hasPerk(FishingPerks.DOUBLE_TREASURE_BOAT)) {
            treasureChance = treasureChance * 2;
        }

        this.parent = parent;
        this.isActive = Math.random() < treasureChance;
        this.treasureTriggerPoint = (float) (Math.random() * TREASURE_MAX_TRIGGER_POINT + TREASURE_MIN_TRIGGER_POINT);
    }

    public void tick(float progress, boolean isPulling) {
        if (!isActive){
            return;
        }

        if (progress > treasureTriggerPoint) {
            pullTreasureTicks = 50;
            treasureTriggerPoint = 2;
        }

        if (pullTreasureTicks == 0) {
            return;
        }

        pullTreasureTicks--;
        if (isPulling) {
            parent.startTreasureHunt();
        }
    }

    public boolean canPullTreasure() {
        return pullTreasureTicks > 0;
    }
}
