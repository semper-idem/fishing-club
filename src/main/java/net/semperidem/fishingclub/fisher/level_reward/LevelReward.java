package net.semperidem.fishingclub.fisher.level_reward;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.item.IllegalGoodsItem;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class LevelReward {
    Amount amount;
    @Nullable ItemStack itemReward;
    RewardType rewardType;
    LevelUpEffect effect;

    private LevelReward(@Nullable ItemStack itemReward, Amount amount, RewardType rewardType) {
        this.itemReward = itemReward;
        this.amount = amount;
        this.rewardType = rewardType;
    }

    private LevelReward(Amount amount, RewardType rewardType) {
        this.amount = amount;
        this.rewardType = rewardType;
    }

    private LevelReward(LevelUpEffect effect){
        this.effect = effect;
        this.rewardType = RewardType.EFFECT;
    }


    private ItemStack grantBoxReward(int level){
        Random r = new Random();
        float levelBuff = 0.5f + Math.min(2, level / 50f);
        int boxTier = (int) Math.max(1, Math.min(5, Math.abs(r.nextGaussian()) * levelBuff));
        return IllegalGoodsItem.getStackWithTier(boxTier);
    }

    private ItemStack grantItemReward(int amount){
        if (itemReward == null) throw new IllegalStateException("Item reward is null");
        ItemStack rewardStack = itemReward.copy();
        int stackCount = rewardStack.getCount();
        rewardStack.setCount(stackCount * amount);
        return rewardStack;
    }

    public void grant(FishingCard fishingCard){
        int fisherLevel = fishingCard.getLevel();
        int resultAmount = amount == null ? 1 : amount.get(fisherLevel);
        switch (rewardType) {
            case ITEM -> fishingCard.addUnclaimedReward(grantItemReward(resultAmount));
            case CREDIT -> fishingCard.addCredit(resultAmount);
            case SKILL_POINT -> fishingCard.addSkillPoints(resultAmount);
            case BOX -> fishingCard.addUnclaimedReward(grantBoxReward(fisherLevel));
            case EFFECT -> executeEffect(fishingCard);
            default -> throw new IllegalStateException("Unexpected value: " + rewardType);
        }
    }

    private void executeEffect(FishingCard fishingCard) {
        if (fishingCard.getHolder() instanceof ServerPlayerEntity serverFisher) {
            ServerWorld world = serverFisher.getServerWorld();
            double x = serverFisher.getX();
            double y = serverFisher.getY();
            double z = serverFisher.getZ();
            effect.execute(world, x, y, z);
        }
    }

    static LevelReward effectReward(LevelUpEffect effect) {
        return new LevelReward(effect);
    }

    static LevelReward itemReward(ItemStack itemStack, Amount amount){
        return new LevelReward(itemStack, amount, RewardType.ITEM);
    }

    static LevelReward itemReward(ItemStack itemStack, int amount){
        return new LevelReward(itemStack, Amount.of(amount), RewardType.ITEM);
    }

    static LevelReward illegalGoodsReward(){
        return new LevelReward(IllegalGoodsItem.getStackWithTier(1), Amount.of(1), RewardType.BOX);
    }

    static LevelReward itemReward(ItemStack itemStack){
        return itemReward(itemStack, Amount.of(1));
    }

    static LevelReward skillPointReward(Amount amount){
        return new LevelReward(amount, RewardType.SKILL_POINT);
    }
    static LevelReward skillPointReward(int amount){
        return new LevelReward(Amount.of(amount), RewardType.SKILL_POINT);
    }

    static LevelReward creditReward(Amount amount){
        return new LevelReward(amount, RewardType.CREDIT);
    }
    static LevelReward creditReward(int amount){
        return new LevelReward(Amount.of(amount), RewardType.CREDIT);
    }

    static class Amount{
        int base;
        int gain;
        int every;

        Amount(int base, int gain, int every) {
            this(base, gain);
            this.every = Math.max(1, every);
        }
        Amount(int base, int gain) {
            this(base);
            this.gain = Math.max(0, gain);
        }
        Amount(int base) {
            this.base = Math.max(0, base);
        }

        public int get(int level){
            if (every == 0) return base;
            return base + ((level / every)) * gain;
        }

        public static Amount of(int base){
            return new Amount(base);
        }
    }
}



enum RewardType{
    CREDIT,
    ITEM,
    SKILL_POINT,
    BOX,
    EFFECT
}