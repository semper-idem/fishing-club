package net.semperidem.fishing_club.registry;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.semperidem.fishing_club.entity.renderer.FishermanEntityRenderer;
import net.semperidem.fishing_club.entity.renderer.HookEntityRenderer;
import net.semperidem.fishing_club.fisher.level_reward.LevelRewardRule;
import net.semperidem.fishing_club.fisher.perks.FishingPerks;
import net.semperidem.fishing_club.screen.dialog.DialogUtil;

public class FCRegistry {

// in the initializer
    public static void register(){
        FCComponents.register();
        FCTags.register();
        FCItems.register();
        FCEntityTypes.register();
        FCNetworking.registerServer();
        FCScreenHandlers.register();
        FCStatusEffects.register();
        LevelRewardRule.initDefaultRewards();
        FishingPerks.register();
        DialogUtil.register();
    }

    public static void registerClient(){
        FCNetworking.registerClient();
        FCScreenHandlers.registerClient();
        FCKeybindings.registerClient();
        FCModelPredicateProvider.registerClient();
        EntityRendererRegistry.register(FCEntityTypes.HOOK_ENTITY, HookEntityRenderer::new);
        EntityRendererRegistry.register(FCEntityTypes.DEREK_ENTITY, FishermanEntityRenderer::new);
    }
}
