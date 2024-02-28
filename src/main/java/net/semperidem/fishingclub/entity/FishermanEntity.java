package net.semperidem.fishingclub.entity;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.semperidem.fishingclub.client.screen.dialog.DialogHelper;
import net.semperidem.fishingclub.client.screen.dialog.DialogScreenHandlerFactory;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.registry.EntityTypeRegistry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class FishermanEntity extends WanderingTraderEntity {
    private SummonType summonType = SummonType.SPELL;
    private ArrayList<PlayerEntity> talkedTo = new ArrayList<>();
    private UUID summonerUUID;
    public FishermanEntity(World world, ItemStack spawnedFrom, UUID summonerUUID) {
        super(EntityTypeRegistry.FISHERMAN, world);
        if (spawnedFrom.isEmpty() || summonerUUID == null) {
            return;
        }
        this.summonType = spawnedFrom.isOf(FishUtil.FISH_ITEM) ? SummonType.HIGH_GRADE_FISH : SummonType.GOLDEN_FISH;
        this.summonerUUID = summonerUUID;
    }

    public HashSet<String> getKeys(PlayerEntity playerEntity){
        HashSet<String> fisherKeys = new HashSet<>();
        fisherKeys.add(playerEntity.getUuid() == summonerUUID ? "SUMMONER" : "NOT_SUMMONER");
        fisherKeys.add(talkedTo.contains(playerEntity) ? "REPEATED" : "NOT_REPEATED");
        fisherKeys.add(summonType.name());
        return fisherKeys;
    }

    public SummonType getSummonType() {
        return summonType;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt) {
        this.setCustomName(Text.of("Derek ol'Stinker"));
        if (!this.hasVehicle()) {
            // Create the boat.
            ChestBoatEntity boat = new ChestBoatEntity(EntityType.CHEST_BOAT, (World) world);
            boat.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0, 0.0F);
            this.startRiding(boat);
            world.spawnEntity(boat);
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public ActionResult interactMob(PlayerEntity playerEntity, Hand hand) {
        if(!isClient()) {
            HashSet<String> keySet = DialogHelper.getKeys(playerEntity, this);
            FishingCard.getPlayerCard(playerEntity).meetDerek(summonType);
            talkedTo.add(playerEntity);
            playerEntity.openHandledScreen(new DialogScreenHandlerFactory(keySet));
        }
        return ActionResult.CONSUME;
    }


    public enum SummonType {
        GOLDEN_FISH,
        HIGH_GRADE_FISH,
        SPELL
    }
}
