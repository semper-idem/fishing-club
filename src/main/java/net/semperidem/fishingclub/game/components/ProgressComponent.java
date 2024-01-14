package net.semperidem.fishingclub.game.components;

import net.semperidem.fishingclub.game.FishGameController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodStatType;

public class ProgressComponent {
    private final FishGameController parent;

    private static final float BASE_GAIN = 0.0075f;
    private static final float BASE_LOSS = 0.005f;

    private final float gain;
    private final float loss;

    private float progress;
    private boolean isWinning;

    public ProgressComponent(FishGameController parent) {
        this.parent = parent;
        gain = BASE_GAIN * getProgressMultiplierBonus();
        loss = BASE_LOSS + (parent.fish.damage * 0.01f);
    }

    private float getProgressMultiplierBonus() {
        return FishingRodPartController.getStat(parent.fish.caughtUsing, FishingRodStatType.PROGRESS_MULTIPLIER_BONUS);
    }

    public void tick(boolean isReeling, boolean isPulling, boolean bobberHasFish, boolean isFishJumping) {
        if (!bobberHasFish && !isFishJumping) {
            revokeProgress();
        }

        if (!isReeling) {
            return;
        }
        if (bobberHasFish && !isPulling) {
            grantProgress();
        } else {
            revokeProgress();
        }
    }

    private void grantProgress(){
        isWinning = true;
        if (progress < 1) {
            progress = Math.min(1, progress + gain);
        } else {
            parent.winGame();
        }
    }

    private void revokeProgress(){
        isWinning = false;
        if (progress > 0) {
            progress = Math.max(0, progress - loss);
        }
    }

    public boolean isWinning(){
        return isWinning;
    }

    public float getProgress(){
        return progress;
    }

}
