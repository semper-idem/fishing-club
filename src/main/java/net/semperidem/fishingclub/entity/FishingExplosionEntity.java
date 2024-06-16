package net.semperidem.fishingclub.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.ChunkPos;
import net.semperidem.fishingclub.fisher.FishingCard;

import javax.swing.text.html.parser.Entity;

public class FishingExplosionEntity implements IHookEntity{
    PlayerEntity causingEntity;
    ChunkPos chunkPos;

    public FishingExplosionEntity(PlayerEntity causingEntity, ChunkPos chunkPos) {
        this.causingEntity = causingEntity;
        this.chunkPos = chunkPos;
    }
    @Override
    public FishingCard getFishingCard() {
        return FishingCard.getPlayerCard(causingEntity);
    }

    @Override
    public ItemStack getCaughtUsing() {
        return Items.TNT.getDefaultStack();
    }

    @Override
    public ChunkPos getFishedInChunk() {
        return chunkPos;
    }
    @Override
    public float getFishMethodDebuff() {
        return 0.35f;
    }
}
