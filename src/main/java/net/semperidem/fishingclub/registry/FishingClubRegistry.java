package net.semperidem.fishingclub.registry;

import net.semperidem.fishingclub.client.screen.dialog.DialogHelper;
import net.semperidem.fishingclub.fisher.level_reward.LevelRewardRule;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;

public class FishingClubRegistry {

    public static void register(){
        BlockRegistry.register();
        ItemRegistry.register();
        EntityTypeRegistry.register();
        NetworkRegistry.register();
        ScreenHandlerRegistry.register();
        StatusEffectRegistry.register();
        EnchantmentRegistry.register();
        LevelRewardRule.initDefaultRewards();
        FishingPerks.register();
        DialogHelper.register();
    }

    public static void registerClient(){
        NetworkRegistry.registerClient();
        ScreenHandlerRegistry.registerClient();
        KeybindingRegistry.registerClient();
        ModelPredicateProviderRegistry.registerClient();
    }
}
