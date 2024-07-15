package net.semperidem.fishing_club.item.fishing_rod.components;

import net.minecraft.entity.LivingEntity;

public class HookPartItem extends PartItem {
    private float biteFailChance = 0;
    private float autoHookChance = 0;
    private boolean piercing = false;
    private float damage = 0;
    private float reelDamage = 0;
    private boolean sticky = false;

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

    public void onEntityHit(LivingEntity entity){

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

    public boolean isPiercing() {
        return this.piercing;
    }

    public HookPartItem piercing(boolean piercing) {
        this.piercing = piercing;
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
    void applyComponent(RodConfiguration.Controller configuration) {

        super.applyComponent(configuration);
    }
}
