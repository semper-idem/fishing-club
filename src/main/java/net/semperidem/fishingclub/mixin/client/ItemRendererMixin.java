package net.semperidem.fishingclub.mixin.client;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.TropicalFishColorFeatureRenderer;
import net.minecraft.client.render.entity.model.LargeTropicalFishEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.RotationAxis;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.mixin.common.TropicalFishAccessor;
import net.semperidem.fishingclub.registry.FCComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useFishModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!FishUtil.isFish(stack)) {
            return value;
        }
        if (renderMode == ModelTransformationMode.GUI) {
            return value;
        }
        String itemName = stack.getItem().toString();
        String speciesName = itemName.substring(itemName.lastIndexOf(":") + 1);
        SpecimenData data = stack.get(FCComponents.SPECIMEN);
            return ((ItemRendererAccessor) this)
              .fishingclub$getModels()
              .getModelManager()
              .getModel(data == null ? Species.Library.fromName(speciesName).modelId() : data.getModelId());
    }

    @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(value ="INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V"), cancellable = true)
    private void fishing_club$renderTropicalFish(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if (!stack.isOf(Items.TROPICAL_FISH)){
            return;
        }
        if (renderMode == ModelTransformationMode.GUI) {
            return;
        }
        if (MinecraftClient.getInstance().world == null) {
            return;
        }
        int variant = TropicalFishEntity.COMMON_VARIANTS.get(5).getId();
        SpecimenData data = stack.get(FCComponents.SPECIMEN);
        if (data != null) {
            variant = TropicalFishEntity.COMMON_VARIANTS.get(data.subspecies()).getId();
        }
        TropicalFishEntity tropicalFish = new TropicalFishEntity(EntityType.TROPICAL_FISH, MinecraftClient.getInstance().world);
        tropicalFish.getDataTracker().set(TropicalFishAccessor.getVariant(), variant);

        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90));
        MinecraftClient.getInstance().getEntityRenderDispatcher().render(tropicalFish, 0.5, -0.7,0.6, 0, 0,matrices, vertexConsumers, 0xFFFFFF);

        matrices.pop();


        matrices.pop();
        ci.cancel();
    }
}
