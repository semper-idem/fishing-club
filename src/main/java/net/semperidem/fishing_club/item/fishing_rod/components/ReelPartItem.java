package net.semperidem.fishing_club.item.fishing_rod.components;

public class ReelPartItem extends PartItem {

    public ReelPartItem(Settings settings) {

        super(settings);
        setDamageMultiplier(DamageSource.CAST, 0);
        setDamageMultiplier(DamageSource.REEL_FISH, 1);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 3);
    }

    @Override
    void applyComponent(RodConfiguration.Controller configuration) {

        super.applyComponent(configuration);
    }
}
