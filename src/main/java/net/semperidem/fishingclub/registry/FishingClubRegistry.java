package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.semperidem.fishingclub.entity.renderer.FishermanEntityRenderer;
import net.semperidem.fishingclub.fisher.level_reward.LevelRewardRule;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.screen.dialog.DialogUtil;

import javax.swing.text.html.parser.Entity;

public class FishingClubRegistry {

// in the initializer
    public static void register(){
        BlockRegistry.register();
        ItemRegistry.register();
        EntityTypeRegistry.register();
        NetworkRegistry.registerServer();
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
        EntityRendererRegistry.register(EntityTypeRegistry.HOOK_ENTITY, FishingBobberEntityRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.DEREK_ENTITY, FishermanEntityRenderer::new);
    }
}
