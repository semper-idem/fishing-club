package net.semperidem.fishingclub.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
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
    private static Pair<ServerWorld, FishermanEntity> DEREK;


    private SummonType summonType = SummonType.SPELL;
    private final ArrayList<UUID> talkedTo = new ArrayList<>();
    private UUID summonerUUID;

    public FishermanEntity(World world) {
        super(EntityTypeRegistry.FISHERMAN, world);
    }

    public static FishermanEntity getDerek(ServerWorld world, ItemStack spawnedFrom, UUID summonerUUID) {
        if (DEREK == null || DEREK.getLeft() != world) {
            DEREK = new Pair<>(world, new FishermanEntity(world));
        }
        discardOldDerek(world);//seems like cursed code fix later
        DEREK.getRight().setSummonDetails(spawnedFrom, summonerUUID);
        return DEREK.getRight();
    }

    private static void discardOldDerek(ServerWorld world) {
        for(Entity fisher : world.getEntitiesByType(EntityTypeRegistry.FISHERMAN, o -> true)) {
            if (fisher == DEREK.getRight()) {
                continue;
            }
            fisher.discard();
        }
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("summonType", summonType.name());
        if (this.summonerUUID != null) {
            nbt.putUuid("summonerUUID", summonerUUID);
        }
        NbtList talkedToUUIDNbt = new NbtList();
        for(UUID interactedWith : talkedTo) {
            talkedToUUIDNbt.add(NbtHelper.fromUuid(interactedWith));
        }
        nbt.put("talkedTo", talkedToUUIDNbt);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("summonType")) {
            this.summonType = SummonType.valueOf(nbt.getString("summonType"));
        }

        if (nbt.contains("summonerUUID")) {
            this.summonerUUID = nbt.getUuid("summonerUUID");
        }
        if (!nbt.contains("talkedTo")) {
            return;
        }
        nbt.getList("talkedTo", NbtElement.INT_ARRAY_TYPE).forEach(
                talkedToUUID -> talkedTo.add(NbtHelper.toUuid(talkedToUUID))
        );
    }


    private void setSummonDetails(ItemStack spawnedFrom, UUID summonerUUID) {
        if (spawnedFrom.isEmpty() || summonerUUID == null) {
            return;
        }
        this.summonType = spawnedFrom.isOf(FishUtil.FISH_ITEM) ? SummonType.HIGH_GRADE_FISH : SummonType.GOLDEN_FISH;
        this.summonerUUID = summonerUUID;
    }

    public HashSet<String> getKeys(PlayerEntity playerEntity){
        HashSet<String> fisherKeys = new HashSet<>();
        fisherKeys.add(playerEntity.getUuid() == summonerUUID ? "SUMMONER" : "NOT_SUMMONER");
        fisherKeys.add(talkedTo.contains(playerEntity.getUuid()) ? "REPEATED" : "NOT_REPEATED");
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
            talkedTo.add(playerEntity.getUuid());
            playerEntity.openHandledScreen(new DialogScreenHandlerFactory(keySet));
        }
        return ActionResult.CONSUME;
    }

    public static void onSummonEffect(ServerWorld serverWorld, FishermanEntity fishermanEntity) {
        serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, fishermanEntity.getX(), fishermanEntity.getY() + 1, fishermanEntity.getZ(), 50,1,1,1,0.01);
        serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, fishermanEntity.getX(), fishermanEntity.getY() + 1, fishermanEntity.getZ(), 50,1,1,1,0.02);
        serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, fishermanEntity.getX(), fishermanEntity.getY() + 1, fishermanEntity.getZ(), 50,1,1,1,0.03);
        serverWorld.spawnParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, fishermanEntity.getX(), fishermanEntity.getY() + 1, fishermanEntity.getZ(), 50,1,1,1,0.01);
        serverWorld.spawnParticles(ParticleTypes.EXPLOSION, fishermanEntity.getX(), fishermanEntity.getY() + 2, fishermanEntity.getZ(), 10,0.5,0.5,0.5,0.1);
        serverWorld.playSound(null, fishermanEntity.getX(), fishermanEntity.getY(), fishermanEntity.getZ(), SoundEvents.ITEM_BUCKET_FILL_FISH, SoundCategory.PLAYERS, 0.3f, 0.2f, 0L);
    }


    public enum SummonType {
        GOLDEN_FISH,
        HIGH_GRADE_FISH,
        SPELL
    }
}
