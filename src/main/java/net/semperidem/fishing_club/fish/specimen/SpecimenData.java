package net.semperidem.fishing_club.fish.specimen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishing_club.entity.FishingExplosionEntity;
import net.semperidem.fishing_club.entity.IHookEntity;
import net.semperidem.fishing_club.fish.Species;
import net.semperidem.fishing_club.fish.SpeciesLibrary;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishing_club.registry.FCComponents;
import net.semperidem.fishing_club.util.MathUtil;

import java.util.Objects;
import java.util.UUID;

public record SpecimenData(
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
    public static SpecimenData init() {

        return init(SpeciesLibrary.DEFAULT);
    }

    public static SpecimenData init(Species species) {

        return init(new IHookEntity() {}, species);
    }

    public static SpecimenData init(IHookEntity caughtWith) {

        return init(caughtWith, Math.random() > 0.5f ? SpeciesLibrary.BUTTERFISH : SpeciesLibrary.DEFAULT);
    }

    public String getTextureName() {
        return Species.of(this.speciesName).getTextureName(this.isAlbino);
    }

    public static SpecimenData init(IHookEntity caughtWith, Species species) {

        var caughtByCard = caughtWith.getFishingCard();
        var caughtUsing = caughtWith.getCaughtUsing();
        var caughtBy = caughtByCard.getHolder();

        var level = calculateLevel(species, caughtByCard.getLevel());
        var quality = calculateQuality(caughtWith, caughtByCard, caughtUsing);
        var weight = calculateWeight(species, level);
        var length = calculateLength(species, level);

        var isAlbino = Math.random() < 0.01f;

        return new SpecimenData(
          (species.weird(weight, length) ? "Weird " : "") + (isAlbino ? "Albino " : "") + species.name,
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


    private static float calculateWeight(Species species, int level) {
        return species.weightInRange(level * 0.01f);
    }

    private static float calculateLength(Species species, int level) {
        return species.lengthInRange(level * 0.01f);
    }

    private static int calculateLevel(Species species, int fisherLevel) {

        return (int) MathHelper.clamp(MathUtil.normal(
            species.level(),
            fisherLevel - species.level(),
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
          MathUtil.normal(MIN_LEVEL, 4, mean),//non-boosting min quality buff impl
          fisher.getMinGrade(caughtWith),
          MAX_QUALITY
        );
    }

    public int experience(double xFisher) {

        var xRarity = (50 / (this.species().rarity() + 9));//expected values from 1 to 200
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
        return Species.of(this.speciesName);
    }

    public boolean weird() {
        return this.species().weird(
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
        return this.species().weightScale(this.length);
    }


    public int value() {
        var species = Species.of(this.speciesName);

        var xRarity = 1 + species.rarity() * 0.01;
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


    public boolean isEqual(SpecimenData other) {
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

    public ItemStack asItemStack() {
        ItemStack fishItemStack = species().item().getDefaultStack();
        fishItemStack.set(FCComponents.SPECIMEN, this);
        fishItemStack.set(DataComponentTypes.CUSTOM_NAME, Text.of(this.name));
        return fishItemStack;
    }

    //private static final float FISHER_VEST_EXP_BONUS = 0.3f;todo

    public static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 99;

    public static final int MIN_QUALITY = 1;
    public static final int MAX_QUALITY = 5;

    public static final int BASE_VALUE = 5;

    public static final int BASE_EXP = 3;
    public static final int MAX_EXP = 99999;


    public static final SpecimenData DEFAULT =
      new SpecimenData(SpeciesLibrary.DEFAULT.name,
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
    public static Codec<SpecimenData> CODEC =
      RecordCodecBuilder.create(
        instance ->
          instance
            .group(
              Codec.STRING.fieldOf("name").forGetter(SpecimenData::name),
              Codec.STRING.fieldOf("species_name").forGetter(SpecimenData::speciesName),
              Codec.INT.fieldOf("level").forGetter(SpecimenData::level),
              Codec.INT.fieldOf("quality").forGetter(SpecimenData::quality),
              Codec.FLOAT.fieldOf("weight").forGetter(SpecimenData::weight),
              Codec.FLOAT.fieldOf("length").forGetter(SpecimenData::length),
              Uuids.INT_STREAM_CODEC.fieldOf("id").forGetter(SpecimenData::id),
              Uuids.INT_STREAM_CODEC.fieldOf("caught_by_uuid").forGetter(SpecimenData::caughtByUUID),
              Codec.STRING.fieldOf("caught_by").forGetter(SpecimenData::caughtBy),
              Codec.LONG.fieldOf("caught_at").forGetter(SpecimenData::caughtAt),
              Codec.BOOL.fieldOf("is_alive").forGetter(SpecimenData::isAlive),
              Codec.BOOL.fieldOf("is_albino").forGetter(SpecimenData::isAlbino)//this is argument count limit, need to package fields
            ).apply(instance, SpecimenData::new)
      );
    public static PacketCodec<RegistryByteBuf, SpecimenData> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

}
