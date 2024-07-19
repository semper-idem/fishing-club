package net.semperidem.fishing_club.entity;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.semperidem.fishing_club.fish.FishComponent;
import net.semperidem.fishing_club.fish.FishUtil;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.fisher.perks.FishingPerks;
import net.semperidem.fishing_club.item.fishing_rod.components.BobberPartItem;
import net.semperidem.fishing_club.item.fishing_rod.components.HookPartItem;
import net.semperidem.fishing_club.item.fishing_rod.components.PartItem;
import net.semperidem.fishing_club.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishing_club.mixin.common.FishingBobberEntityAccessor;
import net.semperidem.fishing_club.registry.FCComponents;
import net.semperidem.fishing_club.registry.FCEntityTypes;
import net.semperidem.fishing_club.registry.FCStatusEffects;
import net.semperidem.fishing_club.screen.fishing_game.FishingGameScreenHandlerFactory;
import net.semperidem.fishing_club.util.Util;
import net.semperidem.fishing_club.util.VelocityUtil;

import java.util.Collections;

import static net.semperidem.fishing_club.registry.FCItems.MEMBER_FISHING_ROD;


public class HookEntity extends FishingBobberEntity implements IHookEntity {
    private static final int MIN_WAIT = 600;
    private static final int MIN_HOOK_TICKS = 10;
    private static final int REACTION_REWARD = 20;

    private int hookCountdown;
    private int waitCountdown;
    private int lastWaitCountdown;
    private int fishTravelCountdown;
    private float fishAngle;
    private int lastHookCountdown;

    private boolean isInFluid = false;

    private float weightRatio = 1;
    private int maxEntityMagnitude = 0;

    private float castCharge = 0.25f;
    private float lineLength;

    private Vec3d airResistance = new Vec3d(1, 1, 1);
    private Vec3d waterResistance = this.airResistance;

    private Vec3d ownerVector = Vec3d.ZERO;
    private BlockState stickToBlockState;
    private BlockPos stickToBlockPos;

    private PlayerEntity playerOwner;
    private FishingCard fishingCard;
    private ItemStack fishingRod;
    private RodConfiguration configuration;
    private HookPartItem hookPartItem;
    private Entity hookedEntity;//For some reason hookedEntity from FishingBobberEntity likes to set itself to null

    private FishComponent caughtFish;

    public HookEntity(EntityType<? extends HookEntity> entityEntityType, World world) {
        super(entityEntityType, world);

    }

    public HookEntity(PlayerEntity owner, World world, ItemStack fishingRod) {
        this(FCEntityTypes.HOOK_ENTITY, world);
        this.setOwner(owner);
        this.init(fishingRod);
    }

    public float getLineLength() {
        return this.lineLength;
    }

    public void setLineLength(int amount) {
        this.lineLength = amount;
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        init(this.playerOwner.getMainHandStack());
    }

    public void init(ItemStack fishingRod) {
        if (!fishingRod.isOf(MEMBER_FISHING_ROD)) {
            this.discard();
            return;
        }
        this.fishingRod = fishingRod;
        this.fishingCard = FishingCard.of(this.playerOwner);
        this.castCharge = this.fishingRod.getOrDefault(FCComponents.CAST_POWER, 1f);
        this.configuration = RodConfiguration.of(this.fishingRod);
        this.configuration.hook().ifPresent(hookStack -> {
            if (hookStack.getItem() instanceof HookPartItem aHookPartItem) {
                this.hookPartItem = aHookPartItem;
            }
        });
        this.maxEntityMagnitude = this.configuration.attributes().weightMagnitude();
        this.lineLength = this.fishingRod.getOrDefault(FCComponents.LINE_LENGTH, this.configuration.attributes().maxLineLength());
        this.calculateResistance();
        this.setCastAngle();
        this.updateHookEntity(this);
    }

    private void calculateResistance() {
        this.airResistance = new Vec3d(0.95, 0.99, 0.95);
        this.waterResistance = new Vec3d(0.85, 0.6, 0.85);
    }

    private void updateHookEntity(Entity hookedEntity) {
        this.hookedEntity = hookedEntity;
        ((FishingBobberEntityAccessor) this).invokeUpdateHookedEntityId(hookedEntity);
        this.weightRatio = (float) (1 + EntityWeights.getEntityMagnitude(hookedEntity.getType()) * 0.067f);
        if (this.hookedEntity == this) {
            return;
        }
        if (this.hookPartItem == null) {
            return;
        }
        if (this.hookPartItem.getDamage() == 0) {
            return;
        }
        this.hookedEntity.damage(this.getDamageSources().playerAttack(this.playerOwner), this.hookPartItem.getDamage());
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        if (this.hookedEntity != this) {
            return;
        }

        if (this.hookPartItem == null) {
            return;
        }

        if (!this.hookPartItem.isSticky()) {
            return;
        }

        this.stickToBlockPos = blockHitResult.getBlockPos();
        this.stickToBlockState = this.getWorld().getBlockState(this.stickToBlockPos);
        this.setPosition(blockHitResult.getPos().add(this.ownerVector.normalize().multiply(-0.02)));
        this.setVelocity(Vec3d.ZERO);
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
        float a = MathHelper.cos(-playerYaw * ((float) Math.PI / 180) - (float) Math.PI);
        float b = MathHelper.sin(-playerYaw * ((float) Math.PI / 180) - (float) Math.PI);
        float c = -MathHelper.cos(-playerPitch * ((float) Math.PI / 180));
        float d = MathHelper.sin(-playerPitch * ((float) Math.PI / 180));
        this.refreshPositionAndAngles(
                this.playerOwner.getX() - (double) b * 0.3,
                this.playerOwner.getEyeY(),
                this.playerOwner.getZ() - (double) a * 0.3,
                playerYaw,
                playerPitch
        );
        return new Vec3d(-b, MathHelper.clamp(-(d / c), -5.0f, 5.0f), -a);
    }

    @SuppressWarnings("all")
    private void setCastAngle() {
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
        this.setYaw((float) (MathHelper.atan2(castAngle.x, castAngle.z) * 57.3));
        this.setPitch((float) (MathHelper.atan2(castAngle.y, castAngle.horizontalLength()) * 57.3));
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();
    }

    @Override
    public void tick() {
        if (this.playerOwner == null) {
            return;
        }
        super.tick();
        if ((!this.getWorld().isClient && this.isOwnerValid(this.playerOwner))) {
            this.discard();
            return;
        }
        this.isInFluid = this.getWorld().getFluidState(this.getBlockPos()).isIn(FluidTags.WATER) || this.getWorld().getFluidState(this.getBlockPos()).isIn(FluidTags.LAVA);
        this.tickEntityCollision();
        this.tickFishingLogic();
        this.tickMotion();
    }


    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (this.hookPartItem != null  && entityHitResult.getEntity() != this && this.hookPartItem.getDamage() > 0) {
        }
        this.updateHookEntity(entityHitResult.getEntity());
        if (this.hookedEntity == this || this.hookedEntity == null) {
            return;
        }
        if (this.hookPartItem == null) {
            return;
        }

        if (this.hookedEntity instanceof LivingEntity livingHookedEntity) {
            this.hookPartItem.onEntityHit(livingHookedEntity);
        }

        if (this.hookPartItem.getDamage() > 0) {
            entityHitResult.getEntity().damage(this.getDamageSources().playerAttack(this.playerOwner), this.hookPartItem.getDamage());
        }
    }

    private boolean isHookedEntityInvalid() {
        return this.hookedEntity != null && (this.hookedEntity.isRemoved() || this.hookedEntity.getWorld().getRegistryKey() != this.getWorld().getRegistryKey());
    }

    private void tickEntityCollision() {
        if (this.hookedEntity == this) {
            this.checkForCollision();
        }
        if (this.isHookedEntityInvalid()) {
            this.updateHookEntity(this);

        }
        if (this.hookedEntity != null && this.hookedEntity != this) {
            this.setVelocity(Vec3d.ZERO);
            this.setPosition(
                    this.hookedEntity.getX(),
                    this.hookedEntity.getBodyY(0.75),
                    this.hookedEntity.getZ()
            );
        }
    }

    private void tickMotion() {
        this.tickStickBlock();
        this.tickBuoyancy();
        this.tickGravity();
        this.move(MovementType.SELF, this.getVelocity());
        this.tickMotionResistance();
        this.tickTension();
    }

    private void tickStickBlock() {

        if (this.stickToBlockPos == null) {
            return;
        }

        if (this.stickToBlockState == this.getWorld().getBlockState(this.stickToBlockPos)) {
            return;
        }

        this.stickToBlockPos = null;
        this.stickToBlockState = null;
    }

    private void tickBuoyancy() {

        if (this.stickToBlockPos != null) {
            return;
        }

        if (this.isInFluid) {
            double buoyancy = 0.03 + this.random.nextGaussian() * 0.005 + Math.sin(Util.getEntityDepth(this)) * -0.1f;
            VelocityUtil.addVelocityY(this, buoyancy);
        }
    }

    private void tickGravity() {

        if (this.stickToBlockPos != null) {
            return;
        }

        VelocityUtil.addVelocityY(this, this.isOnGround() ? 0 : -0.03);
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
        if (this.isOnGround()) {
            float slipperiness = this.getSteppingBlockState().getBlock().getSlipperiness();
            return new Vec3d(slipperiness, slipperiness, slipperiness);
        }
        return this.isInFluid ? this.waterResistance : this.airResistance;
    }

    private void tickTensionWear(double tension) {
        if (this.getWorld().isClient) {
            return;//For some reason server tension is always lower then clients
        }
        if (tension <= 1 || this.maxEntityMagnitude < this.weightRatio) {
            return;
        }
        this.configuration.damage(10, PartItem.DamageSource.REEL_ENTITY, this.playerOwner, this.fishingRod);
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
        return MathHelper.clamp(getRawTension(), 0, 1.5);
    }

    private double getRawTension() {
        return this.ownerVector.length() - this.lineLength * (2 - this.weightRatio);
    }

    private void applyTension(double tension) {

        if (this.stickToBlockPos != null) {
            return;
        }

        VelocityUtil.addVelocity(
                this.hookedEntity,
                this.ownerVector.normalize().multiply(tension * -1f * this.weightRatio)
        );
    }

    public void applyTensionFromOwner(Vec3d tensionVector) {

        if (this.stickToBlockPos != null) {
            return;
        }

        VelocityUtil.addVelocity(
                this.hookedEntity,
                tensionVector.multiply(-0.5f * this.weightRatio)
        );
    }

    private void tickFishingLogic() {
        if (!this.isValidFishing()) {
            return;
        }
        this.playerOwner.sendMessage(Text.of("Distance: " + String.format("%.2f", this.ownerVector.length())), true);
        ServerWorld serverWorld = (ServerWorld) this.getWorld();

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
        return this.hookedEntity == this && this.isInFluid && !this.getWorld().isClient && this.getVelocity().horizontalLength() < 0.05f;
    }

    private boolean tickAutoHookedFish() {

        if (this.hookPartItem == null) {
            return false;
        }

        if (this.hookPartItem.getAutoHookChance() < Math.random()) {
            return false;
        }


        reelFish();
        return true;
    }

    private void tickHookedFish() {

        --this.hookCountdown;

        if (this.hookCountdown > 0) {
            return;
        }

        this.waitCountdown = 0;
        this.fishTravelCountdown = 0;
        this.damageRod(4, PartItem.DamageSource.BITE);

        if (tickAutoHookedFish()) {
            return;
        }
        this.caughtFish = null;

    }

    private void tickFish(ServerWorld serverWorld) {
        this.fishTravelCountdown -= 1;
        if (this.fishTravelCountdown > 0) {
            this.tickFishReeling(serverWorld);
        }
        this.handleFishOnHook(serverWorld);
    }

    private void handleFishOnHook(ServerWorld serverWorld) {
        float fishTypeRarityMultiplier = 1;
        if (this.configuration.bobber().isPresent() && this.configuration.bobber().get().getItem() instanceof BobberPartItem bobberPartItem) {
            bobberPartItem.onFishBiteEffect();
        }
        if (Math.random() < configuration.attributes().baitFailChance()) {
            return;
        }
        if (fishingCard.hasPerk(FishingPerks.BOBBER_THROW_CHARGE)) {
            fishTypeRarityMultiplier += MathHelper.clamp(this.distanceTraveled / 64, 0, 1);
        }
        ItemStack fishingRodCopy = fishingRod.copy();
        ItemStack sharedBait = fishingCard.getSharedBait();
        if (sharedBait != ItemStack.EMPTY) {
            // FishingRodPartController.putPart(fishingRodCopy, sharedBait);
            fishingRod = fishingRodCopy;
        }
        caughtFish = FishUtil.getFishOnHook(this);
        this.getVelocity().add(0, -0.03 * caughtFish.quality() * caughtFish.quality(), 0);
        this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 2f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.4f);
        double m = this.getY() + 0.5;
        serverWorld.spawnParticles(ParticleTypes.FIREWORK, this.getX(), m, this.getZ(), (int) (1.0f + this.getWidth() * 20.0f), this.getWidth(), 0.0, this.getWidth(), 0.2f);
        serverWorld.spawnParticles(ParticleTypes.BUBBLE, this.getX(), m, this.getZ(), (int) (1.0f + this.getWidth() * 20.0f), this.getWidth(), 0.0, this.getWidth(), 0.2f);
        serverWorld.spawnParticles(ParticleTypes.FISHING, this.getX(), m, this.getZ(), (int) (1.0f + this.getWidth() * 20.0f), this.getWidth(), 0.0, this.getWidth(), 0.2f);
        //(From 20 To 45) * Multiplier
        this.hookCountdown = 45;//(int) (( (25 - (caughtFish.level / 4f + this.random.nextInt(1))) + MIN_HOOK_TICKS) * Math.max(1, FishingRodPartController.getStat(fishingRod, FishingRodStatType.BITE_WINDOW_MULTIPLIER)));
        this.lastHookCountdown = hookCountdown;

        if (sharedBait != ItemStack.EMPTY) {
            int damage = sharedBait.getDamage();
            sharedBait.setDamage(damage + 1);
            if (damage + 1 > sharedBait.getMaxDamage()) {
                fishingCard.setSharedBait(ItemStack.EMPTY);//todo fix ugly
            }
//        } else if (FishingRodPartController.hasBait(fishingRod)) {
//            MEMBER_FISHING_ROD.damageComponents(fishingRod, 2, PartItem.DamageSource.BITE, this.playerOwner);
        }

    }

    private void tickFishReeling(ServerWorld serverWorld) {
        this.fishAngle += (float) this.random.nextTriangular(0.0, 9.188);
        float f = this.fishAngle * ((float) Math.PI / 180);
        float g = MathHelper.sin(f);
        float h = MathHelper.cos(f);

        double j = this.getZ() + (double) (h * (float) this.fishTravelCountdown * 0.1f);
        double e = ((float) MathHelper.floor(this.getY()) + 1.0f);
        double d = this.getX() + (double) (g * (float) this.fishTravelCountdown * 0.1f);
        BlockState blockState = serverWorld.getBlockState(new BlockPos((int) d, (int) (e - 1.0), (int) j));
        if (blockState.isOf(Blocks.WATER) || blockState.isOf(Blocks.LAVA)) {
            if (this.random.nextFloat() < 0.15f) {
                serverWorld.spawnParticles(ParticleTypes.BUBBLE, d, e - (double) 0.1f, j, 1, g, 0.1, h, 0.0);
            }
            float k = g * 0.04f;
            float l = h * 0.04f;
            serverWorld.spawnParticles(ParticleTypes.FISHING, d, e, j, 0, l, 0.01, -k, 1.0);
            serverWorld.spawnParticles(ParticleTypes.FISHING, d, e, j, 0, -l, 0.01, k, 1.0);
        }
    }

    private void tickWait(ServerWorld serverWorld) {
        this.waitCountdown -= 1;
        splashWater(serverWorld);
        handleWaitCountdown();
    }

    private void handleWaitCountdown() {
        if (this.waitCountdown <= 0) {
            this.fishAngle = MathHelper.nextFloat(this.random, 0.0f, 360.0f);
            //TODO Initialize with value with skill
            this.fishTravelCountdown = 1;
        }
    }

    private void splashWater(ServerWorld serverWorld) {
        if (this.random.nextFloat() < 0.05f) {
            float g = MathHelper.nextFloat(this.random, 0.0f, 360.0f) * ((float) Math.PI / 180);
            float h = MathHelper.nextFloat(this.random, 25.0f, 60.0f);
            double d = this.getX() + (double) (MathHelper.sin(g) * h) * 0.1;
            double e = ((float) MathHelper.floor(this.getY()) + 1.0f);
            double j = this.getZ() + (double) (MathHelper.cos(g) * h) * 0.1;
            BlockState blockState = serverWorld.getBlockState(new BlockPos((int) d, (int) (e - 1.0), (int) j));
            if (blockState.isOf(Blocks.WATER) || blockState.isOf(Blocks.LAVA)) {
                serverWorld.spawnParticles(ParticleTypes.SPLASH, d, e, j, 1 + this.random.nextInt(2), 0.1f, 0.0, 0.1f, 0.0);
            }
        }
    }

    private void setWaitCountdown() {
        float catchRate;
        float catchRateReduction = 0;// FishingRodPartController.getStat(fishingRod, FishingRodStatType.CATCH_RATE);
        if (this.getWorld().isRaining()) {
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

        if (this.playerOwner.hasStatusEffect(FCStatusEffects.FREQUENCY_BUFF)) {
            catchRateReduction += 0.1f;
        }
        catchRate = Math.max(.33f, (1 - catchRateReduction));
        if (castCharge > 1) {
            catchRate /= MathHelper.clamp((128 - this.distanceTraveled) / 128, 0.5, 1);
        }
        int minWait = (int) (MIN_WAIT * catchRate);
        int maxWait = minWait * 2;
        this.waitCountdown = 20;//MathHelper.nextInt(this.random, minWait, maxWait);
        this.lastWaitCountdown = this.waitCountdown;
    }

    private boolean isOwnerValid(PlayerEntity playerEntity) {
        return playerEntity == null || playerEntity.isRemoved() || !playerEntity.isAlive() || MEMBER_FISHING_ROD.hasNoFishingRod(playerEntity);
    }

    private boolean hasHookEntity() {
        return this.hookedEntity != null && this.hookedEntity != this;
    }

    @Override
    public int use(ItemStack fishingRod) {//Return value is damage to fishingRod
        if (this.playerOwner == null) {
            this.discard();
            return 0;
        }
        if (this.hasHookEntity()) {
            this.reelEntity();
            return 0;
        }
        if (this.hookCountdown > 0) {
            this.reelFish();
            return 0;
        }
        if (this.getWorld().getFluidState(getBlockPos()).getHeight() > 0) {
            this.reelWater();
            return 0;
        }
        if (this.isOnGround() || getVelocity().y == 0) {
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
        this.getWorld().sendEntityStatus(this, EntityStatuses.PULL_HOOKED_ENTITY);
        this.discard();
    }

    private Vec3d getPullVector() {
        return this.ownerVector.normalize().multiply(configuration.attributes().castPower() * getTension());
    }

    private Vec3d getPullVectorNT() {
        return this.ownerVector.normalize().multiply(configuration.attributes().castPower());
    }

    private void pullEntity() {
        if (this.hookPartItem != null && this.hookPartItem.getReelDamage() > 0) {
            this.hookedEntity.damage(this.getDamageSources().playerAttack(this.playerOwner), this.hookPartItem.getReelDamage());
        }
        if (this.hookPartItem != null && this.hookPartItem.isSharp()) {
            return;
        }
        VelocityUtil.addVelocity(this.hookedEntity, this.getPullVectorNT().multiply(-1));
        this.damageRod(2, PartItem.DamageSource.REEL_ENTITY);
        if (!(this.playerOwner instanceof ServerPlayerEntity)) {
            return;
        }
        Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) this.playerOwner, fishingRod, this, Collections.emptyList());
    }


    private void reelFish() {
//        int reactionBonus = calculateReactionBonus();
//        this.caughtFish.experience += reactionBonus;
//        if (reactionBonus > 0) {
//            this.playerOwner.sendMessage(Text.of("[Quick Hands Bonus] +" + reactionBonus + " to fish exp (if caught)"));
//        }
        this.damageRod(2, PartItem.DamageSource.REEL_FISH);
        this.playerOwner.openHandledScreen(new FishingGameScreenHandlerFactory(this.caughtFish, this.configuration));
        this.discard();
    }

    private void reelWater() {
        this.damageRod(2, PartItem.DamageSource.REEL_WATER);
        this.discard();
    }

    private int calculateReactionBonus() {
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
        this.damageRod(2, PartItem.DamageSource.REEL_GROUND);
        this.discard();
    }

    private void pullOwner() {
        VelocityUtil.addVelocity(this.playerOwner, this.getPullVector().multiply(0.5f));
    }

    private void damageRod(int amount, PartItem.DamageSource damageSource) {
        this.configuration.damage(amount, damageSource, this.playerOwner, this.fishingRod);
    }

    @Override
    public FishingCard getFishingCard() {
        return FishingCard.of(this.playerOwner);
    }

    @Override
    public RodConfiguration getCaughtUsing() {
        return this.configuration;
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
    public int getWaitTime() {
        return this.lastWaitCountdown;
    }
}
