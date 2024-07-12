package net.semperidem.fishing_club.item.fishing_rod.components;

public class BobberPartItem extends PartItem {

    float bobberWidth;

    public BobberPartItem(Settings settings) {

        super(settings);
        this.destroyOnBreak = true;
        setDamageMultiplier(DamageSource.CAST, 0);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 0);
        setDamageMultiplier(DamageSource.REEL_WATER, 1);
        setDamageMultiplier(DamageSource.REEL_GROUND, 2);
    }

    public BobberPartItem bobberWidth(float bobberWidth) {

        this.bobberWidth = bobberWidth;
        return this;
    }

    @Override
    void applyComponent(RodConfiguration.Controller configuration) {

        super.applyComponent(configuration);
        configuration.bobberWidth = this.bobberWidth;
    }
}
