package net.semperidem.fishingclub.game.components;

import net.semperidem.fishingclub.game.FishGameController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodStatType;

public class ProgressComponent {
    private static final float PROGRESS_GAIN = 0.0075f;
    private boolean isWinning;
    private float progress;
    private final float progressGain;
    private final float progressLoss;
    private final FishGameController parent;

    public ProgressComponent(FishGameController parent) {
        this.parent = parent;
        progressGain = PROGRESS_GAIN * FishingRodPartController.getStat(parent.fish.caughtUsing, FishingRodStatType.PROGRESS_MULTIPLIER_BONUS);
        progressLoss = 0.005f + (parent.fish.damage / 100);
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
            progress += progressGain;
        } else {
            parent.winGame();
        }
    }

    private void revokeProgress(){
        isWinning = false;
        if (progress > 0) {
            progress = Math.max(0, progress - progressLoss);
        }
    }

    public boolean isWinning(){
        return isWinning;
    }

    public float getProgress(){
        return progress;
    }
}
