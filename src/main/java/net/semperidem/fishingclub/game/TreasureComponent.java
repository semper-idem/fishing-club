package net.semperidem.fishingclub.game;

import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.game.treasure.Rewards;
import net.semperidem.fishingclub.network.payload.FishingUpdatePayload;

public class TreasureComponent {

    private static final float TREASURE_MIN_TRIGGER_POINT = 0.5f;
    private static final float TREASURE_MAX_TRIGGER_POINT = 0.25f;

    private final boolean isActive;
    private boolean canPullTreasure;
    private float progress = 0;
    private float position;
    private float treasureTriggerPoint;

    private final FishingController parent;

    public TreasureComponent(FishingController parent) {
        this.parent = parent;
        this.isActive = Rewards.draw(parent.rodConfiguration, parent.card);
        this.treasureTriggerPoint = (float) (Math.random() * TREASURE_MAX_TRIGGER_POINT + TREASURE_MIN_TRIGGER_POINT);
        this.position = (float) Math.random();
    }

    public void updateClient(FishingUpdatePayload payload) {
        this.canPullTreasure = payload.canPullTreasure();
        this.progress = payload.treasureStartProgress();
        this.position = payload.treasureStartPosition();
    }

    private void tickProgress() {
        if (!this.canPullTreasure) {
            return;
        }
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
