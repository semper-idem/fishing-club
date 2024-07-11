package net.semperidem.fishing_club.fish;

import static net.semperidem.fishing_club.fish.FishUtil.getPseudoRandomValue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishing_club.entity.IHookEntity;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.item.fishing_rod.components.RodConfiguration;

public record FishComponent(
    String name,
    String speciesName,
    int level,
    int quality,
    int experience,
    float weight,
    float length,
    int value,
    float damage,
    UUID id,
    UUID caughtByUUID) {
  //private static final float FISHER_VEST_EXP_BONUS = 0.3f;todo
  public static final FishComponent DEFAULT =
      new FishComponent("Cod", "Cod", 1, 1, 3, 0, 0, 1, 0, UUID.randomUUID(), UUID.randomUUID());
  public static Codec<FishComponent> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      Codec.STRING.fieldOf("name").forGetter(FishComponent::name),
                      Codec.STRING.fieldOf("species_name").forGetter(FishComponent::speciesName),
                      Codec.INT.fieldOf("level").forGetter(FishComponent::level),
                      Codec.INT.fieldOf("quality").forGetter(FishComponent::quality),
                      Codec.INT.fieldOf("experience").forGetter(FishComponent::experience),
                      Codec.FLOAT.fieldOf("weight").forGetter(FishComponent::weight),
                      Codec.FLOAT.fieldOf("length").forGetter(FishComponent::length),
                      Codec.INT.fieldOf("value").forGetter(FishComponent::value),
                      Codec.FLOAT.fieldOf("damage").forGetter(FishComponent::damage),
                      Uuids.INT_STREAM_CODEC.fieldOf("id").forGetter(FishComponent::id),
                      Uuids.INT_STREAM_CODEC
                          .fieldOf("caught_by_uuid")
                          .forGetter(FishComponent::caughtByUUID))
                  .apply(instance, FishComponent::new));
  public static PacketCodec<RegistryByteBuf, FishComponent> PACKET_CODEC =
      PacketCodecs.registryCodec(CODEC);

  public static FishComponent create(IHookEntity caughtWith) {
    FishingCard caughtByCard = caughtWith.getFishingCard();
    PlayerEntity caughtBy = caughtByCard.getHolder();
    RodConfiguration caughtUsing = caughtWith.getCaughtUsing();
    Species species = SpeciesLibrary.COD; // todo caughtWith position and world based
    int level = getLevel(species, caughtByCard.getLevel());
    int quality =
        getQuality(
            caughtByCard.getLevel(),
            caughtWith.getWaitTime(),
            caughtByCard.getMinGrade(),
            Math.pow(caughtWith.getFishMethodDebuff(), 2));
    float weight =
        MathHelper.clamp(
            getPseudoRandomValue(species.fishMinWeight, species.fishRandomWeight, level * 0.01f),
            0,
            caughtUsing.weightCapacity());
    return new FishComponent(
        species.name, // for rare variant names
        species.name,
        level,
        quality,
        getExperience(caughtWith, species, level, quality),
        weight,
        getPseudoRandomValue(species.fishMinLength, species.fishRandomLength, level * 0.01f),
        getValue(weight, quality, species),
        getDamage(level),
        UUID.randomUUID(),
        caughtBy.getUuid());
  }

  private static int getLevel(Species species, int fisherLevel) {
    int adjustedFishLevel =
        getPseudoRandomValue(
            species.minLevel,
            Math.min(99 - species.minLevel, fisherLevel),
            Math.min(
                1, (float) (Math.min(0.5, (fisherLevel / 200f)) + (Math.sqrt(fisherLevel) / 50f))));
    return MathHelper.clamp(adjustedFishLevel, 1, 99);
  }

  private static int getValue(float weight, int quality, Species species) {
    float gradeMultiplier =
        (float) (quality <= 3 ? (0.5 + (quality / 3f)) : Math.pow(2, (quality - 2)));
    float weightMultiplier =
        1 + (weight < 100 ? (float) Math.sqrt(weight / 0.01f) : weight * 0.01f);
    float fValue = 1 + ((125 - species.fishRarity) / 100) * 0.5f;
    fValue *= gradeMultiplier;
    fValue *= weightMultiplier;
    return (int) MathHelper.clamp(fValue, 1, 99999);
  }

  private static int getQuality(int level, int waitTime, int minQuality, double multiplier) {
    float levelSkew = level * 0.0025f;
    float rodSkew = 0.25f; // todo
    float chunkSkew = 0.25f; // 0.25 for best chunk  TODO CHUNK QUALITY
    float waitSkew =
        0.25f * (waitTime / 1200); // make waiting over 60s STILL improve quality (intended)
    float skew = levelSkew + rodSkew + chunkSkew + waitSkew;
    return (int) MathHelper.clamp(getPseudoRandomValue(1, 4, skew) * multiplier, minQuality, 5);
  }

  private static float getDamage(int level) {
    return MathHelper.clamp((level - 5) * 0.05f, 0, 1);
  }

  private static int getExperience(
      IHookEntity caughtWith, Species species, int level, int quality) {
    float fishRarityMultiplier = (200 - species.fishRarity) / 100;
    float fishExpValue = (float) Math.pow(level, 1.3);
    float fishGradeMultiplier = quality > 3 ? (float) Math.pow(2, quality - 3) : 1;
    int fishExp =
        (int)
            ((fishGradeMultiplier * fishRarityMultiplier * (5 + fishExpValue))
                * Math.pow(caughtWith.getFishMethodDebuff(), 2));
    return MathHelper.clamp(fishExp, 3, 99999);
  }

  public boolean isEqual(FishComponent other) {
    boolean isEqual = (Objects.equals(this.name, other.name));
    isEqual &= (Objects.equals(this.speciesName, other.speciesName));
    isEqual &= (this.level == other.level);
    isEqual &= (this.quality == other.quality);
    isEqual &= (this.value == other.value);
    isEqual &= (this.weight == other.weight);
    isEqual &= (this.length == other.length);
    isEqual &= (this.caughtByUUID.compareTo(other.caughtByUUID) == 0);
    isEqual &= (this.id.compareTo(other.id) == 0);
    return isEqual;
  }

  @Override
  public String toString() {
    return name;
  }
}
