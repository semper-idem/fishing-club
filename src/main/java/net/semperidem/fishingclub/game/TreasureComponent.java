package net.semperidem.fishingclub.game;

import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.network.payload.FishingGameTickS2CPayload;

public class TreasureComponent {

    private static final float TREASURE_MIN_CHANCE = 0.05f;
    private static final float TREASURE_MIN_TRIGGER_POINT = 0.5f;
    private static final float TREASURE_MAX_TRIGGER_POINT = 0.25f;

    private final boolean isActive;
    private boolean canPullTreasure;
    private float progress = 0;
    private float position;
    private float treasureTriggerPoint;

    private final FishingGameController parent;

    public TreasureComponent(FishingGameController parent) {
        float treasureChance = TREASURE_MIN_CHANCE;

        treasureChance *= (1 + parent.rodConfiguration.attributes().treasureBonus() * 0.01f);
        treasureChance *= (1 + parent.fishingCard.tradeSecretValue(TradeSecrets.TREASURE_CHANCE_BOAT));

        this.parent = parent;
        this.isActive = true;//Math.random() < treasureChance;
        this.treasureTriggerPoint = 0;//(float) (Math.random() * TREASURE_MAX_TRIGGER_POINT + TREASURE_MIN_TRIGGER_POINT);
        this.position = 0.2f;//(float) Math.random();
    }

    public void updateClient(FishingGameTickS2CPayload payload) {
        this.canPullTreasure = payload.canPullTreasure();
        this.progress = payload.treasureStartProgress();
        this.position = payload.treasureStartPosition();
    }

    private void tickProgress() {
        if (this.progress >= 1 && this.treasureTriggerPoint <= 1) {
            this.parent.startTreasureHunt();
            this.treasureTriggerPoint = 2;
            return;
        }

        if (parent.bobberComponent.hasPoint(this.position)) {
            this.progress = MathHelper.clamp(this.progress + 0.05f, 0, 1);
            return;
        }
        this.progress = MathHelper.clamp(this.progress - 0.03f, 0, 1);
    }


    public void tick() {
        if (!isActive){
            return;
        }

        if (parent.progressComponent.getProgress() >= treasureTriggerPoint) {
            canPullTreasure = true;
        }
        this.tickProgress();

    }

    public void clear() {
        this.canPullTreasure = false;
        this.treasureTriggerPoint = 2;
    }

    public float getPosition() {
        return this.position;
    }

    public float getProgress() {
        return this.progress;
    }

    public boolean canPullTreasure() {
        return canPullTreasure;
    }
}
