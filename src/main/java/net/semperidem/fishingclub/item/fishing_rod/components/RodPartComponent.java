package net.semperidem.fishingclub.item.fishing_rod.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.semperidem.fishingclub.registry.FCItems;

public record RodPartComponent(
        ItemVariant partItem,
        Optional<ItemStack> partStack
){
    public static final RodPartComponent DEFAULT = new RodPartComponent(
            ItemVariant.blank(),
            Optional.empty()
    );

    public static Codec<RodPartComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemVariant.CODEC.fieldOf("partItem").forGetter(RodPartComponent::partItem),
            ItemStack.CODEC.optionalFieldOf("partStack").forGetter(RodPartComponent::partStack)
    ).apply(instance, RodPartComponent::new));

    public static PacketCodec<RegistryByteBuf, RodPartComponent> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    public static RodPartComponent of(ItemStack partStack) {
        return new RodPartComponent(ItemVariant.of(partStack), partStack.isEmpty() ? Optional.empty() : Optional.of(partStack));
    }
    public void apply(RodConfigurationController configurationController) {
        if (partItem instanceof PartItem componentItem && partStack.isPresent()) {
            componentItem.applyComponent(configurationController, partStack.get());
        }
    }
    //ItemStack componentStack, int amount, DamageSource damageSource, T entity, Consumer<T> breakCallback
    public void damage(int amount, PartItem.DamageSource damageSource, LivingEntity entity) {
        if (!(partItem instanceof PartItem componentItem) || partStack.isEmpty()) {
            return;
        }
        componentItem.damage(partStack.get(), amount, damageSource, entity, e -> {
            e.sendEquipmentBreakStatus(componentItem, EquipmentSlot.MAINHAND);
        });
    }
}
