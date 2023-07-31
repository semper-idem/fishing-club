package net.semperidem.fishingclub.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.semperidem.fishingclub.client.game.FishGameLogic;
import net.semperidem.fishingclub.client.game.fish.Fish;
import net.semperidem.fishingclub.client.game.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.fisher.FishingPerks;
import net.semperidem.fishingclub.item.FishingRodPartItem;
import net.semperidem.fishingclub.network.ServerPacketSender;
import net.semperidem.fishingclub.registry.FEntityRegistry;
import net.semperidem.fishingclub.registry.FItemRegistry;
import net.semperidem.fishingclub.util.FishingRodUtil;


public class CustomFishingBobberEntity extends FishingBobberEntity {
    private static final int MIN_WAIT = 20;
    private static final int MIN_HOOK_TICKS = 10;
    private static final int REACTION_REWARD = 20;
    private final Random velocityRandom = Random.create();
    private int outOfOpenWaterTicks;
    private int removalTimer;
    private int hookCountdown;
    private int waitCountdown;
    private int fishTravelCountdown;
    private float fishAngle;
    private boolean inOpenWater = true;
    private State state = State.FLYING;
    private ItemStack fishingRod;
    private Fish caughtFish;
    private int lastHookCountdown;


    private int power;
    private float throwDistance;
    private boolean boatFishing;
    private FisherInfo fisherInfo;



    public CustomFishingBobberEntity(EntityType<? extends CustomFishingBobberEntity> entityEntityType, World world) {
        super(entityEntityType, world);
    }

    public CustomFishingBobberEntity(PlayerEntity owner, World world, ItemStack fishingRod, int power, FisherInfo fisherInfo, boolean boatFishing) {
        this(FEntityRegistry.CUSTOM_FISHING_BOBBER, world);
        this.setOwner(owner);
        this.fishingRod = fishingRod;
        this.fisherInfo = fisherInfo;
        this.power = power;
        this.boatFishing = boatFishing;
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
        vec3d = vec3d.multiply(power);
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
            if (this.removalTimer >= 600) {
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
        if (this.state == State.FLYING && isBobbing) {
            this.setVelocity(this.getVelocity().multiply(0.3, 0.2, 0.3));
            this.state = State.BOBBING;
            this.throwDistance = distanceTo(this.getOwner());
            if (this.world.isClient) {
                MinecraftClient.getInstance().player.sendMessage(Text.of("Distance: " + String.format("%.2f", this.throwDistance)), true);
            }
            return;
        } else {
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
            this.caughtFish = null;
        }
    }

    private void tickFish(ServerWorld serverWorld, int countdownDecrement){
        this.fishTravelCountdown -= countdownDecrement;
        if (this.fishTravelCountdown > 0) {
            tickFishReeling(serverWorld);
        } else {
            handleFishOnHook(serverWorld);
        }
    }

    private void handleFishOnHook(ServerWorld serverWorld){
        float fishTypeRarityMultiplier = 1;
        if (fisherInfo.hasPerk(FishingPerks.BOBBER_THROW_CHARGE)) {
            fishTypeRarityMultiplier += MathHelper.clamp(this.distanceTraveled / 320, 0, 0.2);
        }
        caughtFish = FishUtil.getFishOnHook(fisherInfo, fishingRod, fishTypeRarityMultiplier);
        this.getVelocity().add(0,-0.03 * caughtFish.grade * caughtFish.grade,0);
        this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 2f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.4f);
        double m = this.getY() + 0.5;
        serverWorld.spawnParticles(ParticleTypes.FIREWORK, this.getX(), m, this.getZ(), (int)(1.0f + this.getWidth() * 20.0f), this.getWidth(), 0.0, this.getWidth(), 0.2f);
        serverWorld.spawnParticles(ParticleTypes.BUBBLE, this.getX(), m, this.getZ(), (int)(1.0f + this.getWidth() * 20.0f), this.getWidth(), 0.0, this.getWidth(), 0.2f);
        serverWorld.spawnParticles(ParticleTypes.FISHING, this.getX(), m, this.getZ(), (int)(1.0f + this.getWidth() * 20.0f), this.getWidth(), 0.0, this.getWidth(), 0.2f);
        //(From 20 To 45) * Multiplier
        this.hookCountdown = (int) (( (25 - (caughtFish.fishLevel / 4f + this.random.nextInt(1))) + MIN_HOOK_TICKS) * Math.max(1, FishingRodUtil.getStat(fishingRod, FishGameLogic.Stat.BITE_WINDOW_MULTIPLIER)));
        this.lastHookCountdown = hookCountdown;
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
            float k = g * 0.04f;
            float l = h * 0.04f;
            serverWorld.spawnParticles(ParticleTypes.FISHING, d, e, j, 0, l, 0.01, -k, 1.0);
            serverWorld.spawnParticles(ParticleTypes.FISHING, d, e, j, 0, -l, 0.01, k, 1.0);
        }
    }

    private void tickWait(ServerWorld serverWorld, int countdownDecrement){
        this.waitCountdown -= countdownDecrement;
        splashWater(serverWorld);
        handleWaitCountdown();
    }

    private void handleWaitCountdown(){
        if (this.waitCountdown <= 0) {
            this.fishAngle = MathHelper.nextFloat(this.random, 0.0f, 360.0f);
            //TODO Initialize with value with skill
            this.fishTravelCountdown = 1;
        }
    }

    private void splashWater(ServerWorld serverWorld){
        if (this.random.nextFloat() < 0.05f) {
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
        float catchRate = FishingRodUtil.getStat(fishingRod, FishGameLogic.Stat.CATCH_RATE);
        int minWait = (int) (MIN_WAIT * Math.max(0,(1 - catchRate)));
        int maxWait = minWait * 2;
        this.waitCountdown = MathHelper.nextInt(this.random, minWait, maxWait);
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

    private boolean removeIfInvalid(PlayerEntity playerEntity) {
        ItemStack itemStack = playerEntity.getMainHandStack();
        ItemStack itemStack2 = playerEntity.getOffHandStack();
        boolean bl = itemStack.isOf(FItemRegistry.CUSTOM_FISHING_ROD);
        boolean bl2 = itemStack2.isOf(FItemRegistry.CUSTOM_FISHING_ROD);
        if (playerEntity.isRemoved() || !playerEntity.isAlive() || !bl && !bl2) {
            this.discard();
            return true;
        }
        return false;
    }

    @Override
    public int use(ItemStack itemStack) {
        PlayerEntity playerEntity = this.getPlayerOwner();
        if (this.world.isClient || playerEntity == null) {
            return 0;
        }
        int i = 0;
        if ((hookCountdown > 0)){
            int reactionBonus = calculateReactionBonus();
            caughtFish.experience += reactionBonus;
            this.getOwner().sendMessage(Text.of("[Quick Hands Bonus] +" + reactionBonus + " to fish exp (if caught)"));
            ServerPacketSender.sendFishingStartPacket((ServerPlayerEntity) playerEntity, fishingRod, caughtFish, boatFishing);
        }
        if (this.onGround) {
            i = 2;
        }
        this.discard();
        return i;
    }

    /* TODO
    *   - hide "fish coming" indicator behind skill
    *   - buff rain bonus if skill present
    * */

    private int calculateReactionBonus(){
        int reaction = this.lastHookCountdown - this.hookCountdown;
        if (reaction < REACTION_REWARD) {
            return Math.max(5, REACTION_REWARD - reaction);
        }
        return 0;
    }

    FishingRodPartItem getBobber(){
        return (FishingRodPartItem) FishingRodUtil.getRodPart(fishingRod, FishingRodPartItem.PartType.BOBBER).getItem().asItem();
    }

    enum State {
        FLYING,
        BOBBING;

    }

    enum PositionType {
        ABOVE_WATER,
        INSIDE_WATER,
        INVALID;

    }
}
