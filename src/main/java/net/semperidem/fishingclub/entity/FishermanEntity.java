package net.semperidem.fishingclub.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
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
import net.semperidem.fishingclub.FishingServerWorld;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.registry.EntityTypeRegistry;
import net.semperidem.fishingclub.screen.dialog.DialogKey;
import net.semperidem.fishingclub.screen.dialog.DialogScreenHandler;
import net.semperidem.fishingclub.screen.dialog.DialogScreenHandlerFactory;
import net.semperidem.fishingclub.screen.dialog.DialogUtil;
import net.semperidem.fishingclub.screen.member.MemberScreenHandler;
import net.semperidem.fishingclub.util.EffectUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;


public class FishermanEntity extends PassiveEntity {
    private static final double TWO_PI = Math.PI * 2;
    private static final double QUARTER_PI = Math.PI * 0.25f;
    private static final double SIXTEENTH_PI = Math.PI * 0.06125f;
	private float paddlePhases;
    private PlayerEntity customer;
    private SummonType summonType = SummonType.SPELL;
    private ItemStack spawnedFrom = ItemStack.EMPTY;
    private final ArrayList<UUID> talkedTo = new ArrayList<>();
    private UUID summonerUUID;
    private final static int DESPAWN_TIME = 6000;
    private int despawnTimer;

    public FishermanEntity(World world) {
        super(EntityTypeRegistry.DEREK_ENTITY, world);
        this.setCustomName(Text.of("Derek ol'Stinker"));
        this.intersectionChecked = true;
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        this.despawnTimer = DESPAWN_TIME;
    }
    public FishermanEntity(World world, ItemStack spawnedFrom, UUID summonerUUID) {
        this(world);
        setSummonDetails(spawnedFrom, summonerUUID);
    }

    private void setSummonDetails(ItemStack spawnedFrom, UUID summonerUUID) {
        if (summonerUUID == null) {
            return;
        }
        this.summonerUUID = summonerUUID;

        if (spawnedFrom.isEmpty()) {
            return;
        }
        this.summonType = spawnedFrom.isOf(FishUtil.FISH_ITEM) ? SummonType.GRADE : SummonType.GOLDEN;
        this.spawnedFrom = spawnedFrom;
    }

    protected void initGoals() {
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1));
        this.goalSelector.add(2, new WanderAroundGoal(this, 0.35));
        this.goalSelector.add(4, new GoToWalkTargetGoal(this, 0.35));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 0.35));
        this.goalSelector.add(9, new StopAndLookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
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

    @Override
    public boolean hasVehicle() {
        return isSubmergedInWater();
    }

    protected SoundEvent getPaddleSoundEvent() {
        return SoundEvents.ENTITY_BOAT_PADDLE_WATER;
    }

    public void acceptTrade() {
        if (this.getWorld().isClient()) {
            return;
        }
        if (spawnedFrom.isEmpty()) {
            return;
        }
        FishingCard.of(this.getWorld().getPlayerByUuid(summonerUUID)).giveDerekFish();
        spawnedFrom = ItemStack.EMPTY;
    }

    public void refuseTrade() {
        if (this.getWorld().isClient()) {
            return;
        }
        if (spawnedFrom.isEmpty()) {
            return;
        }


        dropStack(spawnedFrom);
        spawnedFrom = ItemStack.EMPTY;
        this.despawnTimer = 0;
    }

    @Override
    public void tickMovement() {
        if (customer != null) {
            if (!this.getWorld().isClient) {
                if (!(customer.currentScreenHandler instanceof DialogScreenHandler) && !(customer.currentScreenHandler instanceof MemberScreenHandler)) {
                    customer = null;
                }
            }
            return;
        }
        super.tickMovement();
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
           return;
        }
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
        tickDespawnTimer();
        tickPaddles();
        tickBuoyancy();
        super.tick();
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
        if (this.getWorld() instanceof FishingServerWorld serverWorld){
            serverWorld.setDerek(this);
        }
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
        if(!this.getWorld().isClient && customer == null) {
            HashSet<DialogKey> keySet = DialogUtil.getKeys(playerEntity, this);
            FishingCard.of(playerEntity).meetDerek(summonType);
            talkedTo.add(playerEntity.getUuid());
            playerEntity.openHandledScreen(new DialogScreenHandlerFactory(keySet, this));
            setCustomer(playerEntity);
        }
        return ActionResult.CONSUME;
    }

    public void setCustomer(@Nullable PlayerEntity customer) {
        this.customer = customer;
    }

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
        GRADE,
        SPELL
    }
}
