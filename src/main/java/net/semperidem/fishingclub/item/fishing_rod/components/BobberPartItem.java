package net.semperidem.fishingclub.item.fishing_rod.components;

public class BobberPartItem extends PartItem {
    ItemStat fishControl = ItemStat.BASE_T1;
    ItemStat bobberControl;
    ItemStat waitTimeReductionMultiplier;
    ItemStat timeHookedMultiplier;
    Runnable fishBiteEffect;

    public BobberPartItem(Settings settings) {
        super(settings);
        this.type = RodConfiguration.PartType.BOBBER;
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

    public BobberPartItem waitTimeReductionMultiplier(ItemStat waitTimeReductionMultiplier) {
        this.waitTimeReductionMultiplier = waitTimeReductionMultiplier;
        return this;
    }

    public BobberPartItem timeHookedMultiplier(ItemStat timeHookedMultiplier) {
        this.timeHookedMultiplier = timeHookedMultiplier;
        return this;
    }

    @Override
    void apply(RodConfiguration.AttributeComposite attributes) {
        attributes.fishControl += this.fishControl.value;
        attributes.bobberControl += this.bobberControl.value;
        attributes.timeHookedMultiplier *= this.timeHookedMultiplier.value;
        attributes.waitTimeReductionMultiplier *= this.waitTimeReductionMultiplier.value;
        super.apply(attributes);
    }

    public void     onFishBiteEffect() {
        if (this.fishBiteEffect == null) {
            return;
        }
        this.fishBiteEffect.run();
    }

}
