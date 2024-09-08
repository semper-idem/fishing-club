package net.semperidem.fishingclub.game;

import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.network.payload.FishingGameTickPayload;

public class HealthComponent {
    private static final float BASE_HEALTH = 1;
    private static final float MAX_HEALTH = 1024;
    private float health;
    private final FishingGameController parent;

    public HealthComponent(FishingGameController parent) {
        this.parent = parent;
        int rodHealth = parent.rodConfiguration.attributes().maxLineLength();
        int fisherHealth = parent.fishingCard.tradeSecretValue(TradeSecrets.LINE_HEALTH_BOAT);
        this.health = MathHelper.clamp(
                rodHealth + fisherHealth,
                BASE_HEALTH,
                MAX_HEALTH
        );
    }


    public void consumeData(FishingGameTickPayload payload) {
        this.health = payload.health();
    }

    public void tick() {
        if (!parent.progressComponent.isWinning()) {
            return;
        }
        if (parent.hookedFish.damage() != 0) {
            health -= (parent.hookedFish.damage());
        }

        if (health <= 0) {
            parent.loseGame();
        }
    }

    public float getHealth() {
        return health;
    }
}
