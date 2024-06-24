package net.semperidem.fishingclub.entity;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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
import net.semperidem.fishingclub.util.Util;
import net.semperidem.fishingclub.util.VelocityUtil;

import java.util.Collections;

import static net.semperidem.fishingclub.registry.ItemRegistry.MEMBER_FISHING_ROD;


public class HookEntity extends FishingBobberEntity implements IHookEntity{
    private static final int MIN_WAIT = 600;
    private static final int MIN_HOOK_TICKS = 10;
    private static final int REACTION_REWARD = 20;

    private int hookCountdown;
    private int waitCountdown;
    private int lastWaitCountdown;
    private int fishTravelCountdown;
    private float fishAngle;
    private int lastHookCountdown;

    private boolean isInWater = false;

    private float weightRatio = 1;
    private int maxEntityMagnitude = 0;

    private float castCharge = 1;
    private float maxLineLength = 8;
    private float lineLength = this.maxLineLength;

    private Vec3d airResistance = new Vec3d(1,1,1);
    private Vec3d waterResistance = this.airResistance;

    private Vec3d ownerVector;

    private PlayerEntity playerOwner;
    private FishingCard fishingCard;
    private ItemStack fishingRod;
    private FishingRodConfiguration configuration;
    private Entity hookedEntity;//For some reason hookedEntity from FishingBobberEntity likes to set itself to null

    private Fish caughtFish;

    public HookEntity(EntityType<? extends HookEntity> entityEntityType, World world) {
        super(entityEntityType, world);
    }

    public HookEntity(PlayerEntity owner, World world, FishingRodConfiguration configuration) {
        this(EntityTypeRegistry.HOOK_ENTITY, world);
        this.setOwner(owner);
        this.init(configuration);
    }

    public ItemStack getFishingRod(){
        return this.fishingRod;
    }

    public float getLineLength(){
        return this.lineLength;
    }

    public void scrollLine(float amount) {
        this.lineLength = MathHelper.clamp(lineLength + amount, 4, maxLineLength);
        if (this.world.isClient) {
            this.playerOwner.sendMessage(Text.literal(String.format("Line length: %.2f", lineLength)), true);
        }
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        ClientPacketSender.requestFishingRod(packet.getId());
    }

    public void initClient(ItemStack fishingRod) {
        this.init(MEMBER_FISHING_ROD.getRodConfiguration(fishingRod));
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
        this.setCastAngle();
        this.updateHookEntity(this);
    }

    private void calculateResistance(){
        this.airResistance = new Vec3d(0.95, 0.99, 0.95);
        this.waterResistance = new Vec3d(0.85, 0.6, 0.85);
    }

    private void updateHookEntity(Entity hookedEntity) {
        this.hookedEntity = hookedEntity;
        ((FishingBobberEntityAccessor)this).invokeUpdateHookedEntityId(hookedEntity);
        this.weightRatio = (float) (1 + EntityWeights.getEntityMagnitude(hookedEntity.getType()) * 0.067f);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        if (this.getOwner() instanceof  ServerPlayerEntity serverPlayerEntity) {
            ServerPacketSender.sendCardUpdate(serverPlayerEntity, this.fishingCard);
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

    @SuppressWarnings("all")
    private void setCastAngle(){
        Vec3d castAngle = getCastAngle();
        double o = castAngle.length();
        double precision = MathHelper.clamp(
                0.1 - this.fishingCard.getLevel() * 0.001f,
                0.0,
                0.1
        );
        castAngle = castAngle.multiply(
                0.6 / o + this.random.nextTriangular(0.5, precision),
                0.6 / o + this.random.nextTriangular(0.5, precision),
                0.6 / o + this.random.nextTriangular(0.5, precision)
        ).multiply(castCharge);

        this.setVelocity(castAngle);
        this.setYaw((float)(MathHelper.atan2(castAngle.x, castAngle.z) * 57.3));
        this.setPitch((float)(MathHelper.atan2(castAngle.y, castAngle.horizontalLength()) * 57.3));
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();
    }

    @Override
    public void tick() {
        super.tick();
        if ((!this.world.isClient && this.isOwnerValid(this.playerOwner))) {
            this.discard();
            return;
        }
        this.isInWater = this.world.getFluidState(this.getBlockPos()).isIn(FluidTags.WATER);
        this.tickEntityCollision();
        this.tickFishingLogic();
        this.tickMotion();
    }


    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        this.updateHookEntity(entityHitResult.getEntity());
    }

    private boolean isHookedEntityInvalid() {
        return this.hookedEntity != null && (this.hookedEntity.isRemoved() || this.hookedEntity.world.getRegistryKey() != this.world.getRegistryKey());
    }

    private void tickEntityCollision() {
        if (this.hookedEntity == this) {
            this.checkForCollision();
        }
        if (this.isHookedEntityInvalid()) {
            this.updateHookEntity(this);

        }
        if (this.hookedEntity != null && this.hookedEntity != this){
            this.setVelocity(Vec3d.ZERO);
            this.setPosition(
                    this.hookedEntity.getX(),
                    this.hookedEntity.getBodyY(0.75),
                    this.hookedEntity.getZ()
            );
        }
    }

    private void tickMotion() {
        this.tickBuoyancy();
        this.tickGravity();
        this.move(MovementType.SELF, this.getVelocity());
        this.tickMotionResistance();
        this.tickTension();
    }

    private void tickBuoyancy() {
        if (this.isInWater) {
            double buoyancy = 0.03 + this.random.nextGaussian() * 0.005 + Math.sin(Util.getEntityDepth(this)) * -0.1f;
             VelocityUtil.addVelocityY(this, buoyancy);
        }
    }

    private void tickGravity() {
        VelocityUtil.addVelocityY(this, this.onGround ? 0 : -0.03);
    }

    @Override
    public void move(MovementType movementType, Vec3d movement) {
        super.move(movementType, movement);
        this.updateRotation();
        this.refreshPosition();
    }

    private void tickMotionResistance() {
        VelocityUtil.multiplyVelocity(this, getResistance());
    }

    private Vec3d getResistance() {
        if (this.onGround) {
            float slipperiness = this.getSteppingBlockState().getBlock().getSlipperiness();
            return new Vec3d(slipperiness, slipperiness, slipperiness);
        }
        return this.isInWater ? this.waterResistance : this.airResistance;
    }

    private void tickTensionWear(double tension) {
         if (tension <= 1 || this.maxEntityMagnitude < this.weightRatio) {
            return;
        }
          if (this.world.isClient) {
            return;//For some reason server tension is always lower then clients
        }
        MEMBER_FISHING_ROD.damageComponents(this.fishingRod, 10, ComponentItem.DamageSource.REEL_ENTITY, this.playerOwner);
        this.discard();
    }

    private void tickTension() {
        this.ownerVector = this.playerOwner.getEyePos().relativize(this.getPos());
        double tension = getTension();
        if (tension <= 0) {
            return;
        }
        this.applyTension(MathHelper.clamp(tension, 0, 1));
        this.tickTensionWear(tension);
    }

    private double getTension() {
        return MathHelper.clamp(getRawTension(), 0 , 2);
    }

    private double getRawTension() {
        return this.ownerVector.length() - this.lineLength * (2 - this.weightRatio);
    }

    private void applyTension(double tension) {
        VelocityUtil.addVelocity(
                this.hookedEntity,
                this.ownerVector.normalize().multiply(tension * -1f * this.weightRatio)
        );
    }

    public void applyTensionFromOwner(Vec3d tensionVector) {
        VelocityUtil.addVelocity(
                this.hookedEntity,
                tensionVector.multiply( -0.5f * this.weightRatio)
        );
    }

    private void tickFishingLogic() {
        if (!this.isValidFishing()) {
            return;
        }
        this.playerOwner.sendMessage(Text.of("Distance: " + String.format("%.2f", this.ownerVector.length())), true);
        ServerWorld serverWorld = (ServerWorld)this.world;

        if (this.hookCountdown > 0) {
            this.tickHookedFish();
            return;
        }
        if (this.fishTravelCountdown > 0) {
            this.tickFish(serverWorld);
            return;
        }
        if (this.waitCountdown > 0) {
            this.tickWait(serverWorld);
            return;
        }
        this.setWaitCountdown();
    }

    private boolean isValidFishing() {
        return this.hookedEntity == null && this.isInWater && !this.world.isClient && this.getVelocity().horizontalLength() < 0.05f;
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

    private void tickFish(ServerWorld serverWorld){
        this.fishTravelCountdown -= 1;
        if (this.fishTravelCountdown > 0) {
            this.tickFishReeling(serverWorld);
        } else {
            this.handleFishOnHook(serverWorld);
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

    private void tickWait(ServerWorld serverWorld){
        this.waitCountdown -= 1;
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

    private boolean isOwnerValid(PlayerEntity playerEntity) {
        return playerEntity == null || playerEntity.isRemoved() || !playerEntity.isAlive() || !MEMBER_FISHING_ROD.holdsFishingRod(playerEntity);
    }

    @Override
    public int use(ItemStack fishingRod) {//Return value is damage to fishingRod
        if (this.playerOwner == null) {
            this.discard();
            return 0;
        }
        if (this.hookedEntity != null) {
            this.reelEntity();
            return 0;
        }
        if (this.hookCountdown > 0){
            this.reelFish();
            return 0;
        }
        if (this.world.getFluidState(getBlockPos()).getHeight() > 0) {
            this.reelWater();
            return 0;
        }
        if (this.onGround) {
            this.reelGround();
            return 0;
        }
        this.discard();
        return 0;
    }

    private void reelEntity() {
        if (this.hookedEntity != this.playerOwner) {
            pullEntity();
        }
        this.world.sendEntityStatus(this, EntityStatuses.PULL_HOOKED_ENTITY);
        this.discard();
    }

    private Vec3d getPullVector() {
        return this.ownerVector.normalize().multiply(-configuration.getCastPower() * getTension());
    }

    @SuppressWarnings("all")//For this implementation hookedEntity is never null
    private void pullEntity() {
        VelocityUtil.addVelocity(this.hookedEntity, this.getPullVector().multiply(-1));
        MEMBER_FISHING_ROD.damageComponents(fishingRod, 2, ComponentItem.DamageSource.REEL_FISH, this.playerOwner);
        if (!(this.playerOwner instanceof ServerPlayerEntity)) {
            return;
        }
        Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) this.playerOwner, fishingRod, this, Collections.emptyList());
    }


    private void reelFish() {
        int reactionBonus = calculateReactionBonus();
        this.caughtFish.experience += reactionBonus;
        if (reactionBonus > 0) {
            this.getOwner().sendMessage(Text.of("[Quick Hands Bonus] +" + reactionBonus + " to fish exp (if caught)"));
        }
        MEMBER_FISHING_ROD.damageComponents(fishingRod, 2, ComponentItem.DamageSource.REEL_FISH, this.playerOwner);
        if(this.playerOwner.world != null) {
            this.playerOwner.openHandledScreen(new FishGameScreenFactory(fishingCard, caughtFish));
        }
        this.discard();
    }

    private void reelWater() {
        MEMBER_FISHING_ROD.damageComponents(fishingRod, 2, ComponentItem.DamageSource.REEL_WATER, this.playerOwner);
        this.discard();
    }

    private int calculateReactionBonus(){
        int reaction = this.lastHookCountdown - this.hookCountdown;
        if (reaction < REACTION_REWARD) {
            return Math.max(5, REACTION_REWARD - reaction);
        }
        return 0;
    }

    private void reelGround() {
        if (this.hookedEntity != this.playerOwner) {
            this.pullOwner();
        }
        MEMBER_FISHING_ROD.damageComponents(this.fishingRod, 2, ComponentItem.DamageSource.REEL_GROUND, this.playerOwner);
        this.discard();
    }

    private void pullOwner() {
        VelocityUtil.addVelocity(this.playerOwner, this.getPullVector());
    }


    @Override
    public FishingCard getFishingCard() {
        return FishingCard.getPlayerCard(this.playerOwner);
    }

    @Override
    public ItemStack getCaughtUsing() {
        return this.fishingRod;
    }

    @Override
    public ChunkPos getFishedInChunk() {
        return this.getChunkPos();
    }

    @Override
    public float getFishMethodDebuff() {
        return 1f;
    }

    @Override
    public float getWaitTime() {
        return this.lastWaitCountdown;
    }
}
