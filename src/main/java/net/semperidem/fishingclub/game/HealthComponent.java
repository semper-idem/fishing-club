package net.semperidem.fishingclub.game;

import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.network.payload.FishingGameTickS2CPayload;

public class HealthComponent {
    private static final float BASE_HEALTH = 1;
    private static final float MAX_HEALTH = 32;
    private float health;
    private final FishingGameController parent;

    public HealthComponent(FishingGameController parent) {
        this.parent = parent;
        int fisherHealth = parent.fishingCard.tradeSecretValue(TradeSecrets.LINE_HEALTH_BOAT);
        this.health = MathHelper.clamp(
                fisherHealth,
                BASE_HEALTH,
                MAX_HEALTH
        );
    }


    public void updateClient(FishingGameTickS2CPayload payload) {
        this.health = payload.health();
    }

    public void damage() {
        health -= this.parent.hookedFish.damage();

        if (health <= 0) {
            parent.loseGame();
        }
    }

    public float getHealth() {
        return health;
    }
}
