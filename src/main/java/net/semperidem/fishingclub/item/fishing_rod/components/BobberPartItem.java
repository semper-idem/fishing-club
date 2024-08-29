package net.semperidem.fishingclub.item.fishing_rod.components;

public class BobberPartItem extends PartItem {

    ItemStat fishControl = ItemStat.BASE_T1;
    ItemStat fishControlMultiplier = ItemStat.MULTIPLIER_T3;

    ItemStat bobberWidth;
    ItemStat bobberControl;
    ItemStat waitTimeReductionMultiplier;
    ItemStat timeHookedMultiplier;
    Runnable fishBiteEffect = () -> {};

    public BobberPartItem(Settings settings) {

        super(settings);
        this.partType = RodConfiguration.PartType.BOBBER;
        this.destroyOnBreak = true;
        setDamageMultiplier(DamageSource.CAST, 0);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 0);
        setDamageMultiplier(DamageSource.REEL_WATER, 1);
        setDamageMultiplier(DamageSource.REEL_GROUND, 2);
    }

    public BobberPartItem weightClass(int weightClass) {
        this.weightClass = weightClass;
        return this;
    }

    public BobberPartItem minOperatingTemperature(int minOperatingTemperature) {
        this.minOperatingTemperature = minOperatingTemperature;
        return this;
    }

    public BobberPartItem maxOperatingTemperature(int maxOperatingTemperature) {
        this.maxOperatingTemperature = maxOperatingTemperature;
        return this;
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

    public BobberPartItem fishControl(ItemStat fishControl) {
        this.fishControl = fishControl;
        return this;
    }

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
    void applyComponent(RodConfiguration.AttributeComposite configuration) {


        configuration.fishControl += this.fishControl.value;
        configuration.fishControlMultiplier *= this.fishControlMultiplier.value;
        configuration.bobberControl += this.bobberControl.value;
        configuration.timeHookedMultiplier *= this.timeHookedMultiplier.value;
        configuration.waitTimeReductionMultiplier *= this.waitTimeReductionMultiplier.value;
        configuration.bobberWidth = this.bobberWidth.value;
        super.applyComponent(configuration);
    }
}
