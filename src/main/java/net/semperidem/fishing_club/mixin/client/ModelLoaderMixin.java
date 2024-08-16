package net.semperidem.fishing_club.mixin.client;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.BlockStatesLoader;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.semperidem.fishing_club.fish.SpeciesLibrary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {

    @Shadow
    protected abstract void loadItemModel(ModelIdentifier modelId);

    @Inject(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/model/ModelLoader;loadItemModel(Lnet/minecraft/client/util/ModelIdentifier;)V"
        )
    )
    private void fishing_club$addFishModel(
        BlockColors blockColors,
        Profiler profiler,
        Map<Identifier, JsonUnbakedModel> jsonUnbakedModels,
        Map<Identifier, List<BlockStatesLoader.SourceTrackedData>> blockStates,
        CallbackInfo ci
    ) {
       SpeciesLibrary.iterator().forEachRemaining(species -> {
           this.loadItemModel(species.setAndGetModelId());
           this.loadItemModel(species.setAndGetAlbinoModelId());
       });

    }

}