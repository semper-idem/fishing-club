package net.semperidem.fishing_club.game;

import net.minecraft.network.PacketByteBuf;
import net.semperidem.fishing_club.network.payload.FishingGameTickPayload;

public class HealthComponent {
    private static final float BASE_HEALTH = 1;
    private float health;
    private final FishingGameController parent;

    public HealthComponent(FishingGameController parent) {
        this.parent = parent;
        this.health = BASE_HEALTH ;//+ FishingRodPartController.getStat(parent.hookedFish.caughtUsing, FishingRodStatType.LINE_HEALTH);
    }


    public void consumeData(FishingGameTickPayload payload) {
        this.health = payload.health();
    }

    public void writeData(PacketByteBuf buf) {
        buf.writeFloat(health);
    }

    public void tick(){
        if (!parent.progressComponent.isWinning()) {
            return;
        }
        if (parent.hookedFish.damage() != 0) {
            health -= (parent.hookedFish.damage());
        }

        if (health <= 0){
            parent.loseGame();
        }
    }

    public float getHealth(){
        return health;
    }
}
