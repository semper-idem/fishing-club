package net.semperidem.fishingclub.entity;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.semperidem.fishingclub.entity.renderer.CustomFishingBobberEntityRenderer;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.item.fishing_rod.*;
import net.semperidem.fishingclub.item.fishing_rod.components.ComponentItem;
import net.semperidem.fishingclub.item.fishing_rod.components.FishingRodConfiguration;
import net.semperidem.fishingclub.mixin.common.FishingBobberEntityAccessor;
import net.semperidem.fishingclub.network.ServerPacketSender;
import net.semperidem.fishingclub.registry.EntityTypeRegistry;
import net.semperidem.fishingclub.registry.StatusEffectRegistry;
import net.semperidem.fishingclub.screen.fishing_game.FishGameScreenFactory;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

import static net.semperidem.fishingclub.registry.ItemRegistry.MEMBER_FISHING_ROD;


public class CustomFishingBobberEntity extends FishingBobberEntity implements IHookEntity{
    int tick = 0;
    private static final int MIN_WAIT = 600;
    private static final int MIN_HOOK_TICKS = 10;
    private static final int REACTION_REWARD = 20;
    private int outOfOpenWaterTicks;
    private int removalTimer;
    private int hookCountdown;
    private int waitCountdown;
    private int lastWaitCountdown;
    private int fishTravelCountdown;
    private float fishAngle;
    private boolean inOpenWater = true;
    private State state = State.FLYING;
    private ItemStack fishingRod;
    private FishingRodConfiguration configuration;
    private Fish caughtFish;
    private int lastHookCountdown;
    private boolean limitReached = false;
    private boolean distanceRecorded = false;
    private boolean hasSetThrowDirection = false;


    private float power;
    private FishingCard fishingCard;
    private Identifier texture = CustomFishingBobberEntityRenderer.DEFAULT;
    private PlayerEntity playerOwner;


    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        if (packet instanceof BobberEntitySpawnPacketS2C bobberEntitySpawnPacketS2C) {
            this.fishingRod = bobberEntitySpawnPacketS2C.fishingRod;
            if (!fishingRod.isOf(MEMBER_FISHING_ROD)) {
                return;
            }
            this.configuration = MEMBER_FISHING_ROD.getRodConfiguration(fishingRod);
            this.power = 2;//MEMBER_FISHING_ROD.getPower(fishingRod);
            setThrowDirection(this.playerOwner);
            //System.out.println("client velX " + packet.getVelocityX());
            //System.out.println("client velY " + packet.getVelocityY());
            //System.out.println("client velZ " + packet.getVelocityZ());
            //System.out.println(getVelocity());
        }
    }


    @Override
    public void setOwner(Entity entity) {
        super.setOwner(entity);
        if (entity instanceof PlayerEntity playerEntity) {
            this.playerOwner = playerEntity;
        }
    }

    @Override
    public Packet<?> createSpawnPacket() {
        Entity entity = this.getOwner();
        if (entity instanceof  ServerPlayerEntity serverPlayerEntity) {
            ServerPacketSender.sendCardUpdate(serverPlayerEntity, fishingCard);
        }
        return new BobberEntitySpawnPacketS2C(this, entity == null ? this.getId() : entity.getId(), fishingRod);
    }

    public CustomFishingBobberEntity(EntityType<? extends CustomFishingBobberEntity> entityEntityType, World world) {
        super(entityEntityType, world);
        this.playerOwner = MinecraftClient.getInstance().player;
        setTexture(MinecraftClient.getInstance().player);
    }

    public CustomFishingBobberEntity(PlayerEntity owner, World world, FishingRodConfiguration configuration) {
        this(EntityTypeRegistry.CUSTOM_FISHING_BOBBER, world);
        this.setOwner(owner);
        this.configuration = configuration;
        this.fishingRod = configuration.getFishingRod();
        this.fishingCard = FishingCard.getPlayerCard(owner);
        this.power = 2;//MEMBER_FISHING_ROD.getPower(fishingRod);
        setThrowDirection(owner);
        setTexture(this.playerOwner);
    }

    private void setTexture(PlayerEntity playerOwner) {
        if (playerOwner == null) return;
        ItemStack fishingRod = playerOwner.getMainHandStack().getItem() instanceof MemberFishingRodItem ? playerOwner.getMainHandStack() : playerOwner.getOffHandStack();
        String bobberName = String.valueOf(FishingRodPartController.getPart(fishingRod, FishingRodPartType.BOBBER).getItem().toString());
        switch (bobberName) {
            case "bobber_wooden" -> texture = CustomFishingBobberEntityRenderer.WOODEN;
            case "bobber_plant_based" -> texture = CustomFishingBobberEntityRenderer.PLANT;
            case "bobber_ancient" -> texture = CustomFishingBobberEntityRenderer.ANCIENT;
        }
    }

    public Identifier getTexture(){
        return texture;
    }

    private void setThrowDirection(PlayerEntity owner){
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
        vec3d = vec3d.multiply(0.6 / o + 0.5, 0.6 / o + 0.5, 0.6 / o + 0.5);
        vec3d = vec3d.multiply(power);
        this.setVelocity(vec3d);
        this.setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
        this.setPitch((float)(MathHelper.atan2(vec3d.y, vec3d.horizontalLength()) * 57.2957763671875));
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();
        //System.out.println(getVelocity());
        //System.out.println(getYaw());
        //System.out.println(getPitch());
    }


    private void updateHookedEntityId(@Nullable Entity entity) {
        ((FishingBobberEntityAccessor) this).setHookedEntity(entity);
        this.getDataTracker().set(FishingBobberEntityAccessor.getHookedEntityId(), entity == null ? 0 : entity.getId() + 1);
    }


    @Override
    public void tick() {
        tick++;
        //System.out.println(getPos()+ " " + getVelocity() + " " + tick);
        super.tick();
        if (this.playerOwner == null) {
            this.discard();
            return;
        }
        if (!this.world.isClient && this.removeIfInvalid(this.playerOwner)) {
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
        boolean shouldBeBobbing = waterHeight > 0.0f;//isnt this "in water" and not bobbing?
        if (this.state == State.FLYING) {
            if (this.getHookedEntity() != null) {
                this.setVelocity(Vec3d.ZERO);
                this.state = State.HOOKED_IN_ENTITY;
                return;
            }
            if (shouldBeBobbing) {
                //this.setVelocity(this.getVelocity().multiply(0.3, 0.2, 0.3));
                this.state = State.BOBBING;
                return;
            }
            this.checkForCollision();
        } else {
            if (this.state == State.HOOKED_IN_ENTITY) {
                if (getHookedEntity() != null) {
                    if (getHookedEntity().isRemoved() || getHookedEntity().world.getRegistryKey() != this.world.getRegistryKey()) {
                        this.updateHookedEntityId(null);
                        this.state = State.FLYING;
                    } else {
                        this.setPosition(getHookedEntity().getX(), getHookedEntity().getBodyY(0.8), getHookedEntity().getZ());
                    }
                }
                return;
            }
            if (this.state == State.BOBBING) {
                Vec3d vec3d = this.getVelocity();
                if (Math.abs(vec3d.x) + Math.abs(vec3d.z) < 0.005 && !distanceRecorded) {
                    distanceRecorded = true;
                    distanceTraveled = distanceFromCaster();
                    System.out.println("world is client: " + world.isClient + " " + distanceFromCaster());
                    System.out.println("world is client: " + world.isClient + " " + getPos());
                    //this.playerOwner.sendMessage(Text.of("Distance: " + String.format("%.2f", distanceFromCaster())), false);
                }
                double d = this.getY() + vec3d.y - (double)blockPos.getY() - (double)waterHeight;
                if (Math.abs(d) < 0.01) {
                    d += Math.signum(d) * 0.1;
                }
                this.setVelocity(vec3d.x * 0.9, vec3d.y - d * (double)this.random.nextFloat() * 0.2, vec3d.z * 0.9);
                this.inOpenWater = this.hookCountdown <= 0 && this.fishTravelCountdown <= 0 || this.inOpenWater && this.outOfOpenWaterTicks < 10 && this.isOpenOrWaterAround(blockPos);
                if (shouldBeBobbing) {
                    this.outOfOpenWaterTicks = Math.max(0, this.outOfOpenWaterTicks - 1);
                    if (!this.world.isClient) {
                        //this.tickFishingLogic(blockPos);
                    }
                } else {
                    this.outOfOpenWaterTicks = Math.min(10, this.outOfOpenWaterTicks + 1);
                }
            }
        }
        if (!fluidState.isIn(FluidTags.WATER)) {
            this.setVelocity(this.getVelocity().add(0.0, -0.03, 0.0));
        }
        Vec3d beforeMovePos = getSyncedPos();
        this.move(MovementType.SELF, this.getVelocity());
        if (distanceFromCaster() > 999) {
            setPosition(beforeMovePos);
            if (!limitReached) {
                limitReached = true;
                double pullDownForce = (Math.abs(this.getVelocity().getX()) + Math.abs(this.getVelocity().getZ())) * -0.2;
                this.setVelocity(this.getVelocity().getX() * -0.2, pullDownForce, this.getVelocity().getZ() * -0.2);
                this.move(MovementType.SELF, this.getVelocity());
            }
        }
        this.updateRotation();
        if (this.state == State.FLYING && (this.onGround || this.horizontalCollision)) {
            this.setVelocity(Vec3d.ZERO);
        }
        this.setVelocity(this.getVelocity().multiply(0.92));//
        this.refreshPosition();
    }

    private float distanceFromCaster() {
        return distanceTo(this.getOwner());
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
    
    private int getCountdownDecrement(BlockPos bobberPos){
        BlockPos aboveBobberPos = bobberPos.up();
        int countdownDecrement = 1;

//        //Raining Buff
//        if (this.random.nextFloat() < 0.25f && this.world.hasRain(aboveBobberPos)) {
//            ++countdownDecrement;
//        }
//
//        //Cave Fishing de-buff
//        if (this.random.nextFloat() < 0.5f && !this.world.isSkyVisible(aboveBobberPos)) {
//            --countdownDecrement;
//        }

        return countdownDecrement;
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
        if (power > 1) {
            catchRate /= MathHelper.clamp((128 - this.distanceTraveled) / 128 ,0.5, 1);
        }
        int minWait = (int) (MIN_WAIT * catchRate);
        int maxWait = minWait * 2;
        this.waitCountdown = 20;//MathHelper.nextInt(this.random, minWait, maxWait);
        this.lastWaitCountdown = this.waitCountdown;
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
        boolean bl = itemStack.isOf(MEMBER_FISHING_ROD);
        boolean bl2 = itemStack2.isOf(MEMBER_FISHING_ROD);
        if (playerEntity.isRemoved() || !playerEntity.isAlive() || !bl && !bl2) {
            this.discard();
            return true;
        }
        return false;
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
    
    private void reelEntity() {
        this.pullHookedEntity(getHookedEntity());
        Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) this.playerOwner, fishingRod, this, Collections.emptyList());
        this.world.sendEntityStatus(this, EntityStatuses.PULL_HOOKED_ENTITY);
        this.discard();
    }

    private void reelGround() {
        MEMBER_FISHING_ROD.damageComponents(fishingRod, 2, ComponentItem.DamageSource.REEL_GROUND, this.playerOwner);
        this.discard();
    }
    
    private void reelAir() {
        MEMBER_FISHING_ROD.damageComponents(fishingRod, 2, ComponentItem.DamageSource.CAST, this.playerOwner);
        this.discard();
    }
    
    private void reelWater() {
        MEMBER_FISHING_ROD.damageComponents(fishingRod, 2, ComponentItem.DamageSource.REEL_WATER, this.playerOwner);
        this.discard();
    }
    
    @Override
    public int use(ItemStack itemStack) {
        if (this.world.isClient) {
            return 0;
        }
        if (this.playerOwner == null) {
            this.discard();
            return 0;
        }
        if (getHookedEntity() != null) {
            reelEntity();
            return 0;
        }
        if (this.hookCountdown > 0){
            reelFish();
            return 0;
        }
        if (this.onGround) {
            reelGround();
            return 0;
        }
        if (this.world.getFluidState(getBlockPos()).getHeight() > 0) {
            reelWater();
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

    FishingRodPartItem getBobber(){
        return (FishingRodPartItem) FishingRodPartController.getPart(fishingRod, FishingRodPartType.BOBBER).getItem().asItem();
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

    @Override
    protected void pullHookedEntity(Entity entity) {
        Entity entity2 = this.getOwner();
        if (entity2 == null) {
            return;
        }
        Vec3d vec3d = new Vec3d(entity2.getX() - this.getX(), entity2.getY() - this.getY(), entity2.getZ() - this.getZ()).multiply(0.1f * configuration.getCastPower());
        entity.setVelocity(entity.getVelocity().add(vec3d));
        MEMBER_FISHING_ROD.damageComponents(fishingRod, 2, ComponentItem.DamageSource.REEL_FISH, this.playerOwner);
    }


    enum State {
        FLYING,
        HOOKED_IN_ENTITY,
        BOBBING;

    }

    enum PositionType {
        ABOVE_WATER,
        INSIDE_WATER,
        INVALID;

    }
}
