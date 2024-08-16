package net.semperidem.fishingclub.item.fishing_rod.components;

public class BobberPartItem extends PartItem {

    ItemStat bobberWidth;
    ItemStat bobberControl;
    ItemStat waitTimeReductionMultiplier;
    ItemStat timeHookedMultiplier;
    Runnable fishBiteEffect = () -> {};

    public BobberPartItem(Settings settings) {

        super(settings);
        this.destroyOnBreak = true;
        setDamageMultiplier(DamageSource.CAST, 0);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 0);
        setDamageMultiplier(DamageSource.REEL_WATER, 1);
        setDamageMultiplier(DamageSource.REEL_GROUND, 2);
    }

    public BobberPartItem(Settings settings,int weightCapacity,  int minOperatingTemperature, int maxOperatingTemperature) {

        this(settings, weightCapacity);
        this.minOperatingTemperature = minOperatingTemperature;
        this.maxOperatingTemperature = maxOperatingTemperature;
    }

    public BobberPartItem(Settings settings,int weightCapacity) {

        this(settings);
        this.weightCapacity = weightCapacity;
    }

    public BobberPartItem bobberControl(ItemStat bobberControl) {

        this.bobberControl = bobberControl;
        return this;
    }

    public BobberPartItem bobberWidth(ItemStat bobberWidth) {

        this.bobberWidth = bobberWidth;
        return this;
    }

    public void onFishBiteEffect() {
        this.fishBiteEffect.run();
    }

    public BobberPartItem setFishBiteEffect(Runnable fishBiteEffect) {
        this.fishBiteEffect = fishBiteEffect;
        return this;
    }


    public BobberPartItem fishQuality(int fishQuality) {
        this.fishQuality = fishQuality;
        return this;
    }

    @Override
    public BobberPartItem fishControl(ItemStat fishControl) {
        this.fishControl = fishControl;
        return this;
    }

    @Override
    public BobberPartItem fishControlMultiplier(ItemStat fishControlMultiplier) {
        this.fishControlMultiplier = fishControlMultiplier;
        return this;
    }

    public BobberPartItem waitTimeReductionMultiplier(ItemStat waitTimeReductionMultiplier) {

        this.waitTimeReductionMultiplier = waitTimeReductionMultiplier;
        return this;
    }

    public BobberPartItem timeHookedMultiplier(ItemStat timeHookedMultiplier) {

        this.timeHookedMultiplier = timeHookedMultiplier;
        return this;
    }
    @Override
    void applyComponent(RodConfiguration.AttributeProcessor configuration) {

        configuration.bobberControl += this.bobberControl.value;
        configuration.timeHookedMultiplier *= this.timeHookedMultiplier.value;
        configuration.waitTimeReductionMultiplier *= this.waitTimeReductionMultiplier.value;
        configuration.bobberWidth = this.bobberWidth.value;
        super.applyComponent(configuration);
    }
}
