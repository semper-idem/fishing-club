package net.semperidem.fishingclub.game;

import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.network.payload.FishingUpdatePayload;

public class HealthComponent {
    private float health;
    private final FishingController parent;
    private float damageReduction = 0;
    private final float strainTriggerDistance;

    public HealthComponent(FishingController parent) {
        this.parent = parent;
        this.health = MathHelper.clamp(parent.card.tradeSecretValue(TradeSecrets.LINE_HEALTH_BOAT), 1, 4);
        this.strainTriggerDistance = this.parent.rodConfiguration.attributes().bobberWidth() * BobberComponent.BASE_LENGTH;
        this.damageReduction = this.parent.rodConfiguration.attributes().fishControl();
    }

    public void updateClient(FishingUpdatePayload payload) {
        this.health = payload.health();
    }

    public void tick() {
        if (this.health <= 0) {
            this.parent.loseGame();
        }

        float bobberToFishDistance = Math.abs(this.parent.getFishPosX() - this.parent.getBobberPos());
        if (bobberToFishDistance < this.strainTriggerDistance) {
            return;
        }
        float damageScale = MathHelper.clamp(bobberToFishDistance / this.strainTriggerDistance, 0, 1);
        this.health -= this.parent.hookedFish.damage() * damageScale;

    }

    public void damage() {
        this.health -= this.parent.hookedFish.damage() * (1 - this.damageReduction);
    }

    public float getHealth() {
        return health;
    }
}
