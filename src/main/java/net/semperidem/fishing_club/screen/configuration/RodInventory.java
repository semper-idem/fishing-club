package net.semperidem.fishing_club.screen.configuration;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;

public class RodInventory extends SimpleInventory {
    PlayerEntity playerEntity;
    public RodInventory(PlayerEntity playerEntity) {
        super(6);
        this.playerEntity = playerEntity;
    }
}
