package net.semperidem.fishingclub.fisher.perks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.util.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static net.semperidem.fishingclub.fisher.perks.TradeSecrets.*;

public class TradeSecret {
    String name;
    Text label;
    Text shortDescription;
    List<Text> longDescription;
    TradeSecret parent;
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

    public String name() {
        return this.name;
    }

    public List<TradeSecret> getChildren(){
        return this.children;
    }

    public Identifier getTexture(){
        return this.texture;
    }

    public TradeSecret getParent(){
        return parent;
    }

    public Text getLabel(){
        return label;
    }

    public Text shortDescription(){
        return this.shortDescription;
    }

    public Text longDescription(int level){
        if (this.longDescription == null) {
            return Text.empty();
        }
        if (level > this.longDescription.size()) {
            return this.longDescription.getFirst();
        }
        return this.longDescription.get(level);
    }

    public int maxLevel() {
        return this.maxLevel;
    }

    public Instance instance(){
        return new Instance(this, 1);
    }

    private int power(int level) {
        return this.levelAffects == LevelChanges.VALUE ? 1 : (int) this.levelValues[level];
    }

    private long duration(int level) {
        return (long) (this.baseDuration * (this.levelAffects == LevelChanges.DURATION ? this.levelValues[level] : 1));
    }

    private long cooldown(int level) {
       return (long) (this.baseCooldown * (this.levelAffects == LevelChanges.COOLDOWN ? this.levelValues[level] : 1));
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
        TradeSecret parent;
        int level;
        long nextUseTime;

        private Instance(TradeSecret parent, int level) {
            this(parent, level, 0);
        }

        private Instance(TradeSecret parent, int level, long nextUseTime) {
            this.parent = parent;
            this.level = level;
            this.nextUseTime = nextUseTime;
        }

        public int upgradeCost() {
            return this.parent.cost(this.level + 1);
        }

        public void upgrade() {
            this.level++;
        }

        public void use(ServerPlayerEntity player, @Nullable Entity entity) {
            long currentTime = player.getWorld().getTime();
            if (currentTime < this.nextUseTime) {
                return;
            }
            if (this.parent.effect != null) {
                Utils.castEffect(player, new StatusEffectInstance(
                        this.parent.effect,
                        (int) this.parent.duration(this.level),
                        this.parent.power(this.level)
                        ));
            }
            if (this.parent.active == null) {
                return;
            }
            int nextCooldown = 100;
            if (this.parent.active.apply(player, entity)) {
                nextCooldown = (int) this.parent.cooldown(this.level);
            }
            this.nextUseTime = currentTime + nextCooldown;

        }

        public String name() {
            return this.parent.name;
        }

        public static NbtCompound toNbt(Instance instance) {
            NbtCompound tag = new NbtCompound();
            tag.putString("name", instance.parent.name);
            tag.putInt("level", instance.level);
            tag.putLong("nextUseTime", instance.nextUseTime);
            return tag;
        }

        public static Instance fromNbt(NbtCompound nbtCompound) {//todo handle orElse
            return new Instance(TradeSecrets.fromName(nbtCompound.getString("name")).orElseThrow(), nbtCompound.getInt("level"), nbtCompound.getLong("nextUseTime"));
        }
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private String name;
        private TradeSecret parent;
        private float[] levelValues;
        private int[] costPerLevel;
        private LevelChanges levelAffects;
        private BiFunction<ServerPlayerEntity, Entity, Boolean> active;
        private int baseCooldown;
        private int baseDuration;
        private RegistryEntry<StatusEffect> effect;

        TradeSecret build() {
            TradeSecret tradeSecret = new TradeSecret();
            NAME_TO_SKILL.put(this.name, tradeSecret);

            tradeSecret.name = this.name;
            tradeSecret.label = Text.translatable(this.name);
            tradeSecret.texture = FishingClub.identifier("textures/gui/skill/" + this.name);
            tradeSecret.shortDescription = Text.translatable(this.name + ".short_description");
            tradeSecret.longDescription = this.createLongDescriptions(tradeSecret);
            tradeSecret.maxLevel = this.levelValues == null ? 0 : this.levelValues.length;
            tradeSecret.levelValues = this.levelValues;
            tradeSecret.levelAffects = this.levelAffects;
            tradeSecret.costPerLevel = this.costPerLevel;
            if (this.parent != null) {
                this.parent.children.add(tradeSecret);
                tradeSecret.parent = this.parent;
            }
            tradeSecret.active = this.active;
            tradeSecret.effect = this.effect;
            tradeSecret.baseDuration = this.baseDuration;
            tradeSecret.baseCooldown = this.baseCooldown;
            return tradeSecret;
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
            for(int i = 0; i < tradeSecret.maxLevel; i++) {
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

        Builder parent(TradeSecret parent) {
            this.parent = parent;
            return this;
        }

    }


}
