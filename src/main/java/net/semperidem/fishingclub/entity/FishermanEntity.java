package net.semperidem.fishingclub.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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


    @Override
    public void onDeath(DamageSource damageSource) {
        if (DEREK.getLeft() == this.world) {
            DEREK.setRight(null);
        }
        super.onDeath(damageSource);
    }

    private SummonType summonType = SummonType.SPELL;
    private final ArrayList<UUID> talkedTo = new ArrayList<>();
    private UUID summonerUUID;
    private BoatMoveControl boatMoveControl;


    @Override
    public void setMovementSpeed(float movementSpeed) {
        if (movementSpeed > 0 && (hasVehicle() && getVehicle() instanceof BoatEntity boatEntity)) {
            float r = movementSpeed;
            float x = (float) (r*Math.sin(getYaw()));
            float z = (float) (r*Math.cos(getYaw()));
            boatEntity.setVelocity(new Vec3d(x,0,z));
        }
        super.setMovementSpeed(movementSpeed);
    }

    @Override
    public void setYaw(float yaw) {
        super.setYaw(yaw);
        if ((hasVehicle() && getVehicle() instanceof BoatEntity boatEntity)) {
            boatEntity.setYaw(yaw);
        }
    }

    private static class BoatLookControl extends LookControl {
        public BoatLookControl(MobEntity entity) {
            super(entity);
        }

        public void tick() {
            if (this.shouldStayHorizontal()) {
                this.entity.setPitch(0.0F);
            }

            if (this.field_35103 > 0) {
                --this.field_35103;
                this.getTargetYaw().ifPresent((yaw) -> {
                    this.entity.headYaw = this.changeAngle(this.entity.headYaw, yaw, this.maxYawChange);
                    this.entity.setYaw(yaw);
                    if (entity.hasVehicle()) {
                        entity.getVehicle().setYaw(yaw);
                    }
                });
                this.getTargetPitch().ifPresent((pitch) -> {
                    this.entity.setPitch(this.changeAngle(this.entity.getPitch(), pitch, this.maxPitchChange));
                });
            } else {
                this.entity.headYaw = this.changeAngle(this.entity.headYaw, this.entity.bodyYaw, 10.0F);
            }

            this.clampHeadYaw();
        }
    }


    private static class BoatMoveControl extends MoveControl {
        public BoatMoveControl(MobEntity entity) {
            super(entity);
        }

        public void moveTo(double x, double y, double z, double speed) {
            this.targetX = x;
            this.targetY = y;
            this.targetZ = z;
            this.speed = speed;
            if (this.state != MoveControl.State.JUMPING) {
                this.state = MoveControl.State.MOVE_TO;
            }

        }
    }
    private static class BoatNavigation extends MobNavigation {
        BoatNavigation(FishermanEntity entity, World world) {
            super(entity, world);
        }

        protected PathNodeNavigator createPathNodeNavigator(int range) {
            this.nodeMaker = new LandPathNodeMaker();
            this.nodeMaker.setCanEnterOpenDoors(true);
            return new PathNodeNavigator(this.nodeMaker, range);
        }

        protected boolean canWalkOnPath(PathNodeType pathType) {
            return pathType != PathNodeType.WATER ? super.canWalkOnPath(pathType) : true;
        }

        @Override
        public boolean isValidPosition(BlockPos pos) {
            return this.world.getBlockState(pos).isOf(Blocks.WATER) || super.isValidPosition(pos);
        }
    }

    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new FleeEntityGoal(this, ZombieEntity.class, 8.0F, 0.5, 0.5));
        this.goalSelector.add(1, new FleeEntityGoal(this, EvokerEntity.class, 12.0F, 0.5, 0.5));
        this.goalSelector.add(1, new FleeEntityGoal(this, VindicatorEntity.class, 8.0F, 0.5, 0.5));
        this.goalSelector.add(1, new FleeEntityGoal(this, VexEntity.class, 8.0F, 0.5, 0.5));
        this.goalSelector.add(1, new FleeEntityGoal(this, PillagerEntity.class, 15.0F, 0.5, 0.5));
        this.goalSelector.add(1, new FleeEntityGoal(this, IllusionerEntity.class, 12.0F, 0.5, 0.5));
        this.goalSelector.add(1, new FleeEntityGoal(this, ZoglinEntity.class, 10.0F, 0.5, 0.5));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 0.5));
        this.goalSelector.add(2, new WanderAroundGoal(this, 1, 60));
        this.goalSelector.add(4, new GoToWalkTargetGoal(this, 0.35));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 0.35));
        this.goalSelector.add(9, new StopAndLookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
    }


    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WANDERING_TRADER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WANDERING_TRADER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WANDERING_TRADER_DEATH;
    }

    public FishermanEntity(World world) {
        super(EntityTypeRegistry.FISHERMAN, world);
        this.setCustomName(Text.of("Derek ol'Stinker"));
        this.moveControl = new BoatMoveControl(this);
        this.navigation = new BoatNavigation(this, world);
        this.lookControl = new BoatLookControl(this);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
    }


    private void moveToSummonPosition(Vec3d pos, World world) {
        while(world.isWater(new BlockPos(pos))) {
            pos = pos.add(0, 1, 0);
        }
        if (hasVehicle()) {
            getVehicle().setPosition(pos);
        }
        setPosition(pos);
    }


    private void putInBoat() {
        if (this.hasVehicle()) {
            return;
        }
        ChestBoatEntity boat = new ChestBoatEntity(EntityType.CHEST_BOAT, world);
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

    public static FishermanEntity getDerek(World world, ItemStack spawnedFrom, UUID summonerUUID) {
//        if (DEREK == null || DEREK.getLeft() != world || DEREK.getRight() == null) {
//            DEREK = new Pair<>(world, new FishermanEntity(world));
//        }
//        DEREK.getRight().setSummonDetails(spawnedFrom, summonerUUID);
        return new FishermanEntity(world);
    }

    public static void summonDerek(Vec3d pos, World world, ItemStack itemStack, UUID uuid) {
        FishermanEntity derek = getDerek(world, itemStack, uuid);
        derek.putInBoat();
        derek.moveToSummonPosition(pos, world);
        world.spawnEntity(derek);
        //serverWorld.getEntitiesByType(EntityTypeRegistry.FISHERMAN, o -> o != DEREK.getRight()).forEach(Entity::discard);
        if (world instanceof  ServerWorld serverWorld) {
            EffectUtils.onDerekSummonEffect(serverWorld, derek);
        }
    }

    public enum SummonType {
        GOLDEN,
        GRADE,
        SPELL
    }
}
