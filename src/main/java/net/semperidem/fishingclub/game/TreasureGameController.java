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
    private float arrowPos;
    private float nextArrowPos;
    private float treasureSpotSize;
    private int treasureHookedTicks;
    private Reward treasureReward;
    private int ticks;
    FishGameController parent;

    public TreasureGameController(FishGameController parent, FishingCard fishingCard) {
        this.parent = parent;
        this.treasureReward = Rewards.roll(fishingCard);
        this.arrowSpeed = 1 + treasureReward.getGrade();
        this.treasureSpotSize = TREASURE_MAX_SPOT_SIZE - (treasureReward.getGrade() * TREASURE_GRADE_TO_SPOT_SIZE_RATIO);
        this.treasureHookedTicks = (8 - treasureReward.getGrade()) * 60;
    }


    public void tick(boolean isReeling) {
        if (treasureHookedTicks == 0) {
            parent.endTreasureGame(false);
        }
        if (isReeling && isTreasureReeled()) {
            parent.endTreasureGame(true);
        }
        ticks++;
        arrowPos = nextArrowPos;
        nextArrowPos = getNextArrowPos();
        treasureHookedTicks--;
    }

    public ArrayList<ItemStack> getRewards(){
        return treasureReward.getContent();
    }

    public float getNextArrowPos(){
        return ((float) Math.sin(ticks / (12f - arrowSpeed)) + 1) / 2;
    }
    private boolean isTreasureReeled(){
        return (arrowPos >= (0.5f - treasureSpotSize / 2)) && (arrowPos <= (0.5f + treasureSpotSize));
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
