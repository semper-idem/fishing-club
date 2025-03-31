package net.semperidem.fishingclub.fisher.tradesecret;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.registry.Components;
import net.semperidem.fishingclub.util.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets.*;

public class TradeSecret {
    String name;
    Text label;
    Text shortDescription;
    List<Text> longDescription;
    List<TradeSecret> requiredSecrets;
    List<TradeSecret> children = new ArrayList<>();
    Identifier texture;
    int maxLevel = 1;
    float[] levelValues;
    int[] costPerLevel;
    LevelChanges levelAffects = LevelChanges.VALUE;
    RegistryEntry<StatusEffect> effect;
    BiFunction<ServerPlayerEntity, @Nullable Entity, Boolean> active;
    int baseCooldown;
    int baseDuration;
    Predicate<PlayerEntity> condition;

    public String name() {
        return this.name;
    }

    public List<TradeSecret> getChildren() {
        return this.children;
    }

    public Identifier getTexture() {
        return this.texture;
    }

    public List<TradeSecret> getRequiredSecrets() {
        return requiredSecrets;
    }

    public Text getLabel() {
        return label;
    }

    public Text shortDescription() {
        return this.shortDescription;
    }

    public Text longDescription(int level) {
        if (this.longDescription == null) {
            return Text.empty();
        }
        if (level >= this.longDescription.size()) {
            return this.longDescription.getFirst();
        }
        return this.longDescription.get(level);
    }

    public int maxLevel() {
        return this.maxLevel;
    }

    public Instance instance() {
        return new Instance(this, 1);
    }

    public Instance instanceOfLevel(int level) {
        return new Instance(this, level);
    }

    public boolean isActive(Card card) {
        return this.condition == null || condition.test(card.owner());
    }

    public float value(int level) {
        return this.levelAffects == LevelChanges.VALUE ? this.levelValues[level - 1] : 1;
    }

    public long duration(int level) {
        return (long) (this.baseDuration * (this.levelAffects == LevelChanges.DURATION ? this.levelValues[level - 1] : 1));
    }

    public long cooldown(int level) {
        return (long) (this.baseCooldown * (this.levelAffects == LevelChanges.COOLDOWN ? this.levelValues[level - 1] : 1));
    }

    public boolean hasActive() {
        return this.active != null || (this.effect != null && this.baseCooldown > 0);
    }


    private int cost(int level) {
        if (this.costPerLevel == null || this.costPerLevel.length < level) {
            return 1;
        }
        return costPerLevel[level];
    }

    enum LevelChanges {
        VALUE, DURATION, COOLDOWN
    }

    public static class Instance {
        TradeSecret root;
        int level;
        long nextUseTime;

        private Instance(TradeSecret root, int level) {
            this(root, level, 0);
        }

        private Instance(TradeSecret root, int level, long nextUseTime) {
            this.root = root;
            this.level = level;
            this.nextUseTime = nextUseTime;
        }

        public int upgradeCost() {
            return this.root.cost(this.level + 1);
        }

        public void upgrade() {
            this.level++;
        }

        public TradeSecret root() {
            return this.root;
        }

        public int level() {
            return this.level;
        }

        public long nextUseTime() {
            return this.nextUseTime;
        }

        public boolean use(ServerPlayerEntity player, @Nullable Entity entity) {
            long currentTime = player.getWorld().getTime();
            if (currentTime < this.nextUseTime) {
                return false;
            }
            if (this.root.effect != null) {
                Utils.castEffect(player, new StatusEffectInstance(
                                this.root.effect,
                                (int) this.root.duration(this.level),
                                (int) this.root.value(this.level)
                        ),
                        this.root.baseCooldown > 0 ? 4 : 0
                );
                return true;
            }
            if (this.root.active == null) {
                return false;
            }
            ItemStack heldItem = player.getMainHandStack();

            if (heldItem.getOrDefault(Components.SPECIMEN_DATA, SpecimenData.DEFAULT).quality() < 4) {
                player.sendMessage(Text.of("Sacrifice must be made, high quality fish is required"));
                return false;
            }

            heldItem.decrement(1);
            int nextCooldown = 100;
            if (this.root.active.apply(player, entity)) {
                nextCooldown = (int) this.root.cooldown(this.level);
            }
            this.nextUseTime = currentTime + nextCooldown;
            return true;
        }

        public String name() {
            return this.root.name;
        }

        public static NbtCompound toNbt(Instance instance) {
            NbtCompound tag = new NbtCompound();
            tag.putString("name", instance.root.name);
            tag.putInt("level", instance.level);
            tag.putLong("nextUseTime", instance.nextUseTime);
            return tag;
        }

        public static Instance fromNbt(NbtCompound nbtCompound) {//todo handle orElse
            return new Instance(TradeSecrets.fromName(
                    nbtCompound.getString("name", "")).orElseThrow(),
                    nbtCompound.getInt("level", 0),
                    nbtCompound.getLong("nextUseTime", 0)
            );
        }

        @Override
        public String toString() {
            return this.name() + "_" + this.level + "_" + this.nextUseTime;
        }
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private String name;
        private final List<TradeSecret> requirements = new ArrayList<>();
        private float[] levelValues;
        private int[] costPerLevel;
        private LevelChanges levelAffects = LevelChanges.VALUE;
        private BiFunction<ServerPlayerEntity, Entity, Boolean> active;
        private int baseCooldown;
        private int baseDuration;
        private RegistryEntry<StatusEffect> effect;
        private Predicate<PlayerEntity> condition;

        TradeSecret build() {
            TradeSecret tradeSecret = new TradeSecret();
            NAME_TO_SKILL.put(this.name, tradeSecret);

            tradeSecret.name = this.name;
            tradeSecret.label = Text.translatable(this.name);
            tradeSecret.texture = FishingClub.identifier("textures/gui/skill/" + this.name);
            tradeSecret.shortDescription = Text.translatable(this.name + ".short_description");
            tradeSecret.longDescription = this.createLongDescriptions(tradeSecret);
            tradeSecret.levelValues = this.levelValues;
            tradeSecret.levelAffects = this.levelAffects;
            tradeSecret.costPerLevel = this.costPerLevel;
            tradeSecret.maxLevel = maxLevel();
            this.requirements.forEach(parent -> parent.children.add(tradeSecret));
            tradeSecret.requiredSecrets = this.requirements;
            tradeSecret.active = this.active;
            tradeSecret.effect = this.effect;
            tradeSecret.baseDuration = this.baseDuration;
            tradeSecret.baseCooldown = this.baseCooldown;
            tradeSecret.condition = this.condition;
            return tradeSecret;
        }

        private int maxLevel() {
            if (this.levelValues != null) {
                return this.levelValues.length;
            }
            if (this.costPerLevel != null) {
                return this.costPerLevel.length;
            }
            return 1;
        }

        public Builder conditional(Predicate<PlayerEntity> condition) {
            this.condition = condition;
            return this;
        }


        public Builder active(BiFunction<ServerPlayerEntity, Entity, Boolean> active, int baseCooldown) {
            this.active = active;
            this.baseCooldown = baseCooldown;
            return this;
        }

        public Builder active(RegistryEntry<StatusEffect> effect, int baseCooldown, int baseDuration) {
            this.baseCooldown = baseCooldown;
            this.baseDuration = baseDuration;
            this.effect = effect;
            return this;
        }

        private ArrayList<Text> createLongDescriptions(TradeSecret tradeSecret) {
            ArrayList<Text> leveledLongDescriptions = new ArrayList<>();
            for (int i = 0; i < tradeSecret.maxLevel; i++) {
                leveledLongDescriptions.add(Text.translatable(this.name + ".long_description." + i));
            }
            if (leveledLongDescriptions.isEmpty()) {
                leveledLongDescriptions.add(Text.translatable(this.name + ".long_description.0"));
            }
            return leveledLongDescriptions;
        }

        Builder costPerLevel(int... costPerLevel) {
            this.costPerLevel = costPerLevel;
            return this;
        }

        Builder levelDuration(float... levelValues) {
            this.levelAffects = LevelChanges.DURATION;
            this.levelValues = levelValues;
            return this;
        }

        Builder levelCooldown(float... levelValues) {
            this.levelAffects = LevelChanges.COOLDOWN;
            this.levelValues = levelValues;
            return this;
        }

        Builder levelValues(float... levelValues) {
            this.levelValues = levelValues;
            return this;
        }

        Builder name(String name) {
            this.name = name;
            return this;
        }

        Builder require(TradeSecret requiredSecret) {
            this.requirements.add(requiredSecret);
            return this;
        }

    }


    public static final Predicate<PlayerEntity> REQUIRES_BOAT = o -> o.getVehicle() instanceof BoatEntity;
    public static final Predicate<PlayerEntity> REQUIRES_RAIN = o -> o.getWorld().isRaining();
}
