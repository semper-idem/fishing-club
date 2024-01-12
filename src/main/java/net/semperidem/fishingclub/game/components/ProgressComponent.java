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
            //SEND WIN PACKET
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
