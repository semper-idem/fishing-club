package net.semperidem.fishingclub.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.registry.EntityTypeRegistry;
import net.semperidem.fishingclub.screen.dialog.DialogKey;
import net.semperidem.fishingclub.screen.dialog.DialogScreenHandlerFactory;
import net.semperidem.fishingclub.screen.dialog.DialogUtil;
import net.semperidem.fishingclub.util.EffectUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;


public class FishermanEntity extends PassiveEntity {
    private static Pair<ServerWorld, FishermanEntity> DEREK;


    private SummonType summonType = SummonType.SPELL;
    private final ArrayList<UUID> talkedTo = new ArrayList<>();
    private UUID summonerUUID;

    public static FishermanEntity getDerek(ServerWorld world, ItemStack spawnedFrom, UUID summonerUUID) {
        if (DEREK == null || DEREK.getLeft() != world) {
            DEREK = new Pair<>(world, new FishermanEntity(world));
        }
        DEREK.getRight().setSummonDetails(spawnedFrom, summonerUUID);
        return DEREK.getRight();
    }

    public static void summonDerek(Vec3d pos, ServerWorld serverWorld, ItemStack itemStack, UUID uuid) {
        FishermanEntity derek = getDerek(serverWorld, itemStack, uuid);
        derek.putInBoat();
        derek.moveToSummonPosition(pos, serverWorld);
        serverWorld.spawnEntity(derek);
        serverWorld.getEntitiesByType(EntityTypeRegistry.FISHERMAN, o -> o != DEREK.getRight()).forEach(Entity::discard);
        EffectUtils.onDerekSummonEffect(serverWorld, derek);
    }

    public FishermanEntity(World world) {
        super(EntityTypeRegistry.FISHERMAN, world);
        this.setCustomName(Text.of("Derek ol'Stinker"));
    }


    private void moveToSummonPosition(Vec3d pos, ServerWorld serverWorld) {
        while(serverWorld.isWater(new BlockPos(pos))) {
            pos = pos.add(0, 1, 0);
        }
        if (hasVehicle()) {
            getVehicle().setPosition(pos);
        }
        setPosition(pos);
    }


    private void putInBoat() {
        if (this.fluidHeight.getDouble(FluidTags.WATER) == 0.0 || this.hasVehicle()) {
            return;
        }
        ChestBoatEntity boat = new ChestBoatEntity(EntityType.CHEST_BOAT, world);
        boat.setPosition(getX(), getY(), getZ());
        startRiding(boat);
        world.spawnEntity(boat);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
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
        if (world instanceof ServerWorld serverWorld){
            DEREK = new Pair<>(serverWorld, this);
        }
    }


    private void setSummonDetails(ItemStack spawnedFrom, UUID summonerUUID) {
        if (spawnedFrom.isEmpty() || summonerUUID == null) {
            return;
        }
        this.summonType = spawnedFrom.isOf(FishUtil.FISH_ITEM) ? SummonType.GRADE : SummonType.GOLDEN;
        this.summonerUUID = summonerUUID;
    }

    public HashSet<DialogKey> getKeys(PlayerEntity playerEntity){
        HashSet<DialogKey> fisherKeys = new HashSet<>();
        fisherKeys.add(playerEntity.getUuid() == summonerUUID ? DialogKey.SUMMONER : DialogKey.NOT_SUMMONER);
        fisherKeys.add(talkedTo.contains(playerEntity.getUuid()) ? DialogKey.REPEATED : DialogKey.NOT_REPEATED);
        fisherKeys.add(DialogKey.valueOf(summonType.name()));
        return fisherKeys;
    }

    public SummonType getSummonType() {
        return summonType;
    }

    @Override
    public ActionResult interactMob(PlayerEntity playerEntity, Hand hand) {
        if(!world.isClient) {
            HashSet<DialogKey> keySet = DialogUtil.getKeys(playerEntity, this);
            FishingCard.getPlayerCard(playerEntity).meetDerek(summonType);
            talkedTo.add(playerEntity.getUuid());
            playerEntity.openHandledScreen(new DialogScreenHandlerFactory(keySet));
        }
        return ActionResult.CONSUME;
    }

    public enum SummonType {
        GOLDEN,
        GRADE,
        SPELL
    }
}
