package net.semperidem.fishingclub.entity;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.network.ServerPacketSender;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;


public class CustomFishingBobberEntity extends FishingBobberEntity {
    private final Random velocityRandom = Random.create();
    private boolean caughtFish;
    private int outOfOpenWaterTicks;
    private static final TrackedData<Integer> HOOK_ENTITY_ID = DataTracker.registerData(CustomFishingBobberEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> CAUGHT_FISH = DataTracker.registerData(CustomFishingBobberEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int removalTimer;
    private int hookCountdown;
    private int waitCountdown;
    private int fishTravelCountdown;
    private float fishAngle;
    private boolean inOpenWater = true;
    @Nullable
    private Entity hookedEntity;
    private State state = State.FLYING;



    public CustomFishingBobberEntity(EntityType<? extends CustomFishingBobberEntity> entityEntityType, World world) {
        super(entityEntityType, world);
    }

    public CustomFishingBobberEntity(PlayerEntity owner, World world) {
        this(FishingClub.CUSTOM_FISHING_BOBBER, world);
        this.setOwner(owner);
        setThrowDirection();
    }

    private void setThrowDirection(){
        PlayerEntity owner = getPlayerOwner();
        float playerPitch = owner.getPitch();
        float playerYaw = owner.getYaw();
        float h = MathHelper.cos(-playerYaw * ((float)Math.PI / 180) - (float)Math.PI);
        float k = MathHelper.sin(-playerYaw * ((float)Math.PI / 180) - (float)Math.PI);
        float l = -MathHelper.cos(-playerPitch * ((float)Math.PI / 180));
        float m = MathHelper.sin(-playerPitch * ((float)Math.PI / 180));
        double d = owner.getX() - (double)k * 0.3;
        double e = owner.getEyeY();
        double n = owner.getZ() - (double)h * 0.3;
        this.refreshPositionAndAngles(d, e, n, playerYaw, playerPitch);
        Vec3d vec3d = new Vec3d(-k, MathHelper.clamp(-(m / l), -5.0f, 5.0f), -h);
        double o = vec3d.length();
        vec3d = vec3d.multiply(0.6 / o + this.random.nextTriangular(0.5, 0.0103365), 0.6 / o + this.random.nextTriangular(0.5, 0.0103365), 0.6 / o + this.random.nextTriangular(0.5, 0.0103365));
        this.setVelocity(vec3d);
        this.setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
        this.setPitch((float)(MathHelper.atan2(vec3d.y, vec3d.horizontalLength()) * 57.2957763671875));
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();
    }


    @Override
    public void tick() {
        this.velocityRandom.setSeed(this.getUuid().getLeastSignificantBits() ^ this.world.getTime());
        PlayerEntity owner = this.getPlayerOwner();
        if (owner == null) {
            this.discard();
            return;
        }
        if (!this.world.isClient && this.removeIfInvalid(owner)) {
            return;
        }
        if (this.onGround) {
            ++this.removalTimer;
            if (this.removalTimer >= 1200) {
                this.discard();
                return;
            }
        } else {
            this.removalTimer = 0;
        }
        float waterHeight = 0.0f;
        BlockPos blockPos = this.getBlockPos();
        FluidState fluidState = this.world.getFluidState(blockPos);
        if (fluidState.isIn(FluidTags.WATER)) {
            waterHeight = fluidState.getHeight(this.world, blockPos);
        }
        boolean isBobbing = waterHeight > 0.0f;
        if (this.state == State.FLYING) {
            if (this.hookedEntity != null) {
                this.setVelocity(Vec3d.ZERO);
                this.state = State.HOOKED_IN_ENTITY;
                return;
            }
            if (isBobbing) {
                this.setVelocity(this.getVelocity().multiply(0.3, 0.2, 0.3));
                this.state = State.BOBBING;
                return;
            }
            this.checkForCollision();
        } else {
            if (this.state == State.HOOKED_IN_ENTITY) {
                if (this.hookedEntity != null) {
                    if (this.hookedEntity.isRemoved() || this.hookedEntity.world.getRegistryKey() != this.world.getRegistryKey()) {
                        this.updateHookedEntityId(null);
                        this.state = State.FLYING;
                    } else {
                        this.setPosition(this.hookedEntity.getX(), this.hookedEntity.getBodyY(0.8), this.hookedEntity.getZ());
                    }
                }
                return;
            }
            if (this.state == State.BOBBING) {
                Vec3d vec3d = this.getVelocity();
                double d = this.getY() + vec3d.y - (double)blockPos.getY() - (double)waterHeight;
                if (Math.abs(d) < 0.01) {
                    d += Math.signum(d) * 0.1;
                }
                this.setVelocity(vec3d.x * 0.9, vec3d.y - d * (double)this.random.nextFloat() * 0.2, vec3d.z * 0.9);
                this.inOpenWater = this.hookCountdown <= 0 && this.fishTravelCountdown <= 0 || this.inOpenWater && this.outOfOpenWaterTicks < 10 && this.isOpenOrWaterAround(blockPos);
                if (isBobbing) {
                    this.outOfOpenWaterTicks = Math.max(0, this.outOfOpenWaterTicks - 1);
                    if (this.caughtFish) {
                        this.setVelocity(this.getVelocity().add(0.0, -0.05 * (double)this.velocityRandom.nextFloat() * (double)this.velocityRandom.nextFloat(), 0.0));
                    }
                    if (!this.world.isClient) {
                        this.tickFishingLogic(blockPos);
                    }
                } else {
                    this.outOfOpenWaterTicks = Math.min(10, this.outOfOpenWaterTicks + 1);
                }
            }
        }
        if (!fluidState.isIn(FluidTags.WATER)) {
            this.setVelocity(this.getVelocity().add(0.0, -0.03, 0.0));
        }
        this.move(MovementType.SELF, this.getVelocity());
        this.updateRotation();
        if (this.state == State.FLYING && (this.onGround || this.horizontalCollision)) {
            this.setVelocity(Vec3d.ZERO);
        }
        this.setVelocity(this.getVelocity().multiply(0.92));
        this.refreshPosition();
    }


    private void tickFishingLogic(BlockPos blockPos) {
        ServerWorld serverWorld = (ServerWorld)this.world;
        int countdownDecrement = getCountdownDecrement(blockPos);

        if (this.hookCountdown > 0) {
            tickHookedFish();
        } else if (this.fishTravelCountdown > 0) {
            tickFish(serverWorld, countdownDecrement);
        } else if (this.waitCountdown > 0) {
            tickWait(serverWorld, countdownDecrement);
        } else {
            setWaitCountdown();
        }
    }

    private void tickHookedFish(){
        --this.hookCountdown;
        if (this.hookCountdown <= 0) {
            this.waitCountdown = 0;
            this.fishTravelCountdown = 0;
            this.getDataTracker().set(CAUGHT_FISH, false);
        }
    }

    private void tickFish(ServerWorld serverWorld, int countdownDecrement){
        this.fishTravelCountdown -= countdownDecrement;
        if (this.fishTravelCountdown > 0) {
            tickFishReeling(serverWorld);
        } else {
            this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 2f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.4f);
            double m = this.getY() + 0.5;
            serverWorld.spawnParticles(ParticleTypes.FIREWORK, this.getX(), m, this.getZ(), (int)(1.0f + this.getWidth() * 20.0f), this.getWidth(), 0.0, this.getWidth(), 0.2f);
            serverWorld.spawnParticles(ParticleTypes.BUBBLE, this.getX(), m, this.getZ(), (int)(1.0f + this.getWidth() * 20.0f), this.getWidth(), 0.0, this.getWidth(), 0.2f);
            serverWorld.spawnParticles(ParticleTypes.FISHING, this.getX(), m, this.getZ(), (int)(1.0f + this.getWidth() * 20.0f), this.getWidth(), 0.0, this.getWidth(), 0.2f);
            this.hookCountdown = MathHelper.nextInt(this.random, 40, 80);
            this.getDataTracker().set(CAUGHT_FISH, true);
        }
    }
    private void tickFishReeling(ServerWorld serverWorld){
        this.fishAngle += (float)this.random.nextTriangular(0.0, 9.188);
        float f = this.fishAngle * ((float)Math.PI / 180);
        float g = MathHelper.sin(f);
        float h = MathHelper.cos(f);

        double j = this.getZ() + (double)(h * (float)this.fishTravelCountdown * 0.1f);
        double e = ((float)MathHelper.floor(this.getY()) + 1.0f);
        double d = this.getX() + (double)(g * (float)this.fishTravelCountdown * 0.1f);
        BlockState blockState = serverWorld.getBlockState(new BlockPos(d, e - 1.0, j));
        if (blockState.isOf(Blocks.WATER)) {
            if (this.random.nextFloat() < 0.15f) {
                serverWorld.spawnParticles(ParticleTypes.BUBBLE, d, e - (double)0.1f, j, 1, g, 0.1, h, 0.0);
            }
            //TODO hide behind skill
            float k = g * 0.04f;
            float l = h * 0.04f;
            serverWorld.spawnParticles(ParticleTypes.FISHING, d, e, j, 0, l, 0.01, -k, 1.0);
            serverWorld.spawnParticles(ParticleTypes.FISHING, d, e, j, 0, -l, 0.01, k, 1.0);
        }
    }

    private void tickWait(ServerWorld serverWorld, int countdownDecrement){
        float f = 0.05f;
        this.waitCountdown -= countdownDecrement;
        if (this.random.nextFloat() < f) {
            float g = MathHelper.nextFloat(this.random, 0.0f, 360.0f) * ((float)Math.PI / 180);
            float h = MathHelper.nextFloat(this.random, 25.0f, 60.0f);
            double d = this.getX() + (double)(MathHelper.sin(g) * h) * 0.1;
            double e = ((float)MathHelper.floor(this.getY()) + 1.0f);
            double j = this.getZ() + (double)(MathHelper.cos(g) * h) * 0.1;
            BlockState blockState = serverWorld.getBlockState(new BlockPos(d, e - 1.0, j));
            if (blockState.isOf(Blocks.WATER)) {
                serverWorld.spawnParticles(ParticleTypes.SPLASH, d, e, j, 1 + this.random.nextInt(2), 0.1f, 0.0, 0.1f, 0.0);
            }
        }
        if (this.waitCountdown <= 0) {
            this.fishAngle = MathHelper.nextFloat(this.random, 0.0f, 360.0f);
            this.fishTravelCountdown = 1;// MathHelper.nextInt(this.random, 20, 80); Initialize with
        }
    }
    
    private int getCountdownDecrement(BlockPos bobberPos){
        BlockPos aboveBobberPos = bobberPos.up();
        int countdownDecrement = 1;

        //Raining Buff
        if (this.random.nextFloat() < 0.25f && this.world.hasRain(aboveBobberPos)) {
            ++countdownDecrement;
        }

        //Cave Fishing de-buff
        if (this.random.nextFloat() < 0.5f && !this.world.isSkyVisible(aboveBobberPos)) {
            --countdownDecrement;
        }

        return countdownDecrement;
    }

    private void setWaitCountdown() {
        this.waitCountdown = MathHelper.nextInt(this.random, 200, 400);
    }



    private boolean isOpenOrWaterAround(BlockPos blockPos) {
        PositionType positionType = PositionType.INVALID;
        for (int i = -1; i <= 2; ++i) {
            PositionType positionType2 = this.getPositionType(blockPos.add(-2, i, -2), blockPos.add(2, i, 2));
            switch (positionType2) {
                case INVALID: {
                    return false;
                }
                case ABOVE_WATER: {
                    if (positionType != PositionType.INVALID) break;
                    return false;
                }
                case INSIDE_WATER: {
                    if (positionType != PositionType.ABOVE_WATER) break;
                    return false;
                }
            }
            positionType = positionType2;
        }
        return true;
    }


    @Override
    public void onTrackedDataSet(TrackedData<?> trackedData) {
        if (HOOK_ENTITY_ID.equals(trackedData)) {
            int i = this.getDataTracker().get(HOOK_ENTITY_ID);
            Entity entity = this.hookedEntity = i > 0 ? this.world.getEntityById(i - 1) : null;
        }
        if (CAUGHT_FISH.equals(trackedData)) {
            this.caughtFish = this.getDataTracker().get(CAUGHT_FISH);
            if (this.caughtFish) {
                this.setVelocity(this.getVelocity().x, -0.4f * MathHelper.nextFloat(this.velocityRandom, 0.6f, 1.0f), this.getVelocity().z);
            }
        }
        super.onTrackedDataSet(trackedData);
    }

    private PositionType getPositionType(BlockPos blockPos) {
        BlockState blockState = this.world.getBlockState(blockPos);
        if (blockState.isAir() || blockState.isOf(Blocks.LILY_PAD)) {
            return PositionType.ABOVE_WATER;
        }
        FluidState fluidState = blockState.getFluidState();
        if (fluidState.isIn(FluidTags.WATER) && fluidState.isStill() && blockState.getCollisionShape(this.world, blockPos).isEmpty()) {
            return PositionType.INSIDE_WATER;
        }
        return PositionType.INVALID;
    }

    private PositionType getPositionType(BlockPos blockPos, BlockPos blockPos2) {
        return BlockPos.stream(blockPos, blockPos2).map(this::getPositionType).reduce((positionType, positionType2) -> positionType == positionType2 ? positionType : PositionType.INVALID).orElse(PositionType.INVALID);
    }

    private void updateHookedEntityId(@Nullable Entity entity) {
        this.hookedEntity = entity;
        this.getDataTracker().set(HOOK_ENTITY_ID, entity == null ? 0 : entity.getId() + 1);
    }

    private boolean removeIfInvalid(PlayerEntity playerEntity) {
        ItemStack itemStack = playerEntity.getMainHandStack();
        ItemStack itemStack2 = playerEntity.getOffHandStack();
        boolean bl = itemStack.isOf(FishingClub.CUSTOM_FISHING_ROD);
        boolean bl2 = itemStack2.isOf(FishingClub.CUSTOM_FISHING_ROD);
        if (playerEntity.isRemoved() || !playerEntity.isAlive() || !bl && !bl2 || this.squaredDistanceTo(playerEntity) > 1024.0) {
            this.discard();
            return true;
        }
        return false;
    }

    private void checkForCollision() {
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        this.onCollision(hitResult);
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) || entity.isAlive() && entity instanceof ItemEntity;
    }


    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(HOOK_ENTITY_ID, 0);
        this.getDataTracker().startTracking(CAUGHT_FISH, false);
    }

    @Override
    public int use(ItemStack itemStack) {
        PlayerEntity playerEntity = this.getPlayerOwner();
        if (this.world.isClient || playerEntity == null) {
            return 0;
        }
        int i = 0;
        if (this.getHookedEntity() != null) {
            this.pullHookedEntity(this.getHookedEntity());
            Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity)playerEntity, itemStack, this, Collections.emptyList());
            this.world.sendEntityStatus(this, (byte)31);
            i = this.getHookedEntity() instanceof ItemEntity ? 3 : 5;
        } else if ((hookCountdown > 0)){
            ServerPacketSender.sendFishingStartPacket((ServerPlayerEntity) playerEntity, new HashMap<>());
        }
        if (this.onGround) {
            i = 2;
        }
        this.discard();
        return i;
    }

    /* TODO
    *   - buff rain bonus if skill present
    *   - activate mini game instead of normal catch
    *   - adjust for fishing rod catch rate stat
    *   - different texture per bobber part (luxury feature)
    *   - remove "fish coming" indicator
    *   - lengthen "catch" window
    *   - bigger fish pull bobber with greater force
    *   -
    * */


    static enum State {
        FLYING,
        HOOKED_IN_ENTITY,
        BOBBING;

    }

    static enum PositionType {
        ABOVE_WATER,
        INSIDE_WATER,
        INVALID;

    }
}
