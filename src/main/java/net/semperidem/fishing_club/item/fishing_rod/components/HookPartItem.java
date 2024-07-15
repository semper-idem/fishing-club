package net.semperidem.fishing_club.item.fishing_rod.components;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.semperidem.fishing_club.fisher.managers.StatusEffectHelper;

import java.util.function.Consumer;

public class HookPartItem extends PartItem {
    private float biteFailChance = 0;
    private float autoHookChance = 0;
    private boolean sharp = false;
    private float damage = 0;
    private float reelDamage = 0;
    private boolean sticky = false;
    private Consumer<LivingEntity> onEntityHit;

    public static final Consumer<LivingEntity> ON_HIT_POISON = targetEntity -> {
        targetEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200));
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

    public HookPartItem setOnEntityHit(Consumer<LivingEntity> livingEntityConsumer) {
        this.onEntityHit = livingEntityConsumer;
        return this;
    }

    public void onEntityHit(LivingEntity entity){
        if (onEntityHit != null) {
            onEntityHit.accept(entity);
        }
    }


    public void onFishBiteEffect() {

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
    void applyComponent(RodConfiguration.AttributeProcessor configuration) {

        super.applyComponent(configuration);
    }
}
