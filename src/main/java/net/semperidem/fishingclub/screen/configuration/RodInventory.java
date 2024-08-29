package net.semperidem.fishingclub.screen.configuration;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;

public class RodInventory extends SimpleInventory {
    public static final int SIZE = 5;
    PlayerEntity playerEntity;
    public RodInventory(PlayerEntity playerEntity) {
        super(SIZE);
        this.playerEntity = playerEntity;
    }

    public void dropContent() {
        for(int i = 0; i < SIZE; i++) {
            this.playerEntity.dropItem(this.getStack(i), true, true);
        }
    }
}
