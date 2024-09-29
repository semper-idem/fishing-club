package net.semperidem.fishingclub.game;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.game.treasure.Reward;
import net.semperidem.fishingclub.game.treasure.Rewards;
import net.semperidem.fishingclub.network.payload.FishingGameTickS2CPayload;
import net.semperidem.fishingclub.registry.FCKeybindings;

import java.util.ArrayList;

public class TreasureGameController {
    private static final float TREASURE_MAX_SPOT_SIZE = 0.225f;
    private static final float TREASURE_GRADE_TO_SPOT_SIZE_RATIO = 0.025f;

    private final Reward treasureReward;

    private int treasureHookedTicks;
    private float progress;
    private int ticks;
    private boolean isDone = false;
    private final FishingGameController parent;
    private boolean canReel = true;
    private final float decay;

    public void updateClient(FishingGameTickS2CPayload payload) {
        this.progress = payload.treasureProgress();
        this.treasureHookedTicks = payload.treasureTicksLeft();
        this.isDone = payload.isTreasureDone();
    }

    public boolean isWon(){
        return this.progress == 1;
    }

    public TreasureGameController(FishingGameController parent) {
        this.parent = parent;
        this.treasureReward = Rewards.roll(parent.fishingCard, parent.rodConfiguration);
        this.decay = 0.005f + 0.002f * parent.hookedFish.quality();
    }

    public void start() {
        this.treasureHookedTicks = 200;
        this.parent.treasureComponent.clear();
    }

    public void tick() {
        if (treasureHookedTicks == 1 && !this.isDone) {//verify if u can just hold reeling button
            this.isDone = true;
            this.treasureHookedTicks = 60;
            return;
        }
        if (this.progress >= 1 && !this.isDone){
            this.isDone = true;
            treasureHookedTicks = 60;
            return;
        }
        ticks++;
        treasureHookedTicks--;
        if (this.isDone) {
            return;
        }
        float gain = parent.isReeling() && this.canReel ? 0.05f : -decay;
        if (parent.isReeling()) {
            this.canReel = false;
        }
        this.progress = MathHelper.clamp(this.progress + gain, 0f, 1f);
    }

    public boolean isDone() {
        return this.isDone;
    }

    public boolean isActive() {
        return treasureHookedTicks > 0;
    }

    public ArrayList<ItemStack> getRewards(){
        return isWon() ? treasureReward.getContent() : new ArrayList<>();
    }

    public float getProgress() {
        return this.progress;
    }

    public int getTimeLeft() {
        return (int) (treasureHookedTicks / 20f);
    }

    public int getTicksLeft() {
        return this.treasureHookedTicks;
    }

    public void letReel() {
        this.canReel = true;
    }
}
