package net.semperidem.fishingclub.game;

import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.game.treasure.Reward;
import net.semperidem.fishingclub.game.treasure.Rewards;

import java.util.ArrayList;

public class TreasureGameController {
    private static final float TREASURE_MAX_SPOT_SIZE = 0.225f;
    private static final float TREASURE_GRADE_TO_SPOT_SIZE_RATIO = 0.025f;

    private float arrowSpeed;
    private float treasureSpotSize;
    private Reward treasureReward;

    private float arrowPos;
    private float nextArrowPos;
    private int treasureHookedTicks;
    private int ticks;
    private boolean isWon;

    public void start(FishingCard fishingCard) {
        this.treasureReward = Rewards.roll(fishingCard);
        this.arrowSpeed = 1 + treasureReward.getGrade();
        this.treasureSpotSize = TREASURE_MAX_SPOT_SIZE - (treasureReward.getGrade() * TREASURE_GRADE_TO_SPOT_SIZE_RATIO);
        this.treasureHookedTicks = (8 - treasureReward.getGrade()) * 60;
    }


    public void tick(boolean isReeling) {
        if (treasureHookedTicks == 0) {
            isWon = false;
            return;
        }
        if (isReeling && canReelTreasure()) {
            isWon = true;
            treasureHookedTicks = 0;
            return;
        }
        ticks++;
        arrowPos = nextArrowPos;
        nextArrowPos = getNextArrowPos();
        treasureHookedTicks--;
    }

    private boolean canReelTreasure(){
        return (arrowPos >= (0.5f - treasureSpotSize / 2)) && (arrowPos <= (0.5f + treasureSpotSize));
    }

    public boolean isActive() {
        return treasureHookedTicks > 0;
    }

    public ArrayList<ItemStack> getRewards(){
        return isWon ? treasureReward.getContent() : new ArrayList<>();
    }

    public float getNextArrowPos(){
        return ((float) Math.sin(ticks / (12f - arrowSpeed)) + 1) / 2;
    }


    public float getArrowPos() {
        return arrowPos;
    }

    public float getTreasureSpotSize() {
        return treasureSpotSize;
    }

    public int getTimeLeft() {
        return (int) (treasureHookedTicks / 20f);
    }
}
