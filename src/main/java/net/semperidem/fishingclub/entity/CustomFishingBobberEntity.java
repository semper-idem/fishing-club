package net.semperidem.fishingclub.entity;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.noise.NoiseHelper;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.item.fishing_rod.*;
import net.semperidem.fishingclub.item.fishing_rod.components.ComponentItem;
import net.semperidem.fishingclub.item.fishing_rod.components.FishingRodConfiguration;
import net.semperidem.fishingclub.mixin.common.FishingBobberEntityAccessor;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.network.ServerPacketSender;
import net.semperidem.fishingclub.registry.EntityTypeRegistry;
import net.semperidem.fishingclub.registry.StatusEffectRegistry;
import net.semperidem.fishingclub.screen.fishing_game.FishGameScreenFactory;

import java.util.Collections;

import static net.semperidem.fishingclub.registry.ItemRegistry.MEMBER_FISHING_ROD;


public class CustomFishingBobberEntity extends FishingBobberEntity implements IHookEntity{
    private static final int MIN_WAIT = 600;
    private static final int MIN_HOOK_TICKS = 10;
    private static final int REACTION_REWARD = 20;

    private int hookCountdown;
    private int waitCountdown;
    private int lastWaitCountdown;
    private int fishTravelCountdown;
    private float fishAngle;
    private int lastHookCountdown;
    private boolean isInWater;

    private float castCharge = 1;
    private Vec3d airResistance;
    private Vec3d waterResistance;
    private int maxEntityMagnitude;
    private float maxLineLength = 8;
    public float lineLength = 8;
    private Vec3d ownerVector;

    private FishingCard fishingCard;
    public ItemStack fishingRod;
    private FishingRodConfiguration configuration;
    private PlayerEntity playerOwner;

    private Fish caughtFish;
    public float weightRatio;
    Entity hookEntity = this;
    public CustomFishingBobberEntity(EntityType<? extends CustomFishingBobberEntity> entityEntityType, World world) {
        super(entityEntityType, world);
    }

    public CustomFishingBobberEntity(PlayerEntity owner, World world, FishingRodConfiguration configuration) {
        this(EntityTypeRegistry.CUSTOM_FISHING_BOBBER, world);
        this.setOwner(owner);
        this.init(configuration);
    }

    public void scrollLine(float amount) {
        this.lineLength = MathHelper.clamp(lineLength + amount, 4, maxLineLength);
        if (this.world.isClient) {
            this.playerOwner.sendMessage(Text.literal(String.format("Line length: %.2f", lineLength)), true);
        }
    }

    public void initClient(ItemStack fishingRod) {
        this.init(MEMBER_FISHING_ROD.getRodConfiguration(fishingRod));
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        ClientPacketSender.requestFishingRod(packet.getId());
    }

    private void init(FishingRodConfiguration configuration) {
        this.fishingCard = FishingCard.getPlayerCard(this.playerOwner);
        this.configuration = configuration;
        this.fishingRod = configuration.getFishingRod();
        this.maxLineLength = configuration.getMaxLineLength();
        this.maxEntityMagnitude = configuration.getWeightMagnitude();
        this.lineLength = MEMBER_FISHING_ROD.getLineLength(this.fishingRod);
        this.castCharge = MEMBER_FISHING_ROD.getCastCharge(this.fishingRod);
        this.calculateResistance();
        this.setCastDirection();
        this.updateHookEntity(this);
    }


    private void updateHookEntity(Entity hookEntity) {
        this.hookEntity = hookEntity;
        this.weightRatio = (float) (1 + EntityWeights.getEntityMagnitude(hookEntity.getType()) * 0.067f);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        Entity entity = this.getOwner();
        if (entity instanceof  ServerPlayerEntity serverPlayerEntity) {
            ServerPacketSender.sendCardUpdate(serverPlayerEntity, fishingCard);
        }
        return super.createSpawnPacket();
    }

    @Override
    public void setOwner(Entity entity) {
        super.setOwner(entity);
        if (entity instanceof PlayerEntity playerEntity) {
            this.playerOwner = playerEntity;
        }
    }

    private Vec3d getCastAngle() {
        float playerPitch = this.playerOwner.getPitch();
        float playerYaw = this.playerOwner.getYaw();
        float a = MathHelper.cos(-playerYaw * ((float)Math.PI / 180) - (float)Math.PI);
        float b = MathHelper.sin(-playerYaw * ((float)Math.PI / 180) - (float)Math.PI);
        float c = -MathHelper.cos(-playerPitch * ((float)Math.PI / 180));
        float d = MathHelper.sin(-playerPitch * ((float)Math.PI / 180));
        this.refreshPositionAndAngles(
                this.playerOwner.getX() - (double)b * 0.3,
                this.playerOwner.getEyeY(),
                this.playerOwner.getZ() - (double)a * 0.3,
                playerYaw,
                playerPitch
        );
        return new Vec3d(-b, MathHelper.clamp(-(d / c), -5.0f, 5.0f), -a);
    }

    private void setCastDirection(){
        Vec3d vec3d = getCastAngle();
        double o = vec3d.length();
        vec3d = vec3d.multiply(
                0.6 / o + this.random.nextTriangular(0.5, 0.01),
                0.6 / o + this.random.nextTriangular(0.5, 0.01),
                0.6 / o + this.random.nextTriangular(0.5, 0.01)
        );
        vec3d = vec3d.multiply(castCharge);
        this.setVelocity(vec3d);
        this.setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.3));
        this.setPitch((float)(MathHelper.atan2(vec3d.y, vec3d.horizontalLength()) * 57.3));
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();
    }

    @Override
    public void tick() {
        super.tick();
        if ((!this.world.isClient && this.isInvalid(this.playerOwner))) {
            this.discard();
            return;
        }
        this.isInWater = this.world.getFluidState(this.getBlockPos()).isIn(FluidTags.WATER);
        tickEntityCollision();
        tickFishingLogic();
        tickMotion();
        tickTension();
    }

    private boolean isHookedEntityInvalid() {
        return this.getHookedEntity() != null && (this.getHookedEntity().isRemoved() || this.getHookedEntity().world.getRegistryKey() != this.world.getRegistryKey());
    }

    private void tickEntityCollision() {
        if (this.getHookedEntity() == null) {
            this.checkForCollision();
        }
        if (this.isHookedEntityInvalid()) {
            ((FishingBobberEntityAccessor)this).invokeUpdateHookedEntityId(null);
            this.updateHookEntity(this);

        }
        if (this.getHookedEntity() != null){
            this.updateHookEntity(this.getHookedEntity());
            this.setVelocity(Vec3d.ZERO);
            this.setPosition(this.getHookedEntity().getX(), this.getHookedEntity().getBodyY(0.8), this.getHookedEntity().getZ());
        }
    }

    private boolean isValidFishing() {
        return this.getHookedEntity() == null && this.isInWater && !this.world.isClient && this.getVelocity().length() < 0.01f;
    }

    private void calculateResistance(){
        this.airResistance = new Vec3d(0.95, 0.99, 0.95);
        this.waterResistance = new Vec3d(0.85, 0.6, 0.85);
    }

    private void tickMotion() {
        if (isInWater) {
            double nextYPos = this.getPos().y + this.getVelocity().y;
            int nextYBlockPos = (int) Math.floor(nextYPos);
            BlockPos pos = new BlockPos(this.getBlockX(), nextYPos, this.getBlockZ());
            FluidState fluidState = this.world.getFluidState(pos);
            FluidState fluidAbove = this.world.getFluidState(pos.up());
            float waterLevel = (fluidAbove.isOf(fluidState.getFluid()) ? 1 : fluidState.getHeight());
            float waterLevelAbove = fluidAbove.getHeight();
            double up = (nextYPos - nextYBlockPos) - waterLevel - waterLevelAbove;
            double upForce = MathHelper.clamp(up, -2, 0);
            this.addVelocity(0, 0.03 + random.nextGaussian() * 0.005 + Math.sin(upForce) * -0.1f, 0);
        }
        this.addVelocity(0, this.onGround ? 0.0f : -0.03f, 0);
        this.move(MovementType.SELF, this.getVelocity());
        Vec3d resistance = getResistance();
        this.setVelocity(this.getVelocity().multiply(resistance));
    }

    private float getTension() {
        return (float) MathHelper.clamp(this.ownerVector.length()- this.lineLength * (2 - weightRatio), 0 ,2);

    }

    private void tickTension() {
        this.ownerVector = this.playerOwner.getEyePos().relativize(this.getPos());
        float tension = getTension();
        if (tension < 0) {
            return;
        }
        calculateTensionVelocity(MathHelper.clamp(tension, 0, 1f));

        if (tension > 1 && this.maxEntityMagnitude >= this.weightRatio) {
            MEMBER_FISHING_ROD.damageComponents(fishingRod, 10, ComponentItem.DamageSource.REEL_ENTITY, this.playerOwner);
            this.discard();
        }
    }


    public void applyTension(Vec3d tensionVector) {
        Vec3d weighedTension = tensionVector.multiply(weightRatio * 0.5f);
        this.hookEntity.addVelocity(weighedTension.x, weighedTension.y, weighedTension.z);
    }

    private void calculateTensionVelocity(float force) {
        Vec3d tensionVector = this.ownerVector.normalize().multiply(force * -1f * this.weightRatio);
        this.hookEntity.addVelocity(tensionVector.x, tensionVector.y, tensionVector.z);
    }

    private Vec3d getResistance() {
        if (this.onGround) {
            float slipperiness = this.getSteppingBlockState().getBlock().getSlipperiness();
            return new Vec3d(slipperiness, slipperiness, slipperiness);
        }
        return this.isInWater ? this.waterResistance : this.airResistance;
    }

    @Override
    public void move(MovementType movementType, Vec3d movement) {
        super.move(movementType, movement);
        this.updateRotation();
        this.refreshPosition();
    }


    private void tickFishingLogic() {
        if (!isValidFishing()) {
            return;
        }
        this.playerOwner.sendMessage(Text.of("Distance: " + String.format("%.2f", this.ownerVector.length())), true);
        ServerWorld serverWorld = (ServerWorld)this.world;

        if (this.hookCountdown > 0) {
            tickHookedFish();
        } else if (this.fishTravelCountdown > 0) {
            tickFish(serverWorld, 1);
        } else if (this.waitCountdown > 0) {
            tickWait(serverWorld, 1);
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
            MEMBER_FISHING_ROD.damageComponents(fishingRod, 4, ComponentItem.DamageSource.BITE, this.playerOwner);
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
        if (fishingCard.hasPerk(FishingPerks.BOBBER_THROW_CHARGE)) {
            fishTypeRarityMultiplier += MathHelper.clamp(this.distanceTraveled / 64, 0, 1);
        }
        ItemStack fishingRodCopy = fishingRod.copy();
        ItemStack sharedBait = fishingCard.getSharedBait();
        if (sharedBait != ItemStack.EMPTY) {
            FishingRodPartController.putPart(fishingRodCopy, sharedBait);
            fishingRod = fishingRodCopy;
        }
        caughtFish = FishUtil.getFishOnHook(this);
        this.getVelocity().add(0,-0.03 * caughtFish.grade * caughtFish.grade,0);
        this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 2f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.4f);
        double m = this.getY() + 0.5;
        serverWorld.spawnParticles(ParticleTypes.FIREWORK, this.getX(), m, this.getZ(), (int)(1.0f + this.getWidth() * 20.0f), this.getWidth(), 0.0, this.getWidth(), 0.2f);
        serverWorld.spawnParticles(ParticleTypes.BUBBLE, this.getX(), m, this.getZ(), (int)(1.0f + this.getWidth() * 20.0f), this.getWidth(), 0.0, this.getWidth(), 0.2f);
        serverWorld.spawnParticles(ParticleTypes.FISHING, this.getX(), m, this.getZ(), (int)(1.0f + this.getWidth() * 20.0f), this.getWidth(), 0.0, this.getWidth(), 0.2f);
        //(From 20 To 45) * Multiplier
        this.hookCountdown = (int) (( (25 - (caughtFish.level / 4f + this.random.nextInt(1))) + MIN_HOOK_TICKS) * Math.max(1, FishingRodPartController.getStat(fishingRod, FishingRodStatType.BITE_WINDOW_MULTIPLIER)));
        this.lastHookCountdown = hookCountdown;

        if (sharedBait != ItemStack.EMPTY) {
            int damage = sharedBait.getDamage();
            sharedBait.setDamage(damage + 1);
            if (damage + 1 > sharedBait.getMaxDamage()) {
                fishingCard.setSharedBait(ItemStack.EMPTY);//todo fix ugly
            }
        } else if (FishingRodPartController.hasBait(fishingRod)) {
            MEMBER_FISHING_ROD.damageComponents(fishingRod, 2, ComponentItem.DamageSource.BITE, this.playerOwner);
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

    private void setWaitCountdown() {
        float catchRate;
        float catchRateReduction = FishingRodPartController.getStat(fishingRod, FishingRodStatType.CATCH_RATE);
        if (world.isRaining()) {
            float rainBonus = 0.125f;
            if (fishingCard.hasPerk(FishingPerks.RAINY_FISH)) {
                rainBonus *= 2;
            }
            if (fishingCard.hasPerk(FishingPerks.RAINY_FISH_PLUS)) {
                rainBonus *= 2;
            }
            catchRateReduction += rainBonus;
        }
        if (FishUtil.hasFishingHat(this.playerOwner)) {
            catchRateReduction += 0.15f;
            if (FishUtil.hasProperFishingEquipment(this.playerOwner)) {
                catchRateReduction += 0.15f;
            }
        }

        if (this.playerOwner.hasStatusEffect(StatusEffectRegistry.FREQUENCY_BUFF)) {
            catchRateReduction += 0.1f;
        }
        catchRate = Math.max(.33f,(1 - catchRateReduction));
        if (castCharge > 1) {
            catchRate /= MathHelper.clamp((128 - this.distanceTraveled) / 128 ,0.5, 1);
        }
        int minWait = (int) (MIN_WAIT * catchRate);
        int maxWait = minWait * 2;
        this.waitCountdown = 20;//MathHelper.nextInt(this.random, minWait, maxWait);
        this.lastWaitCountdown = this.waitCountdown;
    }

    private boolean isInvalid(PlayerEntity playerEntity) {
        return playerEntity == null || playerEntity.isRemoved() || !playerEntity.isAlive() || !MEMBER_FISHING_ROD.holdsFishingRod(playerEntity);
    }
    
    private void reelFish() {
        int reactionBonus = calculateReactionBonus();
        caughtFish.experience += reactionBonus;
        if (reactionBonus > 0) {
            this.getOwner().sendMessage(Text.of("[Quick Hands Bonus] +" + reactionBonus + " to fish exp (if caught)"));
        }
        MEMBER_FISHING_ROD.damageComponents(fishingRod, 2, ComponentItem.DamageSource.REEL_FISH, this.playerOwner);
        if(this.playerOwner.world != null) {
            this.playerOwner.openHandledScreen(new FishGameScreenFactory(fishingCard, caughtFish));
        }
        this.discard();
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        super.onBlockCollision(state);
    }

    private void reelEntity() {
        if (this.hookEntity != this.playerOwner) {
            Vec3d pullVec = this.ownerVector.normalize().multiply(-configuration.getCastPower() * getTension());
            this.hookEntity.addVelocity(pullVec.x, pullVec.y, pullVec.z);
            MEMBER_FISHING_ROD.damageComponents(fishingRod, 2, ComponentItem.DamageSource.REEL_FISH, this.playerOwner);
            if (this.playerOwner instanceof ServerPlayerEntity)
                Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) this.playerOwner, fishingRod, this, Collections.emptyList());{
            }
        }
        //Should also impact player
        this.world.sendEntityStatus(this, EntityStatuses.PULL_HOOKED_ENTITY);
        this.discard();
    }

    private void reelGround() {
        Vec3d pullVec = this.ownerVector.normalize().multiply(configuration.getCastPower() * getTension());
        this.playerOwner.addVelocity(pullVec.x, pullVec.y, pullVec.z);
        MEMBER_FISHING_ROD.damageComponents(fishingRod, 2, ComponentItem.DamageSource.REEL_GROUND, this.playerOwner);
        this.discard();
    }

    private void reelWater() {
        MEMBER_FISHING_ROD.damageComponents(fishingRod, 2, ComponentItem.DamageSource.REEL_WATER, this.playerOwner);
        this.discard();
    }
    
    @Override
    public int use(ItemStack itemStack) {
        if (this.playerOwner == null) {
            this.discard();
            return 0;
        }
        if (this.hookCountdown > 0){
            reelFish();
            return 0;
        }


        if (this.world.getFluidState(getBlockPos()).getHeight() > 0) {
            reelWater();
            return 0;
        }
        if (this.getHookedEntity() != null) {
            reelEntity();
            return 0;
        }
        if (this.onGround) {
            reelGround();
            return 0;
        }
        reelAir();
        return 0;
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

    @Override
    public FishingCard getFishingCard() {
        return FishingCard.getPlayerCard(this.playerOwner);
    }

    @Override
    public ItemStack getCaughtUsing() {
        return fishingRod;
    }

    @Override
    public ChunkPos getFishedInChunk() {
        return getChunkPos();
    }

    @Override
    public float getFishMethodDebuff() {
        return 1;
    }

    @Override
    public float getWaitTime() {
        return this.lastWaitCountdown;
    }
    private void reelAir() {
        MEMBER_FISHING_ROD.damageComponents(fishingRod, 2, ComponentItem.DamageSource.CAST, this.playerOwner);
        this.discard();
    }
}
