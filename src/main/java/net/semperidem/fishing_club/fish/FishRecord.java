package net.semperidem.fishing_club.fish;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.PlayerEntity;
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
import java.util.UUID;

import static net.semperidem.fishing_club.fish.FishUtil.getPseudoRandomValue;

public record FishRecord(
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
  UUID caughtByUUID,
  String caughtBy,
  long caughtAt,
  boolean isAlive,
  boolean isAlbino
) {

    //private static final float FISHER_VEST_EXP_BONUS = 0.3f;todo
    public static final FishRecord DEFAULT =
      new FishRecord("Cod", "Cod", 0, 1, 3, 0, 0, 1, 0, UUID.randomUUID(), UUID.randomUUID(), "Herobrain", System.currentTimeMillis(), false, false);
    public static Codec<FishRecord> CODEC =
      RecordCodecBuilder.create(
        instance ->
          instance
            .group(//todo remove values that don't need to be stored like exp, damage etc as the can be calculated on the go
              Codec.STRING.fieldOf("name").forGetter(FishRecord::name),
              Codec.STRING.fieldOf("species_name").forGetter(FishRecord::speciesName),
              Codec.INT.fieldOf("level").forGetter(FishRecord::level),
              Codec.INT.fieldOf("quality").forGetter(FishRecord::quality),
              Codec.INT.fieldOf("experience").forGetter(FishRecord::experience),
              Codec.FLOAT.fieldOf("weight").forGetter(FishRecord::weight),
              Codec.FLOAT.fieldOf("length").forGetter(FishRecord::length),
              Codec.INT.fieldOf("value").forGetter(FishRecord::value),
              Codec.FLOAT.fieldOf("damage").forGetter(FishRecord::damage),
              Uuids.INT_STREAM_CODEC.fieldOf("id").forGetter(FishRecord::id),
              Uuids.INT_STREAM_CODEC.fieldOf("caught_by_uuid").forGetter(FishRecord::caughtByUUID),
              Codec.STRING.fieldOf("caught_by").forGetter(FishRecord::caughtBy),
              Codec.LONG.fieldOf("caught_at").forGetter(FishRecord::caughtAt),
              Codec.BOOL.fieldOf("is_alive").forGetter(FishRecord::isAlive),
              Codec.BOOL.fieldOf("is_albino").forGetter(FishRecord::isAlbino)//this is argument count limit, need to package fields
            ).apply(instance, FishRecord::new));
    public static PacketCodec<RegistryByteBuf, FishRecord> PACKET_CODEC =
      PacketCodecs.registryCodec(CODEC);

    private static final int DEFAULT_LEVEL = 10;
    private static final int DEFAULT_MIN_GRADE = 1;
    private static final double DEFAULT_MULTIPLIER = 0.9D;
    public static FishRecord create(Species species) {
        int level = getLevel(species, DEFAULT_LEVEL);
        int quality = getQuality(
          DEFAULT_LEVEL,
          0,
          DEFAULT_MIN_GRADE,
          DEFAULT_MULTIPLIER);
        float weight =
          MathHelper.clamp(
            getPseudoRandomValue(species.fishMinWeight, species.fishRandomWeight, level * 0.01f),
            0,
            999);
        float length = getPseudoRandomValue(species.fishMinLength, species.fishRandomLength, level * 0.01f);
        boolean isAlbino = Math.random() < 0.01f;
        boolean isWeird = isWeird(species, weight, length);
        String name = (isWeird ? "Weird " : "") +  (isAlbino ? "Albino " : "") + species.name;
        return new FishRecord(
          name,
          species.name,
          level,
          quality,
          getExperience(isWeird, isAlbino, DEFAULT_MULTIPLIER, species, level, quality),
          weight,
          length,
          getValue(isWeird, isAlbino, weight, quality, species),
          getDamage(level),
          UUID.randomUUID(),
          UUID.randomUUID(),
          "unknown",
          System.currentTimeMillis(),
          true,
          isAlbino
        );
    }

    public static FishRecord create(IHookEntity caughtWith) {
        FishingCard caughtByCard = caughtWith.getFishingCard();
        PlayerEntity caughtBy = caughtByCard.getHolder();
        RodConfiguration caughtUsing = caughtWith.getCaughtUsing();
        Species species = SpeciesLibrary.BUTTERFISH; // todo caughtWith position and world based
        int level = getLevel(species, caughtByCard.getLevel());
        int quality =
          getQuality(
            caughtByCard.getLevel(),
            caughtWith.getWaitTime(),
            caughtByCard.getMinGrade(caughtWith),
            Math.pow(caughtWith.getFishMethodDebuff(), 2));
        float weight =
          MathHelper.clamp(
            getPseudoRandomValue(species.fishMinWeight, species.fishRandomWeight, level * 0.01f),
            0,
            caughtUsing.attributes().weightCapacity());
        float length = getPseudoRandomValue(species.fishMinLength, species.fishRandomLength, level * 0.01f);
        boolean isAlbino = Math.random() < 0.01f;
        boolean isWeird = isWeird(species, weight, length);
        String name =(isWeird ? "Weird " : "") +  (isAlbino ? "Albino " : "") + species.name;
        return new FishRecord(
          name,
          species.name,
          level,
          quality,
          getExperience(isWeird, isAlbino, Math.pow(caughtWith.getFishMethodDebuff(), 2), species, level, quality),
          weight,
          length,
          getValue(isWeird, isAlbino, weight, quality, species),
          getDamage(level),
          UUID.randomUUID(),
          caughtBy.getUuid(),
          caughtBy.getNameForScoreboard(),
          System.currentTimeMillis(),
          !(caughtWith instanceof FishingExplosionEntity),
          isAlbino
        );
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

    private static boolean isWeird(Species species, float weight, float length) {
        float lengthScale = FishComponent.getLengthScale(species, length);
        float weightScale = FishComponent.getWeightScale(species, weight);
        return Math.abs(lengthScale - weightScale) > FishComponent.SCALE_RANGE * 0.7f;
    }

    private static int getValue(boolean isWeird, boolean isAlbino, float weight, int quality, Species species) {
        float gradeMultiplier =
          (float) (quality <= 3 ? (0.5 + (quality / 3f)) : Math.pow(2, (quality - 2)));
        float weightMultiplier =
          1 + (weight < 100 ? (float) Math.sqrt(weight / 0.01f) : weight * 0.01f);
        float fValue = 1 + ((125 - species.fishRarity) / 100) * 0.5f;
        fValue *= gradeMultiplier;
        fValue *= weightMultiplier;

        if (isWeird) {
            fValue *= 1.5f;
        }
        if (isAlbino) {
            fValue *= 3f;
        }
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

    private static int getExperience(boolean isWeird, boolean isAlbino,
                                     double multiplier, Species species, int level, int quality) {
        float fishRarityMultiplier = (200 - species.fishRarity) / 100;
        float fishExpValue = (float) Math.pow(level, 1.3);
        float fishGradeMultiplier = quality > 3 ? (float) Math.pow(2, quality - 3) : 1;
        int fishExp =
          (int)
            ((fishGradeMultiplier * fishRarityMultiplier * (5 + fishExpValue))
              * multiplier);

        if (isWeird) {
            fishExp *= (int) 3f;
        }
        if (isAlbino) {
            fishExp = (int) (fishExp * 1.5f);
        }
        return MathHelper.clamp(fishExp, 3, 99999);
    }

    public boolean isEqual(FishRecord other) {
        boolean isEqual = (Objects.equals(this.name, other.name));
        isEqual &= (Objects.equals(this.speciesName, other.speciesName));
        isEqual &= (this.level == other.level);
        isEqual &= (this.quality == other.quality);
        isEqual &= (this.value == other.value);
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

}
