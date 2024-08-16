package net.semperidem.fishingclub.entity;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fish.specimen.SpecimenDisplayComponent;
import net.semperidem.fishingclub.network.payload.StopPlayingPayload;
import net.semperidem.fishingclub.registry.FCBlocks;
import net.semperidem.fishingclub.registry.FCComponents;

public class FishDisplayBlockEntity extends BlockEntity {

    private ItemStack fishStack;
    private FishEntity fishEntity;
    private SoundEvent fishSong;
    private boolean isPlaying = false;
    private int duration = 0;

    public boolean use(PlayerEntity player, ItemStack stackInHand) {

        if (player.isSneaking() && this.tryTake(player, stackInHand)) {
            return true;
        }

        if (tryPut(player, stackInHand)) {
            return true;
        }

        if (this.fishSong != null) {
            this.startPlaying();
            return true;
        }

        return false;
    }

    private boolean tryPut(PlayerEntity player, ItemStack stackInHand){

        if (!FishUtil.isFish(stackInHand)) {
            return false;
        }

        SpecimenDisplayComponent displayFishComponent = SpecimenDisplayComponent.of(this);
        if (displayFishComponent.get() != null) {
            return false;
        }

        SpecimenData fishRecord = stackInHand.getOrDefault(FCComponents.SPECIMEN, SpecimenData.DEFAULT);
        if (fishRecord.isEqual(SpecimenData.DEFAULT)) {
            return false;
        }

        displayFishComponent.set(fishRecord);
        this.markDirty();
        if (!player.isCreative()) {
            stackInHand.decrement(1);
        }
        return true;
    }

    private boolean tryTake(PlayerEntity player, ItemStack stackInHand) {

        if (!stackInHand.isEmpty()) {
            return false;
        }

        if (this.fishStack == null) {
            return false;
        }

        if (!player.giveItemStack(this.fishStack)) {
            return false;
        }

        this.stopPlaying();
        SpecimenDisplayComponent.of(this).set(null);
        return true;
    }

    public void drop() {

        if (!(this.world instanceof ServerWorld serverWorld)) {
            return;
        }

        SpecimenDisplayComponent displayFishComponent = SpecimenDisplayComponent.of(this);
        if (displayFishComponent.get() == null) {
            return;
        }

        serverWorld.spawnEntity(
          new ItemEntity(
            serverWorld,
            pos.getX() + 0.5,
            pos.getY(),
            pos.getZ() + 0.5,
            this.fishStack
          )
        );

        this.fishEntity.discard();
        this.fishStack = null;
    }
    public FishEntity getFishEntity() {

        return this.fishEntity;
    }

    public int getDuration() {

        return this.duration;
    }


    public void startPlaying() {

        if (this.isPlaying) {
            this.stopPlaying();
            return;
        }

        if (this.fishSong == null) {
            return;
        }

        if (this.fishEntity == null) {
            return;
        }

        this.duration = 1200;
        this.isPlaying = true;

        if (!(this.world instanceof ServerWorld)) {
            return;
        }

        this.fishEntity.playSound(
          this.fishSong,
          1,1
        );
    }

    public void stopPlaying() {

        if (!this.isPlaying) {
            return;
        }
        this.isPlaying = false;

        if (!(this.world instanceof ServerWorld serverWorld)) {
            return;
        }

        StopPlayingPayload stopPlayingPayload = new StopPlayingPayload(
          this.fishSong.getId().toString(),
          this.fishEntity.getX(),
          this.fishEntity.getY(),
          this.fishEntity.getZ()
        );

        serverWorld.getPlayers(player -> player.isInRange(fishEntity, 16)).forEach(playerInRange -> {
            ServerPlayNetworking.send(playerInRange, stopPlayingPayload);
        });
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, FishDisplayBlockEntity fishDisplayBlockEntity) {

        fishDisplayBlockEntity.tickDisplayedFish();
        fishDisplayBlockEntity.tickPlaying();
    }

    private void tickDisplayedFish() {
//
//        FishRecord fishRecord = FishComponent.of(this).get();
//
//        if (fishRecord == null) {
//            this.fishEntity = null;
//            this.fishStack = null;
//            this.fishSong = null;
//            return;
//        }
//        if (this.fishEntity != null) {
//            return;
//        }
//
//        this.fishStack = FishUtil.getStackFromFish(fishRecord);
//        this.fishEntity = new AbstractFishEntity(this.world);
//        this.fishEntity.setPosition(this.pos.getX(), this.pos.getY(), this.pos.getZ());
//        FishComponent.of(this.fishEntity).set(fishRecord);
//        this.fishSong = FCRegistry.SPECIES_TO_TUNE.get(fishRecord.speciesName());
    }

    private void tickPlaying() {

        if (!this.isPlaying) {
            return;
        }

        this.duration--;
        if (this.duration <= 0) {
            this.stopPlaying();
        }

    }

    public FishDisplayBlockEntity(BlockPos pos, BlockState state) {super(FCBlocks.FISH_DISPLAY, pos, state);}
    @Override protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {super.readNbt(nbt, registryLookup);}
    @Override protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {super.writeNbt(nbt, registryLookup);}
    @Override public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {return this.createNbt(registryLookup);}
}
