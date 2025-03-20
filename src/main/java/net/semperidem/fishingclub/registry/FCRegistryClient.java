package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.semperidem.fishingclub.entity.renderer.*;
import net.semperidem.fishingclub.fish.Species;

public class FCRegistryClient {


    public static void register(){
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


}
