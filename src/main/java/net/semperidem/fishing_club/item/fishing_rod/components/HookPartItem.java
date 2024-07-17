package net.semperidem.fishing_club.item.fishing_rod.components;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import java.util.ArrayList;
import java.util.function.Consumer;

public class HookPartItem extends PartItem {
    private float biteFailChance = 0;
    private float autoHookChance = 0;
    private boolean sharp = false;
    private float damage = 0;
    private float reelDamage = 0;
    private boolean sticky = false;
    private final ArrayList<Consumer<LivingEntity>> onEntityHitEffects = new ArrayList<>();
    private float treasureBonus = 0;
    private float treasureRarityBonus = 0;
    private float fishRarity = 0;
    private float fishRarityMultiplier = 1;
    private float timeHookedMultiplier = 1;
    private float timeUntilHookedMultiplier = 1;

    public static final Consumer<LivingEntity> ON_HIT_POISON = targetEntity -> {
        targetEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200));
    };
    public static final Consumer<LivingEntity> ON_HIT_WITHER = targetEntity -> {
        targetEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100));
    };
    public static final Consumer<LivingEntity> ON_HIT_FIRE = targetEntity -> {
        targetEntity.setOnFireForTicks(200);
    };

    public HookPartItem(Settings settings) {

        super(settings);
        this.destroyOnBreak = true;
        setDamageMultiplier(DamageSource.CAST, 0);
        setDamageMultiplier(DamageSource.BITE, 1);
        setDamageMultiplier(DamageSource.REEL_FISH, 2);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 5);
        setDamageMultiplier(DamageSource.REEL_GROUND, 1);
    }

    public HookPartItem(Settings settings,int weightCapacity,  int minOperatingTemperature, int maxOperatingTemperature, float fishQuality) {

        this(settings, weightCapacity, minOperatingTemperature, maxOperatingTemperature);
        this.fishQuality = fishQuality;
    }

    public HookPartItem(Settings settings,int weightCapacity,  int minOperatingTemperature, int maxOperatingTemperature) {

        this(settings, weightCapacity);
        this.minOperatingTemperature = minOperatingTemperature;
        this.maxOperatingTemperature = maxOperatingTemperature;
    }

    public HookPartItem(Settings settings,int weightCapacity) {

        this(settings);
        this.weightCapacity = weightCapacity;
    }

    public HookPartItem fishQuality(float fishQuality) {
        this.fishQuality = fishQuality;
        return this;
    }

    public HookPartItem addOnEntityHitEffects(Consumer<LivingEntity> livingEntityConsumer) {
        this.onEntityHitEffects.add(livingEntityConsumer);
        return this;
    }

    public void onEntityHit(LivingEntity entity){
        this.onEntityHitEffects.forEach(o -> o.accept(entity));
    }


    public float getBiteFailChance() {
        return this.biteFailChance;
    }

    public HookPartItem biteFailChance(float biteFailChance) {
        this.biteFailChance = biteFailChance;
        return this;
    }

    public float getAutoHookChance() {
        return this.autoHookChance;
    }

    public HookPartItem autoHookChance(float autoHookChance) {
        this.autoHookChance = autoHookChance;
        return this;
    }

    public boolean isSharp() {
        return this.sharp;
    }

    public HookPartItem sharp(boolean sharp) {
        this.sharp = sharp;
        return this;
    }

    public float getDamage() {
        return this.damage;
    }

    public HookPartItem damage(float damage) {
        this.damage = damage;
        return this;
    }

    public float getReelDamage() {
        return this.reelDamage;
    }

    public HookPartItem reelDamage(float reelDamage) {
        this.reelDamage = reelDamage;
        return this;
    }

    public boolean isSticky() {
        return this.sticky;
    }

    public HookPartItem sticky(boolean sticky) {
        this.sticky = sticky;
        return this;
    }


    @Override
    public HookPartItem fishControl(float fishControl) {
        this.fishControl = fishControl;
        return this;
    }

    @Override
    public HookPartItem fishControlMultiplier(float fishControlMultiplier) {
        this.fishControlMultiplier = fishControlMultiplier;
        return this;
    }


    public HookPartItem treasureBonus(float treasureBonus) {

        this.treasureBonus = treasureBonus;
        return this;
    }

    public HookPartItem treasureRarityBonus(float treasureRarityBonus) {

        this.treasureRarityBonus = treasureRarityBonus;
        return this;
    }

    public HookPartItem fishRarity(float fishRarity) {

        this.fishRarity = fishRarity;
        return this;
    }

    public HookPartItem fishRarityMultiplier(float fishRarityMultiplier) {

        this.fishRarityMultiplier = fishRarityMultiplier;
        return this;
    }

    public HookPartItem timeUntilHookedMultiplier(float timeUntilHookedMultiplier) {

        this.timeUntilHookedMultiplier = timeUntilHookedMultiplier;
        return this;
    }

    public HookPartItem timeHookedMultiplier(float timeHookedMultiplier) {

        this.timeHookedMultiplier = timeHookedMultiplier;
        return this;
    }
    @Override
    void applyComponent(RodConfiguration.AttributeProcessor configuration) {

        configuration.timeHookedMultiplier *= this.timeHookedMultiplier;
        configuration.timeUntilHookedMultiplier *= this.timeUntilHookedMultiplier;
        configuration.fishRarity += this.fishRarity;
        configuration.fishRarityMultiplier += this.fishRarityMultiplier;
        configuration.treasureBonus += this.treasureBonus;
        configuration.treasureRarityBonus += this.treasureRarityBonus;
        super.applyComponent(configuration);
    }
}
