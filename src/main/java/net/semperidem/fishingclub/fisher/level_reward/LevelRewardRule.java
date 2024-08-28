package net.semperidem.fishingclub.fisher.level_reward;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.semperidem.fishingclub.fisher.level_reward.LevelReward.Amount;
import net.semperidem.fishingclub.registry.FCItems;

import java.util.ArrayList;
import java.util.List;

import static net.semperidem.fishingclub.fisher.level_reward.LevelReward.*;

public class LevelRewardRule {
    private static final ArrayList<LevelRewardRule> LEVEL_REWARD_RULES = new ArrayList<>();//This could've been hashmap with levels being keys
    private static final int MAX_LEVEL = 9001;

    private ArrayList<LevelReward> rewards;
    int repeating; //every x levels
    int from;
    int to;

    private LevelRewardRule(int from, int to, int repeating) {
        this.from = from;
        this.to = to;
        this.repeating = repeating;
    }

    public ArrayList<LevelReward> getRewards() {
        return rewards;
    }

    public LevelRewardRule withRewards(LevelReward... rewards){
        this.rewards = new ArrayList<>(List.of(rewards));
        return this;
    }

    public static LevelRewardRule create(int from, int to, int every){
        return new LevelRewardRule(from, to, every);
    }

    public static LevelRewardRule create(int from, int to){
        return new LevelRewardRule(from, to, 1);
    }

    private static void addRule(LevelRewardRule rule){
        LEVEL_REWARD_RULES.add(rule);
    }

    //TODO DONT INIT BEOFRE ITS NEEDED (server loaded)
    public static void initDefaultRewards() {//TODO READ FROM CONFIGURED REWARDS HERE
        //SKILL POINTS
        addRule(create(1,5).withRewards(skillPointReward(1))); //5
        addRule(create(6,15, 2).withRewards(skillPointReward(1))); // 5
        addRule(create(16,30, 3).withRewards(skillPointReward(1))); // 5
        addRule(create(31,50, 4).withRewards(skillPointReward(1))); // 5
        addRule(create(51, 100, 5).withRewards(skillPointReward(1))); // 10
        addRule(create(101, 1601, 100).withRewards(skillPointReward(1))); // 15 //TODO Remove when adding prestige

        //CREDIT
        addRule(create(0,MAX_LEVEL).withRewards(creditReward(new Amount(0, 10, 1))));

        //ITEMS

        //Fishing Rods //TODO ADD PARTS TO SUIT LEVEL
        addRule(create(5, 0, 0).withRewards(itemReward(FCItems.CORE_IRON.getDefaultStack(), 1)));
        addRule(create(50, 0, 0).withRewards(itemReward(FCItems.CORE_IRON.getDefaultStack(), 1)));

        //Trophy //TODO ADD TROPHY, CHANGE PER PRESTIGE
        addRule(create(100, 0, 0).withRewards(itemReward(new ItemStack(Items.DIAMOND), 1)));

        //Lootbox
        addRule(create(0, MAX_LEVEL, 5).withRewards(illegalGoodsReward()));

        //EFFECT
        addRule(create(0, MAX_LEVEL).withRewards(effectReward(LevelUpEffect.COMMON_EFFECT)));
        addRule(create(0, MAX_LEVEL, 5).withRewards(effectReward(LevelUpEffect.UNCOMMON_EFFECT)));
        addRule(create(0, MAX_LEVEL, 100).withRewards(effectReward(LevelUpEffect.RARE_EFFECT)));

    }

    private static boolean isAtLevel(LevelRewardRule rule, int level) {
        if (isRepeatedAtLevel(rule, level)) return true;
        return rule.repeating == 0 && (level == rule.from);
    }

    private static boolean isRepeatedAtLevel(LevelRewardRule rule, int level){
        if (rule.repeating <= 0) return false;
        if (level < rule.from || level > rule.to) return false;
        return (level - rule.from) % rule.repeating == 0;
    }

    public static ArrayList<LevelReward> getRewardForLevel(int level) {
        ArrayList<LevelReward> rewards = new ArrayList<>();
        LEVEL_REWARD_RULES.stream().filter(rule -> isAtLevel(rule, level)).forEach(rule -> rewards.addAll(rule.rewards));
        return rewards;
    }



}
