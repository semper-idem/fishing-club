package net.semperidem.fishingclub.game.components;

public class ProgressComponent {
    private static final float PROGRESS_GAIN = 0.0075f;
    private boolean isFinished;
    private boolean isSuccessful;
    private boolean isWinning;
    private float progress;
    private final float progressGain;
    private final float progressLoss;

    public ProgressComponent(float progressGainMultiplier, float fishDamage) {
        progressGain = PROGRESS_GAIN * progressGainMultiplier;
        progressLoss = 0.005f + (fishDamage / 100);
    }

    public void tick(boolean isReeling, boolean bobberHasFish, boolean isFishJumping, boolean isTreasurePopupActive) {
        if (isTreasurePopupActive) {
            return;
        }

        if (isReeling) {
            if (bobberHasFish) {
                grantProgress();
            } else {
                revokeProgress();
            }
        }

        if (!bobberHasFish && !isFishJumping) {
            revokeProgress();
        }

    }

    private void grantProgress(){
        isWinning = true;
        if (progress < 1) {
            progress += progressGain;
        } else {
            isFinished = true;
            isSuccessful = true;
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

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}
