package net.semperidem.fishingclub.item.fishing_rod.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartItems;

import java.util.Optional;

public record RodPartComponent(
        ItemVariant partItem,
        Optional<ItemStack> partStack
){
    public static final RodPartComponent DEFAULT = new RodPartComponent(
            ItemVariant.of(FishingRodPartItems.EMPTY_COMPONENT),
            Optional.empty()
    );

    public static Codec<RodPartComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemVariant.CODEC.fieldOf("partItem").forGetter(RodPartComponent::partItem),
            ItemStack.CODEC.optionalFieldOf("partStack").forGetter(RodPartComponent::partStack)
    ).apply(instance, RodPartComponent::new));

    public static PacketCodec<RegistryByteBuf, RodPartComponent> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    public void apply(RodConfigurationController configurationController) {
        if (partItem instanceof ComponentItem componentItem && partStack.isPresent()) {
            componentItem.applyComponent(configurationController, partStack.get());
        }
    }
}
