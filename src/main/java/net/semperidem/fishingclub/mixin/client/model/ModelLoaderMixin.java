package net.semperidem.fishingclub.mixin.client.model;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.BlockStatesLoader;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.semperidem.fishingclub.fish.Species;
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
    private void fishingclub$addFishModel(
        BlockColors blockColors,
        Profiler profiler,
        Map<Identifier, JsonUnbakedModel> jsonUnbakedModels,
        Map<Identifier, List<BlockStatesLoader.SourceTrackedData>> blockStates,
        CallbackInfo ci
    ) {
        Species.Library.iterator().forEachRemaining(species -> {
           this.loadItemModel(species.modelId());
           this.loadItemModel(species.albinoModelId());
       });

    }

}