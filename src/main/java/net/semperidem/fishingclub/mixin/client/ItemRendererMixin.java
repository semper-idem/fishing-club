package net.semperidem.fishingclub.mixin.client;


import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.item.FishItem;
import net.semperidem.fishingclub.registry.FCComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useFishModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (FishItem.isFish(stack) && renderMode != ModelTransformationMode.GUI) {
            return ((ItemRendererAccessor) this)
              .fishingclub$getModels()
              .getModelManager()
              .getModel(stack.getOrDefault(FCComponents.SPECIMEN, SpecimenData.DEFAULT).getModelId()
              );
        }
        return value;
    }
}
