package net.semperidem.fishing_club.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.semperidem.fishing_club.fish.FishComponent;
import net.semperidem.fishing_club.fish.FishRecord;
import net.semperidem.fishing_club.fish.FishUtil;
import net.semperidem.fishing_club.registry.FCBlocks;
import net.semperidem.fishing_club.registry.FCComponents;
import net.semperidem.fishing_club.registry.FCRegistry;

public class FishDisplayBlockEntity extends BlockEntity {

    ItemStack fishStack;
    FishEntity fishEntity;
    SoundEvent fishSong;
    boolean isPlaying = false;
    int duration = 0;

    public FishDisplayBlockEntity(BlockPos pos, BlockState state) {
        super(FCBlocks.FISH_DISPLAY, pos, state);
    }


    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return this.createNbt(registryLookup);
    }

    public void stopPlaying() {
        if (!this.isPlaying) {
            return;
        }
        this.isPlaying = false;

        if (!(this.world instanceof ServerWorld serverWorld)) {
            return;
        }
        StopSoundS2CPacket stopSoundS2CPacket = new StopSoundS2CPacket(this.fishSong.getId(), SoundCategory.NEUTRAL);
        //todo Add custom payload to only stop this exact fish from playing
        serverWorld.getPlayers(player -> player.isInRange(fishEntity, 16)).forEach(playerInRange -> {
            playerInRange.networkHandler.sendPacket(stopSoundS2CPacket);
        });
    }

    public void startPlaying() {
        if (this.isPlaying) {
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

    public void drop() {
        if (!(this.world instanceof ServerWorld serverWorld)) {
            return;
        }
        FishComponent displayFishComponent = FishComponent.of(this);
        if (displayFishComponent.record() == null) {
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

    private boolean tryTake(PlayerEntity player, ItemStack stackInHand) {
        if (!stackInHand.isEmpty()) {
            return false;
        }
        if (!player.giveItemStack(this.fishStack)) {
            return false;
        }
        this.stopPlaying();
        FishComponent.of(this).set(null);
        return true;
    }

    private boolean tryPut(PlayerEntity player, ItemStack stackInHand){
        if (!FishUtil.isFish(stackInHand)) {
            return false;
        }
        FishComponent displayFishComponent = FishComponent.of(this);
        if (displayFishComponent.record() != null) {
            return false;
        }
        FishRecord fishRecord = stackInHand.getOrDefault(FCComponents.FISH, FishRecord.DEFAULT);
        if (fishRecord.isEqual(FishRecord.DEFAULT)) {
            return false;
        }
        displayFishComponent.set(fishRecord);
        if (!player.isCreative()) {
            stackInHand.decrement(1);
        }
        return true;
    }

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

    public ItemStack getFishStack() {
        return this.fishStack;
    }

    public FishEntity getFishEntity() {
        return this.fishEntity;
    }

    public int getDuration() {
        return this.duration;
    }


    private void tickDisplayedFish() {
        FishRecord fishRecord = FishComponent.FISH_COMPONENT.get(this).record();
        if (fishRecord == null) {
            this.fishEntity = null;
            return;
        }
        this.fishStack = FishUtil.getStackFromFish(fishRecord);
        this.fishEntity = new FCFishEntity(this.world);
        this.fishEntity.setPosition(this.pos.getX(), this.pos.getY(), this.pos.getZ());
        FishComponent.of(this.fishEntity).set(fishRecord);
        this.fishSong = FCRegistry.SPECIES_TO_TUNE.get(fishRecord.speciesName());
    }

    private void tickPlaying() {
        if (!this.isPlaying) {
            return;
        }
        if (this.duration < 0) {
            this.stopPlaying();
        }
        this.duration--;

    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, FishDisplayBlockEntity fishDisplayBlockEntity) {
        fishDisplayBlockEntity.tickDisplayedFish();
        fishDisplayBlockEntity.tickPlaying();
    }

}
