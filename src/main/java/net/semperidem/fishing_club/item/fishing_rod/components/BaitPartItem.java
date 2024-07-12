package net.semperidem.fishing_club.item.fishing_rod.components;

public class BaitPartItem extends PartItem {

    public BaitPartItem(Settings settings) {

        super(settings);
        this.destroyOnBreak = true;
        setDamageMultiplier(DamageSource.BITE, 1);
        setDamageMultiplier(DamageSource.REEL_FISH, 3);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 1);
        setDamageMultiplier(DamageSource.REEL_WATER, 1.5f);
        setDamageMultiplier(DamageSource.REEL_GROUND, 1);
    }


    @Override
    void applyComponent(RodConfiguration.Controller configuration) {

        super.applyComponent(configuration);
    }
}
