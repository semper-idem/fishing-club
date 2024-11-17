package net.semperidem.fishingclub.fish.specimen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.semperidem.fishingclub.entity.FishingExplosionEntity;
import net.semperidem.fishingclub.entity.HookEntity;
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishingclub.registry.FCComponents;
import net.semperidem.fishingclub.util.MathUtil;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public record SpecimenData(
        Species<?> species,
        String label,
        int level,
        int quality,
        float weight,
        float length,
        UUID id,
        UUID caughtByUUID,
        String caughtBy,
        long caughtAt,
        boolean isAlive,
        int subspecies
) {

    public SpecimenData(
            String speciesName,
            String label,
            int level,
            int quality,
            float weight,
            float length,
            UUID id,
            UUID caughtByUUID,
            String caughtBy,
            long caughtAt,
            boolean isAlive,
            int subspecies
    ) {
        this(
            Species.Library.fromName(speciesName),
            label,
            level,
            quality,
            weight,
            length,
            id,
            caughtByUUID,
            caughtBy,
            caughtAt,
            isAlive,
            subspecies
        );

    }
    public static SpecimenData init() {

        return init(Species.Library.DEFAULT);
    }

    public static SpecimenData init(Species<?> species) {

        var isAlbino = Math.random() < 0.01f;
        return init(new IHookEntity() {}, species, isAlbino ? -1 : 0);
    }

    public static SpecimenData init(Species<TropicalFishEntity> species, int subspecies) {
        return init(new IHookEntity() {}, species, subspecies);
    }
    public static Optional<SpecimenData> init(IHookEntity caughtWith) {

        int luck = 0;
        FishingCard fishingCard = caughtWith.getFishingCard();
        if (fishingCard.knowsTradeSecret(TradeSecrets.FISH_WHISPERER)) {
            luck++;
        }
        if (fishingCard.holder() instanceof PlayerEntity holder) {
            luck += (int) holder.getLuck();
        }

        var isAlbino = Math.random() < (0.005f + luck * 0.001) * caughtWith.getWaitTime() / 1200;
        return species(caughtWith).map(value -> init(caughtWith, value, isAlbino ? -1 : 0));
    }

    private static Optional<Species<?>> species(IHookEntity iHookEntity) {
        if (iHookEntity instanceof HookEntity hookEntity) {
            return Species.Library.drawRandom(hookEntity);
        }
        return Species.Library.drawRandom(iHookEntity);
    }


    public ModelIdentifier getModelId() {
        return this.isAlbino() ? species().albinoModelId() : species().modelId();
    }

    public static SpecimenData init(IHookEntity caughtWith, Species<? extends WaterCreatureEntity> species, int subspecies) {

        FishingCard caughtByCard = caughtWith.getFishingCard();
        RodConfiguration caughtUsing = caughtWith.getCaughtUsing();
        PlayerEntity caughtBy = caughtByCard.unsafeHolder();

        int level = calculateLevel(species, caughtByCard.getLevel());
        int quality = calculateQuality(caughtWith, caughtByCard, caughtUsing);
        return init(species, level, quality, caughtBy, subspecies, !(caughtWith instanceof FishingExplosionEntity));
    }

    private static SpecimenData init(Species<? extends  WaterCreatureEntity> species, int level, int quality, PlayerEntity caughtBy, int subspecies, boolean isDead) {

        float weight = calculateWeight(species, level);
        float length = calculateLength(species, level);

        String label = (subspecies == -1 ? "Albino " : "") + species.label();
        if (Math.random() < GIANT_CHANCE) {
            double giantBase = MathHelper.clamp(Math.abs(Species.RANDOM.nextGaussian()) + 1.5, 1.5, 4);
            double giantScale = MathHelper.clamp(Math.abs(Species.RANDOM.nextGaussian()) + 1.5, 1.5, 4);
            double xGiant = Math.pow(giantBase, giantScale);
            weight = (float) (species.maxWeight() * xGiant);
            length = (float) (species.maxLength() * xGiant);
            label = "Gigantic " + label;
        }


        if (subspecies > 0) {
            label = Text.translatable(TropicalFishEntity.getToolTipForVariant(subspecies)).getString();
        }

        return new SpecimenData(
                species,
                label,
                level,
                quality,
                weight,
                length,
                UUID.randomUUID(),
                caughtBy == null ? UUID.randomUUID() : caughtBy.getUuid(),
                caughtBy == null ? "x" : caughtBy.getNameForScoreboard(),
                System.currentTimeMillis(),
                isDead,
                subspecies
        );
    }


    private static float calculateWeight(Species<? extends WaterCreatureEntity> species, int level) {
        return species.weightInRange(level * 0.01f);
    }

    private static float calculateLength(Species<? extends WaterCreatureEntity> species, int level) {
        return species.lengthInRange(level * 0.01f);
    }

    private static int calculateLevel(Species<? extends WaterCreatureEntity> species, int fisherLevel) {

        return (int) MathHelper.clamp(MathUtil.normal(
                        species.level(),
                        fisherLevel - species.level(),
                        fisherLevel * 0.01
                ), MIN_LEVEL, MAX_LEVEL
        );
    }

    private static int calculateQuality(IHookEntity caughtWith, FishingCard fisher, RodConfiguration rod) {
        double fisherMean = MathHelper.clamp(fisher.getLevel() * 0.005, 0, 0.5);
        double rodMean = rod.attributes().fishQuality() * 0.05;
        double circumstanceMean = caughtWith.getCircumstanceQuality();
        double mean = fisherMean + rodMean + circumstanceMean;

        return (int) MathHelper.clamp(
                MathUtil.normal(MIN_LEVEL, 4, MIN_LEVEL + mean),//non-boosting min quality buff impl
                fisher.unsafeHolder() == null ? 0 : fisher.minQuality(caughtWith),
                MAX_QUALITY
        );
    }

public int experience(double xFisher) {

        double xRarity = (50 / (this.species().rarity() + 9));//expected values from 1 to 200
        double xQuality = this.quality > 3 ? Math.pow(2, this.quality - 3) : 1;
        double xGiant = this.isGiant() ? 3 : 1;
        double xAlbino = this.subspecies == -1 ? 1.5 : 1;
        double xLevel = Math.pow(this.level, 1.3);

        return (int) MathHelper.clamp(
                BASE_EXP + xFisher * xLevel * xRarity * xQuality * xGiant  * xAlbino,
                BASE_EXP, MAX_EXP
        );
    }

    public boolean isGiant() {
        return this.species().isGiant(
                this.weight,
                this.length
        );
    }


    public float lengthPercentile() {
        return this.species().lengthPercentile(this.length);
    }

    public float lengthScale() {
        return this.species().lengthScale(this.length);
    }

    public float weightPercentile() {
        return this.species().weightPercentile(this.weight);
    }

    public float weightScale() {
        return this.species().weightScale(this.weight);
    }

    public boolean isAlbino() {
        return isAlbino(this);
    }
    public static boolean isAlbino(SpecimenData specimenData) {
        return specimenData.subspecies == -1;
    }

    public int value() {
        double xRarity = 1 + this.species.rarity() * 0.01;
        double xQuality = this.quality <= 2 ? (0.5 + this.quality / 4D) : Math.pow(2, (this.quality - 2));
        double xWeight = 1 + weight * 0.005;
        double xAlbino = this.isAlbino() ? 3 : 1;

        return (int) MathHelper.clamp(
                BASE_VALUE * xRarity * xQuality * xWeight * xAlbino
                , 1, 99999);
    }


    public float damage() {
        return MathHelper.clamp((this.level - 10) * 0.05f, 0, 1);
    }


    public boolean isEqual(SpecimenData other) {
        boolean isEqual = (Objects.equals(this.label, other.label));
        isEqual &= (Objects.equals(this.species.name(), other.species.name()));
        isEqual &= (this.level == other.level);
        isEqual &= (this.quality == other.quality);
        isEqual &= (this.weight == other.weight);
        isEqual &= (this.length == other.length);
        isEqual &= (this.caughtByUUID.compareTo(other.caughtByUUID) == 0);
        isEqual &= (this.id.compareTo(other.id) == 0);
        isEqual &= (this.isAlive == other.isAlive);
        return isEqual;
    }

    @Override
    public String toString() {
        return label;
    }

    public ItemStack asItemStack() {
        ItemStack fishItemStack = species().item().getDefaultStack();
        fishItemStack.set(FCComponents.SPECIMEN, this);
        Text label = Text.of((!this.isAlive ? "Raw " : "") + this.label);
        if (this.species == Species.Library.TROPICAL_FISH) {

            label = Text.of((!this.isAlive ? "Raw " : "") + Text.translatable(TropicalFishEntity.getToolTipForVariant(this.subspecies)).getString());

        }
        fishItemStack.set(DataComponentTypes.ITEM_NAME, label);
        return fishItemStack;
    }

    public SpecimenData sibling(PlayerEntity caughtBy) {
        Random r = Random.create();
        var level = MathHelper.clamp(r.nextInt(this.level), MIN_LEVEL, MAX_LEVEL);
        var quality = MathHelper.clamp(r.nextInt(this.quality), MIN_QUALITY, MAX_QUALITY);
        return init(this.species, level, quality, caughtBy, this.subspecies, false);
    }

    //private static final float FISHER_VEST_EXP_BONUS = 0.3f;todo

    public static SpecimenData ofVariant(SpecimenData data, int subspecies) {
        return new SpecimenData(
                data.species,
                data.label,
                data.level,
                data.quality,
                data.weight,
                data.length,
                data.id,
                data.caughtByUUID,
                data.caughtBy,
                data.caughtAt,
                data.isAlive,
                subspecies
        );
    }
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 99;

    public static final int MIN_QUALITY = 1;
    public static final int MAX_QUALITY = 6;

    public static final int BASE_VALUE = 5;

    public static final int BASE_EXP = 3;
    public static final int MAX_EXP = 99999;

    private static final double GIANT_CHANCE = 0.01;


    public static final SpecimenData DEFAULT = SpecimenData.init(Species.Library.DEFAULT);
    public static Codec<SpecimenData> CODEC =
            RecordCodecBuilder.create(
                    instance ->
                            instance
                                    .group(
                                            Codec.STRING.fieldOf("species_name").forGetter(c -> c.species.name()),
                                            Codec.STRING.fieldOf("label").forGetter(SpecimenData::label),
                                            Codec.INT.fieldOf("level").forGetter(SpecimenData::level),
                                            Codec.INT.fieldOf("quality").forGetter(SpecimenData::quality),
                                            Codec.FLOAT.fieldOf("weight").forGetter(SpecimenData::weight),
                                            Codec.FLOAT.fieldOf("length").forGetter(SpecimenData::length),
                                            Uuids.INT_STREAM_CODEC.fieldOf("id").forGetter(SpecimenData::id),
                                            Uuids.INT_STREAM_CODEC.fieldOf("caught_by_uuid").forGetter(SpecimenData::caughtByUUID),
                                            Codec.STRING.fieldOf("caught_by").forGetter(SpecimenData::caughtBy),
                                            Codec.LONG.fieldOf("caught_at").forGetter(SpecimenData::caughtAt),
                                            Codec.BOOL.fieldOf("is_alive").forGetter(SpecimenData::isAlive),
                                            Codec.INT.fieldOf("subspecies").forGetter(SpecimenData::subspecies)//this is argument count limit, need to package fields
                                    ).apply(instance, SpecimenData::new)
            );
    public static PacketCodec<RegistryByteBuf, SpecimenData> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

}
