package net.semperidem.fishingclub.registry;

import net.semperidem.fishingclub.fisher.level_reward.LevelRewardRule;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.screen.dialog.DialogUtil;

public class FishingClubRegistry {

// in the initializer
    public static void register(){
        BlockRegistry.register();
        ItemRegistry.register();
        EntityTypeRegistry.register();
        NetworkRegistry.registerCommon();
        ScreenHandlerRegistry.register();
        StatusEffectRegistry.register();
        LevelRewardRule.initDefaultRewards();
        FishingPerks.register();
        DialogUtil.register();
        ComponentRegistry.register();
        TagRegistry.register();
    }

    public static void registerClient(){
        NetworkRegistry.registerClient();
        ScreenHandlerRegistry.registerClient();
        KeybindingRegistry.registerClient();
        ModelPredicateProviderRegistry.registerClient();
    }
}
