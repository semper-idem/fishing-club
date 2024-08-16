package net.semperidem.fishingclub.game;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.semperidem.fishingclub.game.treasure.Reward;
import net.semperidem.fishingclub.game.treasure.Rewards;
import net.semperidem.fishingclub.network.payload.FishingGameTickPayload;

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
    private boolean isWon = false;
    private final FishingGameController parent;


    public void consumeData(FishingGameTickPayload payload) {
        arrowPos = payload.arrowPos();
        treasureHookedTicks = payload.treasureHookedTicks();
        isWon = payload.isWon();
    }

    public void writeData(PacketByteBuf buf) {
        buf.writeFloat(arrowPos);
        buf.writeInt(treasureHookedTicks);
        buf.writeBoolean(isWon);
    }

    public boolean isWon(){
        return this.isWon;
    }

    public int getTreasureHookedTicks(){
        return this.treasureHookedTicks;
    }

    public TreasureGameController(FishingGameController parent) {
        this.parent = parent;
        this.treasureReward = Rewards.roll(parent.fishingCard);
        this.arrowSpeed = 1 + treasureReward.getGrade();
        this.treasureSpotSize = TREASURE_MAX_SPOT_SIZE - (treasureReward.getGrade() * TREASURE_GRADE_TO_SPOT_SIZE_RATIO);
    }

    public void start() {
        this.treasureHookedTicks = (8 - treasureReward.getGrade()) * 60;
    }

    public void tick() {
        if (treasureHookedTicks == 0) {//verify if u can just hold reeling button
            isWon = false;
            return;
        }
        if (parent.isReeling() && canReelTreasure()) {
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
