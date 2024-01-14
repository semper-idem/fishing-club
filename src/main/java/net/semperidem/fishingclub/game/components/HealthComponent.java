package net.semperidem.fishingclub.game.components;

import net.semperidem.fishingclub.game.FishGameController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodStatType;

public class HealthComponent {
    private static final float BASE_HEALTH = 0;
    private float lineHealth;
    private final float fishDamage;
    private final FishGameController parent;

    public HealthComponent(FishGameController parent) {
        this.parent = parent;
        this.lineHealth = BASE_HEALTH + FishingRodPartController.getStat(parent.fish.caughtUsing, FishingRodStatType.LINE_HEALTH);
        this.fishDamage = parent.fish.damage;
    }


    public void tick(boolean shouldDealDamage){
        if (!shouldDealDamage) {
            return;
        }

        if (fishDamage != 0) {
            lineHealth -= (fishDamage);
        }

        if (lineHealth <= 0){
            parent.loseGame();
        }
    }

    public float getLineHealth(){
        return lineHealth;
    }
}
