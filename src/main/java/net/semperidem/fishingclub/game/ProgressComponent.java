package net.semperidem.fishingclub.game;

import net.semperidem.fishingclub.network.payload.FishingGameTickS2CPayload;

public class ProgressComponent {
    private final FishingGameController parent;

    private static final float BASE_GAIN = 0.0075f;
    private static final float BASE_LOSS = 0.005f;

    private final float gain;
    private final float loss;

    private float progress;

    public ProgressComponent(FishingGameController parent) {
        this.parent = parent;
        gain = BASE_GAIN * (1 + getProgressMultiplierBonus());
        loss = BASE_LOSS + (parent.hookedFish.damage() * 0.01f);
    }



    public void updateClient(FishingGameTickS2CPayload payload) {
        this.progress = payload.progress();
    }

    private float getProgressMultiplierBonus() {
        return 1;//FishingRodPartController.getStat(parent.hookedFish.caughtUsing, FishingRodStatType.PROGRESS_MULTIPLIER_BONUS);
    }

    public void tick() {
        boolean bobberHasFish = parent.bobberComponent.hasFish(parent.fishController);
        if (bobberHasFish && parent.isReeling()) {
            grantProgress();
            return;
        }
        revokeProgress(parent.isReeling() ? 2 : 0.25f);
    }

    private void grantProgress(){
        if (progress < 1) {
            progress = Math.min(1, progress + gain);
            return;
        }
        parent.winGame();
    }

    private void revokeProgress(float multiplier){
        if (progress > 0) {
            progress = Math.max(0, progress - loss * multiplier);
        }
        if (multiplier > 1) {
            this.parent.healthComponent.damage();
        }
    }

    public float getProgress(){
        return progress;
    }

}
