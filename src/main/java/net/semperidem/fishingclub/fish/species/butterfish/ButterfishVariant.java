package net.semperidem.fishingclub.fish.species.butterfish;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.VariantSelectorProvider;
import net.minecraft.entity.spawn.SpawnCondition;
import net.minecraft.entity.spawn.SpawnConditionSelectors;
import net.minecraft.entity.spawn.SpawnContext;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.ModelAndTexture;
import net.minecraft.util.StringIdentifiable;
import net.semperidem.fishingclub.registry.RegistryKeys;

import java.util.List;

public record ButterfishVariant(
        ModelAndTexture<Model> modelAndTexture, 
        SpawnConditionSelectors spawnConditions
) implements VariantSelectorProvider<SpawnContext, SpawnCondition> {

    public static final Codec<ButterfishVariant> CODEC = RecordCodecBuilder.create((instance) -> instance.group(ModelAndTexture.createMapCodec(ButterfishVariant.Model.CODEC, ButterfishVariant.Model.NORMAL).forGetter(ButterfishVariant::modelAndTexture), SpawnConditionSelectors.CODEC.fieldOf("spawn_conditions").forGetter(ButterfishVariant::spawnConditions)).apply(instance, ButterfishVariant::new));
    public static final Codec<ButterfishVariant> NETWORK_CODEC = RecordCodecBuilder.create((instance) -> instance.group(ModelAndTexture.createMapCodec(ButterfishVariant.Model.CODEC, ButterfishVariant.Model.NORMAL).forGetter(ButterfishVariant::modelAndTexture)).apply(instance, ButterfishVariant::new));
    public static final Codec<RegistryEntry<ButterfishVariant>> ENTRY_CODEC;
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<ButterfishVariant>> ENTRY_PACKET_CODEC;

    private ButterfishVariant(ModelAndTexture<ButterfishVariant.Model> modelAndTexture) {
        this(modelAndTexture, SpawnConditionSelectors.EMPTY);
    }

    public ButterfishVariant(ModelAndTexture<ButterfishVariant.Model> modelAndTexture, SpawnConditionSelectors spawnConditions) {
        this.modelAndTexture = modelAndTexture;
        this.spawnConditions = spawnConditions;
    }

    public List<VariantSelectorProvider.Selector<SpawnContext, SpawnCondition>> getSelectors() {
        return this.spawnConditions.selectors();
    }

    public ModelAndTexture<ButterfishVariant.Model> modelAndTexture() {
        return this.modelAndTexture;
    }

    public SpawnConditionSelectors spawnConditions() {
        return this.spawnConditions;
    }

    static {
        ENTRY_CODEC = RegistryFixedCodec.of(RegistryKeys.BUTTERFISH_VARIANT);
        ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(RegistryKeys.BUTTERFISH_VARIANT);
    }

    public static enum Model implements StringIdentifiable {
        NORMAL("normal"),
        MELTED("melted");

        public static final Codec<ButterfishVariant.Model> CODEC = StringIdentifiable.createCodec(ButterfishVariant.Model::values);
        private final String id;

        private Model(final String id) {
            this.id = id;
        }

        public String asString() {
            return this.id;
        }
    }

}
