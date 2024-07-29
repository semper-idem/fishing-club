package net.semperidem.fishing_club.registry;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.entity.renderer.CustomBoatEntityRenderer;
import net.semperidem.fishing_club.entity.renderer.FishDisplayEntityRenderer;
import net.semperidem.fishing_club.entity.renderer.FishermanEntityRenderer;
import net.semperidem.fishing_club.entity.renderer.HookEntityRenderer;
import net.semperidem.fishing_club.fisher.level_reward.LevelRewardRule;
import net.semperidem.fishing_club.fisher.perks.FishingPerks;
import net.semperidem.fishing_club.screen.dialog.DialogController;

import java.util.HashMap;

public class FCRegistry {

    public static HashMap<String, SoundEvent> SPECIES_TO_TUNE = new HashMap<>();
    public static final SoundEvent YELLOW_FISH_TUNE = registerSoundEvent("yellow_fish_tune");
    static {
      //  SPECIES_TO_TUNE.put(SpeciesLibrary.BUTTERFISH.name, YELLOW_FISH_TUNE);
    }


      private static SoundEvent registerSoundEvent(String name) {
        Identifier id = FishingClub.getIdentifier(name);
        return Registry.register(Registries.SOUND_EVENT, FishingClub.getIdentifier(name), SoundEvent.of(id));
      }

// in the initializer
    public static void register(){
        FCComponents.register();
        FCTags.register();
        FCBlocks.register();
        FCItems.register();
        FCEntityTypes.register();
        FCNetworking.registerServer();
        FCScreenHandlers.register();
        FCStatusEffects.register();
        LevelRewardRule.initDefaultRewards();
        FishingPerks.register();
        DialogController.initialize();
    }

    public static void registerClient(){
        FCNetworking.registerClient();
        FCScreenHandlers.registerClient();
        FCKeybindings.registerClient();
        FCModelPredicateProvider.registerClient();
        FCModels.initModels();
        EntityRendererRegistry.register(FCEntityTypes.HOOK_ENTITY, HookEntityRenderer::new);
        EntityRendererRegistry.register(FCEntityTypes.DEREK_ENTITY, FishermanEntityRenderer::new);
        EntityRendererRegistry.register(FCEntityTypes.BOAT_ENTITY, (EntityRendererFactory.Context ctx) -> new CustomBoatEntityRenderer(ctx, false));
        BlockEntityRendererFactories.register(FCBlocks.FISH_DISPLAY, FishDisplayEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(FCBlocks.REED_BLOCK, RenderLayer.getCutout());
    }
}
