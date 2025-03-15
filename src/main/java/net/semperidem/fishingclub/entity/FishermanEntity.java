package net.semperidem.fishingclub.entity;

import com.mojang.logging.LogUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.semperidem.fishingclub.screen.member.MemberScreenHandlerFactory;
import net.semperidem.fishingclub.world.FishingServerWorld;
import net.semperidem.fishingclub.registry.FCEntityTypes;
import net.semperidem.fishingclub.screen.member.MemberScreenHandler;
import net.semperidem.fishingclub.util.EffectUtils;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


public class FishermanEntity extends PassiveEntity {
    private static final TrackedData<Integer> HEAD_ROLLING_TIME_LEFT = DataTracker.registerData(FishermanEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final double TWO_PI = Math.PI * 2;
    private static final double QUARTER_PI = Math.PI * 0.25f;
    private static final double SIXTEENTH_PI = Math.PI * 0.06125f;
	private float paddlePhases;
    private PlayerEntity customer;
    private final static int DESPAWN_TIME = 6000;
    private int despawnTimer;
    private int outOfWaterTicks;
    private CustomBoatEntity boat;

    public FishermanEntity(EntityType<? extends FishermanEntity> entityType, World world) {
        super(entityType, world);
        this.setCustomName(Text.of("Derek ol'Stinker"));
        this.intersectionChecked = true;
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        this.despawnTimer = DESPAWN_TIME;
    }
    public FishermanEntity(World world) {
        this(FCEntityTypes.DEREK_ENTITY, world);
    }

    public int getHeadRollingTimeLeft() {
        return (Integer)this.dataTracker.get(HEAD_ROLLING_TIME_LEFT);
    }

    public void setHeadRollingTimeLeft(int ticks) {
        this.dataTracker.set(HEAD_ROLLING_TIME_LEFT, ticks);
    }

    public int getExperience() {
        return 0;
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(HEAD_ROLLING_TIME_LEFT, 0);
    }

    @Override
    protected float turnHead(float bodyRotation, float headRotation) {
        return super.turnHead(bodyRotation, headRotation);
    }

    @Override
    public void setHeadYaw(float headYaw) {
        super.setHeadYaw(headYaw);
    }

    @Override
    public void setYaw(float yaw) {
        super.setYaw(yaw);
    }

    public void tickBoat() {
        if (this.boat != null) {
            return;
        }
        this.boat = new CustomBoatEntity(FCEntityTypes.BOAT_ENTITY, this.getWorld());
        this.getWorld().spawnEntity(boat);
        this.boat.startRiding(this, true);
    }


    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengerList().size() <= 2 && !this.isSubmergedIn(FluidTags.WATER);
    }

    @Override
    protected boolean hasCollidedSoftly(Vec3d adjustedMovement) {
        return super.hasCollidedSoftly(adjustedMovement);
    }

    @Override
    public boolean collidesWith(Entity other) {
        return !(other instanceof BoatEntity);
    }


    protected void initGoals() {
//        this.goalSelector.add(1, new EscapeDangerGoal(this, 1));
//        this.goalSelector.add(2, new WanderAroundGoal(this, 0.35));
//        this.goalSelector.add(4, new GoToWalkTargetGoal(this, 0.35));
//        this.goalSelector.add(8, new WanderAroundFarGoal(this, 0.35));
//        this.goalSelector.add(9, new StopAndLookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
    }

    public void setDespawnTimer(int value) {
        despawnTimer = value;
    }

    public boolean canWalkOnFluid(FluidState state) {
        return state.isIn(FluidTags.WATER);
    }

    private boolean isPaddleMoving() {
        return this.limbAnimator.isLimbMoving();
    }

    public int getOutOfWaterTicks() {
        return outOfWaterTicks;
    }


    protected SoundEvent getPaddleSoundEvent() {
        return SoundEvents.ENTITY_BOAT_PADDLE_WATER;
    }

    @Override
    public void tickMovement() {
        if (customer != null) {
            if (!this.getWorld().isClient) {
                if (!(customer.currentScreenHandler instanceof MemberScreenHandler)) {
                    customer = null;
                }
            }
            return;
        }
//        double r = 0.3;
//        double radians = Math.toRadians(headYaw + 90);
//        double x = Math.cos(radians) * r;
//        double z = Math.sin(radians) * r;
//        this.boatBack.setPosition(this.getPos().add(x,0,z));
//        this.boatFront.setPosition(this.getPos().add(-x,0,-z));
        super.tickMovement();
    }

    @Override
    public void move(MovementType movementType, Vec3d movement) {
        super.move(movementType, movement);
    }

    private void tickPaddles() {
        if (!this.isPaddleMoving()) {
            return;
        }
        this.paddlePhases += (float) SIXTEENTH_PI;
        this.paddlePhases %= (float) TWO_PI;
        if (isSilent()){
            return;
        }
        if ((this.paddlePhases % TWO_PI) <= QUARTER_PI && ((this.paddlePhases + SIXTEENTH_PI) % TWO_PI) >= QUARTER_PI) {
            SoundEvent soundEvent = this.getPaddleSoundEvent();
            this.getWorld().playSoundFromEntity(null, this, soundEvent, this.getSoundCategory(), 1.0F, 0.8F + 0.4F * this.random.nextFloat());
        }
    }

    private void tickBuoyancy() {
        if (!isInWater()) {
            outOfWaterTicks++;
           return;
        }
        outOfWaterTicks = 0;
        ShapeContext shapeContext = ShapeContext.of(this);
        if (shapeContext.isAbove(FluidBlock.COLLISION_SHAPE, this.getBlockPos(), true) && !this.getWorld().getFluidState(this.getBlockPos().up()).isIn(FluidTags.WATER)) {
            this.setOnGround(true);
            return;
        }
        this.setVelocity(this.getVelocity().multiply(0.5).add(0.0, 0.05, 0.0));
        //checkBlockCollision(); from strider method
    }

    private void tickDespawnTimer() {
        if (customer == null) {
            despawnTimer--;
        }
        if (despawnTimer > 0) {
            return;
        }
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            EffectUtils.onDerekDisappearEffect(serverWorld, this);
        }
        this.discard();
    }

    @Override
    public void tick() {
        tickBoat();
        tickDespawnTimer();
        tickPaddles();
        tickBuoyancy();

        super.tick();
        if (!(this.getWorld() instanceof FishingServerWorld fishingWorld)) {
            return;
        }

        if (fishingWorld.getDerek() == null) {
            fishingWorld.setDerek(this);
        }
        if (fishingWorld.getDerek() != this) {
            fishingWorld.getDerek().discard();
        }

    }

    public boolean isInWater() {
        return !this.firstUpdate && this.fluidHeight.getDouble(FluidTags.WATER) > 0.0;
    }

    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        if (world.getBlockState(pos).getFluidState().isIn(FluidTags.WATER) && world.getBlockState(pos.up()).getFluidState().isEmpty()) {
            return 10.0F;
        }
        return this.isInWater() ? Float.NEGATIVE_INFINITY : 0.0F;
    }

    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
        this.checkBlockCollision();
        if (isInWater()) {
            this.onLanding();
        } else {
            super.fall(heightDifference, onGround, state, landedPosition);
        }
    }

    public float interpolatePaddlePhase(float tickDelta) {
        return this.isPaddleMoving() ? (float) MathHelper.clampedLerp(this.paddlePhases - SIXTEENTH_PI, this.paddlePhases, tickDelta) : this.paddlePhases;
    }

    private static class BoatNavigation extends MobNavigation {
        BoatNavigation(FishermanEntity entity, World world) {
            super(entity, world);
        }

        protected PathNodeNavigator createPathNodeNavigator(int range) {
            this.nodeMaker = new AmphibiousPathNodeMaker(false);
            this.nodeMaker.setCanEnterOpenDoors(true);
            return new PathNodeNavigator(this.nodeMaker, range);
        }

        protected boolean canWalkOnPath(PathNodeType pathType) {
            return pathType == PathNodeType.WATER || super.canWalkOnPath(pathType);
        }

        @Override
        public boolean isValidPosition(BlockPos pos) {
            return this.world.getBlockState(pos).isOf(Blocks.WATER) || super.isValidPosition(pos);
        }
    }
    protected EntityNavigation createNavigation(World world) {
        return new BoatNavigation(this, world);
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

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }


    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (this.getWorld() instanceof FishingServerWorld serverWorld){
            serverWorld.setDerek(this);
        }
    }

    private void sayNo() {
        this.setHeadRollingTimeLeft(40);
        if (!this.getWorld().isClient()) {
            this.playSound(SoundEvents.ENTITY_VILLAGER_NO);
        }

    }


    @Override
    public ActionResult interactMob(PlayerEntity playerEntity, Hand hand) {
        if(!this.getWorld().isClient && customer == null) {
            playerEntity.openHandledScreen(new MemberScreenHandlerFactory());
            setCustomer(playerEntity);
        }
        return ActionResult.CONSUME;
    }

    public void setCustomer(@Nullable PlayerEntity customer) {
        this.customer = customer;
    }


    //todo make unsummonable(teleportable if talked to someone <60s ago)
    public static void summonDerek(Vec3d pos, ServerWorld serverWorld, ItemStack itemStack, UUID uuid) {
        if (!(serverWorld instanceof  FishingServerWorld derekWorld)) {
            return;
        }
        FishermanEntity derek = derekWorld.getDerek(itemStack, uuid);
        Vec3d waterSurface = new Vec3d(pos.x, serverWorld.getTopY(Heightmap.Type.MOTION_BLOCKING, (int)pos.x, (int)pos.z), pos.z);
        derek.refreshPositionAndAngles(waterSurface.x, waterSurface.y, waterSurface.z ,derek.getYaw(), derek.getPitch());
        serverWorld.spawnEntity(derek);
        EffectUtils.onDerekSummonEffect(serverWorld, derek);
    }

    public enum SummonType {
        GOLDEN,
        FISH,
        SPELL
    }
}
