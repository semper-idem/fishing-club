package net.semperidem.fishingclub.game;

import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.network.payload.FishingGameTickS2CPayload;

public class TreasureComponent {

    private static final float TREASURE_MIN_CHANCE = 0.05f;
    private static final float TREASURE_MIN_TRIGGER_POINT = 0.5f;
    private static final float TREASURE_MAX_TRIGGER_POINT = 0.25f;

    private final boolean isActive;
    private boolean canPullTreasure;
    private int pullTreasureTicks = 0;
    private float treasureTriggerPoint;

    private final FishingGameController parent;

    public TreasureComponent(FishingGameController parent) {
        float treasureChance = TREASURE_MIN_CHANCE;

        treasureChance *= (1 + parent.rodConfiguration.attributes().treasureBonus() * 0.01f);
        treasureChance *= (1 + parent.fishingCard.tradeSecretValue(TradeSecrets.TREASURE_CHANCE_BOAT));

        this.parent = parent;
        this.isActive = Math.random() < treasureChance;
        this.treasureTriggerPoint = (float) (Math.random() * TREASURE_MAX_TRIGGER_POINT + TREASURE_MIN_TRIGGER_POINT);
    }

    public void updateClient(FishingGameTickS2CPayload payload) {
        this.canPullTreasure = payload.canPullTreasure();
    }


    public void tick() {
        if (!isActive){
            return;
        }

        if (parent.progressComponent.getProgress() > treasureTriggerPoint) {
            pullTreasureTicks = 50;
            treasureTriggerPoint = 2;
        }

        if (pullTreasureTicks == 0) {
            return;
        }

        pullTreasureTicks--;
            pullTreasureTicks = 0;
            parent.startTreasureHunt();
    }

    public boolean canPullTreasure() {
        return canPullTreasure;
    }
}
