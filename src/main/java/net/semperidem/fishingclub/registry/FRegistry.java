package net.semperidem.fishingclub.registry;

import net.semperidem.fishingclub.fisher.level_reward.LevelRewardRule;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;

public class FRegistry {

    public static void register(){
        FBlockRegistry.register();
        FItemRegistry.register();
        FEntityRegistry.register();
        FNetworkRegistry.register();
        FScreenHandlerRegistry.register();
        FStatusEffectRegistry.register();
        EnchantmentRegistry.register();
        LevelRewardRule.initDefaultRewards();
        FishingPerks.register();
    }

    public static void registerClient(){
        FNetworkRegistry.registerClient();
        FScreenHandlerRegistry.registerClient();
        FKeybindingRegistry.registerClient();
        FModelPredicateProviderRegistry.registerClient();
    }
}
