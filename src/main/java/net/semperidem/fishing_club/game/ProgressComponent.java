package net.semperidem.fishing_club.game;

import net.minecraft.network.PacketByteBuf;
import net.semperidem.fishing_club.network.payload.FishingGameTickPayload;

public class ProgressComponent {
    private final FishingGameController parent;

    private static final float BASE_GAIN = 0.0075f;
    private static final float BASE_LOSS = 0.005f;

    private final float gain;
    private final float loss;

    private float progress;
    private boolean isWinning = false;

    public ProgressComponent(FishingGameController parent) {
        this.parent = parent;
        gain = BASE_GAIN * (1 + getProgressMultiplierBonus());
        loss = BASE_LOSS + (parent.hookedFish.damage() * 0.01f);
    }



    public void consumeData(FishingGameTickPayload payload) {
        this.progress = payload.progress();
    }

    public void writeData(PacketByteBuf buf) {
        buf.writeFloat(progress);
    }

    private float getProgressMultiplierBonus() {
        return 1;//FishingRodPartController.getStat(parent.hookedFish.caughtUsing, FishingRodStatType.PROGRESS_MULTIPLIER_BONUS);
    }

    public void tick() {
        boolean bobberHasFish = parent.bobberComponent.hasFish(parent.fishController);
        if (!bobberHasFish && !parent.fishController.isFishJumping()) {
            revokeProgress();
        }

        if (!parent.isReeling()) {
            return;
        }
        if (bobberHasFish && !parent.isPulling()) {
            grantProgress();
        } else {
            revokeProgress();
        }
    }

    private void grantProgress(){
        isWinning = true;
        if (progress < 1) {
            progress = Math.min(1, progress + gain);
            return;
        }
        parent.winGame();
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
