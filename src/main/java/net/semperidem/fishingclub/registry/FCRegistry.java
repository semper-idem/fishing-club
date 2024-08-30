package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.renderer.CustomBoatEntityRenderer;
import net.semperidem.fishingclub.entity.renderer.FishDisplayEntityRenderer;
import net.semperidem.fishingclub.entity.renderer.FishermanEntityRenderer;
import net.semperidem.fishingclub.entity.renderer.HookEntityRenderer;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.fisher.level_reward.LevelRewardRule;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.screen.dialog.DialogController;

import java.util.HashMap;

public class FCRegistry {

    public static HashMap<String, SoundEvent> SPECIES_TO_TUNE = new HashMap<>();
    public static final SoundEvent YELLOW_FISH_TUNE = registerSoundEvent("yellow_fish_tune");
    static {
      //  SPECIES_TO_TUNE.put(SpeciesLibrary.BUTTERFISH.label, YELLOW_FISH_TUNE);
    }


      private static SoundEvent registerSoundEvent(String name) {
        Identifier id = FishingClub.identifier(name);
        return Registry.register(Registries.SOUND_EVENT, FishingClub.identifier(name), SoundEvent.of(id));
      }

// in the initializer
    public static void register(){
        FCTags.register();
        FCBlocks.register();
        FCItems.register();
        FCEntityTypes.register();
        FCComponents.register();
        FCNetworking.registerServer();
        FCScreenHandlers.register();
        FCStatusEffects.register();
        LevelRewardRule.initDefaultRewards();
        FishingPerks.register();
        DialogController.initialize();
        FCFeatures.register();
        FCLootTables.register();
    }

    public static void registerClient(){
        FCNetworking.registerClient();
        FCScreenHandlers.registerClient();
        FCKeybindings.registerClient();
        FCModelPredicateProvider.registerClient();
        FCModels.initModels();
        Species.Library.registerClient();
        EntityRendererRegistry.register(FCEntityTypes.HOOK_ENTITY, HookEntityRenderer::new);
        EntityRendererRegistry.register(FCEntityTypes.DEREK_ENTITY, FishermanEntityRenderer::new);
        EntityRendererRegistry.register(FCEntityTypes.BOAT_ENTITY, (EntityRendererFactory.Context ctx) -> new CustomBoatEntityRenderer(ctx, false));
        BlockEntityRendererFactories.register(FCBlocks.FISH_DISPLAY, FishDisplayEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(FCBlocks.REED_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FCBlocks.ENERGY_DENSE_KELP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FCBlocks.ENERGY_DENSE_KELP_PLANT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FCBlocks.NUTRITIOUS_KELP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FCBlocks.NUTRITIOUS_KELP_PLANT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FCBlocks.WATERLOGGED_LILY_PAD_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FCBlocks.DUCKWEED_BLOCK, RenderLayer.getCutout());
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> 2129968, FCBlocks.WATERLOGGED_LILY_PAD_BLOCK);
    }

    //Server-side only
    // this is so we can modify vanilla fishing rod
}
