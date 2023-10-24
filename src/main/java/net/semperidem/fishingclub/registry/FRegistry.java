package net.semperidem.fishingclub.registry;

import net.semperidem.fishingclub.fisher.level_reward.LevelRewardRule;

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
    }

    public static void registerClient(){
        FNetworkRegistry.registerClient();
        FScreenHandlerRegistry.registerClient();
        FKeybindingRegistry.registerClient();
        FModelPredicateProviderRegistry.registerClient();
    }
}
