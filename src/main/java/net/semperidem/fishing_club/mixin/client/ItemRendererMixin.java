package net.semperidem.fishing_club.mixin.client;


import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.registry.FCItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useFishModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (stack.isOf(FCItems.FISH) && renderMode != ModelTransformationMode.GUI) {
            return ((ItemRendererAccessor) this)
              .fishing_club$getModels()
              .getModelManager()
              .getModel(new ModelIdentifier(
                FishingClub.getIdentifier("butterfish_item_3d"),
                "inventory"
                )
              );
        }
        return value;
    }
}