package net.semperidem.fishing_club.mixin.client;


import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.semperidem.fishing_club.fish.FishRecord;
import net.semperidem.fishing_club.item.FishItem;
import net.semperidem.fishing_club.registry.FCComponents;
import net.semperidem.fishing_club.registry.FCModels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useFishModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (FishItem.isFish(stack) && renderMode != ModelTransformationMode.GUI) {
            return ((ItemRendererAccessor) this)
              .fishing_club$getModels()
              .getModelManager()
              .getModel(FCModels.getModelId(stack.getOrDefault(FCComponents.FISH, FishRecord.DEFAULT))
              );
        }
        return value;
    }
}