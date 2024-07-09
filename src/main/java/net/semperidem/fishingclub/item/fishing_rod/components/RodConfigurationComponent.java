package net.semperidem.fishingclub.item.fishing_rod.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.semperidem.fishingclub.registry.FCComponents;

import java.util.Optional;

public record RodConfigurationComponent(
        RodPartComponent coreComponent,
        RodPartComponent lineComponent,
        Integer maxLineLength,
        Float castPower,
        Integer weightCapacity,
        Integer weightMagnitude,
        Boolean canCast
) {

    public static final RodConfigurationComponent DEFAULT = new RodConfigurationComponent(
            RodPartComponent.DEFAULT,
            RodPartComponent.DEFAULT,
            8,
            1f,
            9999,
            0,
            true
    );

    public static Codec<RodConfigurationComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RodPartComponent.CODEC.fieldOf("coreComponent").forGetter(RodConfigurationComponent::coreComponent),
            RodPartComponent.CODEC.fieldOf("lineComponent").forGetter(RodConfigurationComponent::lineComponent),
            Codec.INT.fieldOf("maxLineLength").forGetter(RodConfigurationComponent::maxLineLength),
            Codec.FLOAT.fieldOf("castPower").forGetter(RodConfigurationComponent::castPower),
            Codec.INT.fieldOf("weightCapacity").forGetter(RodConfigurationComponent::weightCapacity),
            Codec.INT.fieldOf("weightMagnitude").forGetter(RodConfigurationComponent::weightMagnitude),
            Codec.BOOL.fieldOf("canCast").forGetter(RodConfigurationComponent::canCast)
    ).apply(instance, RodConfigurationComponent::new));

    public static PacketCodec<RegistryByteBuf, RodConfigurationComponent>  PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    public static RodConfigurationComponent of(ItemStack fishingRod) {
        return fishingRod.getOrDefault(FCComponents.ROD_CONFIGURATION, DEFAULT);
    }

    public RodConfigurationComponent equipCore(RodPartComponent core) {
        return equip(core, this.lineComponent).toRecord();
    }

    public RodConfigurationComponent equipLine(RodPartComponent line) {
        return equip(this.coreComponent, line).toRecord();
    }

    private RodConfigurationController equip(
            RodPartComponent core,
            RodPartComponent line
    ) {
        return new RodConfigurationController(new RodConfigurationComponent(
                core,
                line,
                DEFAULT.maxLineLength,
                DEFAULT.castPower,
                DEFAULT.weightCapacity,
                DEFAULT.weightMagnitude,
                DEFAULT.canCast
        ));
    }

    public void damage(int amount, PartItem.DamageSource damageSource, LivingEntity entity){
        coreComponent.damage(amount, damageSource, entity);
        lineComponent.damage(amount, damageSource, entity);
    }

    public SimpleInventory getParts() {
        SimpleInventory parts = new SimpleInventory(5) {
            @Override
            public void markDirty() {
                super.markDirty();
            }
        };
        parts.setStack(0, coreComponent.partStack().orElse(ItemStack.EMPTY));
        parts.setStack(1, lineComponent.partStack().orElse(ItemStack.EMPTY));
        parts.setStack(2, ItemStack.EMPTY);
        parts.setStack(3, ItemStack.EMPTY);
        parts.setStack(4, ItemStack.EMPTY);
        return parts;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RodConfigurationComponent other)) {
            return false;
        }
        boolean areEqual = true;
        areEqual &= coreComponent.equals(other.coreComponent);
        areEqual &= lineComponent.equals(other.lineComponent);
        return areEqual;
    }

    @Override
    public String toString() {
        return """
                        maxLineLength: %s
                        castPower: %s
                        weightCapacity: %s
                        weightMagnitude: %s
                        canCast: %s
                """.formatted(maxLineLength, castPower, weightCapacity, weightMagnitude, canCast);
    }
}

