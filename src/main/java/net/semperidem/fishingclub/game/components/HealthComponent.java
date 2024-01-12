package net.semperidem.fishingclub.game.components;

import net.semperidem.fishingclub.network.ClientPacketSender;

public class HealthComponent {
    private static final float BASE_HEALTH = 0;
    private float lineHealth;
    private final float fishDamage;

    public HealthComponent(float bonusHealth, float fishDamage) {
        this.lineHealth = BASE_HEALTH + bonusHealth;
        this.fishDamage = fishDamage;
    }


    public void tick(boolean shouldDealDamage){
        if (!shouldDealDamage) {
            return;
        }

        if (fishDamage != 0) {
            lineHealth -= (fishDamage);
        }

        if (lineHealth <= 0){
            ClientPacketSender.sendFishGameLost();//TODO MOVE TO PARENT SO ITS CLEAR HOW WIN/LOSE IS CALLED
        }
    }

    public float getLineHealth(){
        return lineHealth;
    }
}
