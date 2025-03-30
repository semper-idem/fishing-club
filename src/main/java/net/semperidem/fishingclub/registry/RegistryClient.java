package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.semperidem.fishingclub.entity.renderer.*;
import net.semperidem.fishingclub.fish.Species;

public class RegistryClient {


    public static void register(){
        Networking.registerClient();
        ScreenHandlers.registerClient();
        Keybindings.registerClient();
        EntityModelLayers.register();
        EntityRendererRegistry.register(EntityTypes.HOOK, HookEntityRenderer::new);
        EntityRendererRegistry.register(EntityTypes.FISHERMAN, FishermanEntityRenderer::new);
        EntityRendererRegistry.register(EntityTypes.BOAT, (EntityRendererFactory.Context ctx) -> new CustomBoatEntityRenderer(ctx, net.minecraft.client.render.entity.model.EntityModelLayers.DARK_OAK_CHEST_BOAT));
        BlockEntityRendererFactories.register(Blocks.FISH_DISPLAY, FishDisplayEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.REED_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ENERGY_DENSE_KELP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.ENERGY_DENSE_KELP_PLANT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NUTRITIOUS_KELP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NUTRITIOUS_KELP_PLANT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.WATERLOGGED_LILY_PAD_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.DUCKWEED_BLOCK, RenderLayer.getCutout());
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> 2129968, Blocks.WATERLOGGED_LILY_PAD_BLOCK);
    }

}
