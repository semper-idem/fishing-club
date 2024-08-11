package net.semperidem.fishing_club.fish;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishing_club.entity.FishingExplosionEntity;
import net.semperidem.fishing_club.entity.IHookEntity;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.item.fishing_rod.components.RodConfiguration;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public record FishRecord(
  String name,
  String speciesName,
  int level,
  int quality,
  float weight,
  float length,
  UUID id,
  UUID caughtByUUID,
  String caughtBy,
  long caughtAt,
  boolean isAlive,
  boolean isAlbino
) {
    public static FishRecord init() {

        return init(SpeciesLibrary.DEFAULT);
    }

    public static FishRecord init(Species species) {

        return init(new IHookEntity() {}, species);
    }

    public static FishRecord init(IHookEntity caughtWith) {

        return init(caughtWith, Math.random() > 0.5f ? SpeciesLibrary.BUTTERFISH : SpeciesLibrary.DEFAULT);
    }

    public String getTextureName() {
        return Species.ofName(this.speciesName).getTextureName(this.isAlbino);
    }

    public static FishRecord init(IHookEntity caughtWith, Species species) {

        var caughtByCard = caughtWith.getFishingCard();
        var caughtUsing = caughtWith.getCaughtUsing();
        var caughtBy = caughtByCard.getHolder();

        var level = calculateLevel(species, caughtByCard.getLevel());
        var quality = calculateQuality(caughtWith, caughtByCard, caughtUsing);
        var weight = calculateWeight(species, level, caughtUsing.attributes().weightCapacity());
        var length = calculateLength(species, level);

        var isAlbino = Math.random() < 0.01f;

        return new FishRecord(
          (weird(species, weight, length) ? "Weird " : "") + (isAlbino ? "Albino " : "") + species.name,
          species.name,
          level,
          quality,
          weight,
          length,
          UUID.randomUUID(),
          caughtBy == null ? UUID.randomUUID() : caughtBy.getUuid(),
          caughtBy == null ? "x" : caughtBy.getNameForScoreboard(),
          System.currentTimeMillis(),
          !(caughtWith instanceof FishingExplosionEntity),
          isAlbino
        );
    }


    private static float calculateWeight(Species species, int level, float maxWeight) {
        return (float) Math.min(getNormalizedRandom(species.fishMinWeight, species.fishRandomWeight, level * 0.01f), maxWeight);
    }

    private static float calculateLength(Species species, int level) {
        return (float) getNormalizedRandom(species.fishMinLength, species.fishRandomLength, level * 0.01f);
    }

    private static int calculateLevel(Species species, int fisherLevel) {

        return (int) MathHelper.clamp(getNormalizedRandom(
            species.minLevel,
            fisherLevel - species.minLevel,
            fisherLevel * 0.01
          ), MIN_LEVEL, MAX_LEVEL
        );
    }

    private static int calculateQuality(IHookEntity caughtWith, FishingCard fisher, RodConfiguration rod) {

        var fisherMean = MathHelper.clamp(fisher.getLevel() * 0.0025, 0, 0.25);
        var rodMean = rod.attributes().fishQuality() * 0.025;
        var circumstanceMean = caughtWith.getCircumstanceQuality(); //todo add method that handle this
        var mean = fisherMean + rodMean + circumstanceMean;

        return (int) MathHelper.clamp(
          getNormalizedRandom(MIN_LEVEL, 4, mean),//non-boosting min quality buff impl
          fisher.getMinGrade(caughtWith),
          MAX_QUALITY
        );
    }

    public int experience(double xFisher) {

        var xRarity = this.species().fishRarity * 0.01;
        var xQuality = this.quality > 3 ? Math.pow(2, this.quality - 3) : 1;
        var xWeird = this.weird() ? 3 : 1;
        var xAlbino = this.isAlbino ? 1.5 : 1;
        var xLevel = Math.pow(this.level, 1.3);

        return (int) MathHelper.clamp(
          BASE_EXP + xFisher * xLevel * xRarity * xQuality * xWeird * xAlbino,
          BASE_EXP, MAX_EXP
        );
    }


    public Species species() {

        return Species.ofName(this.speciesName);
    }

    public boolean weird() {

        return weird(
          this.species(),
          this.weight,
          this.length
        );
    }

    private static boolean weird(Species species, float weight, float length) {

        return Math.abs(getWeightScale(species, length) - getLengthScale(species, weight)) > SCALE_RANGE * WEIRD_RANGE;
    }


    public int value() {
        var species = Species.ofName(this.speciesName);

        var xRarity = 1 + species.fishRarity * 0.01;
        var xQuality = this.quality <= 2 ? (0.5 + this.quality / 4D) : Math.pow(2, (this.quality - 2));
        var xWeight = 1 + weight * 0.005;
        var xWeird = this.weird() ? 1.5 : 1;
        var xAlbino = this.isAlbino() ? 3 : 1;

        return (int) MathHelper.clamp(
          BASE_VALUE * xRarity * xQuality * xWeight * xWeird * xAlbino
          , 1, 99999);
    }


    public float damage() {
        return MathHelper.clamp((this.level - 10) * 0.05f, 0, 1);
    }


    public boolean isEqual(FishRecord other) {
        boolean isEqual = (Objects.equals(this.name, other.name));
        isEqual &= (Objects.equals(this.speciesName, other.speciesName));
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
        return name;
    }


    private static double getNormalizedRandom(double base, double range, double mean) {
        return base + range * Math.abs(RANDOM.nextGaussian(mean, 1)) / 4D;
    }

    public static float getLengthScale(Species species, float length) {

        return 1 - HALF_SCALE_RANGE + ((length - species.fishMinLength) / species.fishRandomLength * SCALE_RANGE);
    }

    public static float getWeightScale(Species species, float weight) {

        return 1 - HALF_SCALE_RANGE + ((weight - species.fishMinLength) / species.fishRandomLength * SCALE_RANGE);
    }


    public static final float SCALE_RANGE = 0.4f;
    private static final float HALF_SCALE_RANGE = SCALE_RANGE * 0.5f;
    //private static final float FISHER_VEST_EXP_BONUS = 0.3f;todo
    private static final Random RANDOM = new Random(42L);

    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 99;

    public static final int MIN_QUALITY = 1;
    public static final int MAX_QUALITY = 5;

    public static final int BASE_VALUE = 5;

    public static final int BASE_EXP = 3;
    public static final int MAX_EXP = 99999;

    public static final double WEIRD_RANGE = 0.7D;

    public static final FishRecord DEFAULT =
      new FishRecord(SpeciesLibrary.DEFAULT.name,
        SpeciesLibrary.DEFAULT.name,
        MIN_LEVEL, MIN_QUALITY,
        0,
        0,
        UUID.randomUUID(),
        UUID.randomUUID(),
        "x",
        System.currentTimeMillis(),
        false, false
      );
    public static Codec<FishRecord> CODEC =
      RecordCodecBuilder.create(
        instance ->
          instance
            .group(
              Codec.STRING.fieldOf("name").forGetter(FishRecord::name),
              Codec.STRING.fieldOf("species_name").forGetter(FishRecord::speciesName),
              Codec.INT.fieldOf("level").forGetter(FishRecord::level),
              Codec.INT.fieldOf("quality").forGetter(FishRecord::quality),
              Codec.FLOAT.fieldOf("weight").forGetter(FishRecord::weight),
              Codec.FLOAT.fieldOf("length").forGetter(FishRecord::length),
              Uuids.INT_STREAM_CODEC.fieldOf("id").forGetter(FishRecord::id),
              Uuids.INT_STREAM_CODEC.fieldOf("caught_by_uuid").forGetter(FishRecord::caughtByUUID),
              Codec.STRING.fieldOf("caught_by").forGetter(FishRecord::caughtBy),
              Codec.LONG.fieldOf("caught_at").forGetter(FishRecord::caughtAt),
              Codec.BOOL.fieldOf("is_alive").forGetter(FishRecord::isAlive),
              Codec.BOOL.fieldOf("is_albino").forGetter(FishRecord::isAlbino)//this is argument count limit, need to package fields
            ).apply(instance, FishRecord::new)
      );
    public static PacketCodec<RegistryByteBuf, FishRecord> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

}
