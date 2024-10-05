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
        this.progress = 1;
        this.parent.winGame();
        if (this.progress == 1) {
            return;
        }
//        if (bobberHasFish && parent.isReeling()) {
//            grantProgress();
//            return;
//        }
//        revokeProgress();
    }

    private void grantProgress(){
        progress = Math.min(1, progress + gain);

        if (progress >= 1) {
            parent.winGame();
        }
    }

    private void revokeProgress(){
        if (progress > 0) {
            progress = (float) Math.max(0, progress - loss * (this.parent.isReeling() ? 2 : 0.25));
        }
        if (this.parent.isReeling()) {
            this.parent.healthComponent.damage();
        }
    }

    public float getProgress(){
        return progress;
    }

}
