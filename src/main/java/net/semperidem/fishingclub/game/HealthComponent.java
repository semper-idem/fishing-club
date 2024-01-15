package net.semperidem.fishingclub.game;

import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodStatType;

public class HealthComponent {
    private static final float BASE_HEALTH = 0;
    private float health;
    private final FishGameController parent;

    public HealthComponent(FishGameController parent) {
        this.parent = parent;
        this.health = BASE_HEALTH + FishingRodPartController.getStat(parent.fish.caughtUsing, FishingRodStatType.LINE_HEALTH);
    }


    public void tick(boolean shouldDealDamage){
        if (!shouldDealDamage) {
            return;
        }
        if (parent.fish.damage != 0) {
            health -= (parent.fish.damage);
        }

        if (health <= 0){
            parent.loseGame();
        }
    }

    public float getHealth(){
        return health;
    }
}
