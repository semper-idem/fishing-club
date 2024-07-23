package net.semperidem.fishing_club.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.passive.FishEntity;
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
import net.semperidem.fishing_club.registry.FCBlocks;
import net.semperidem.fishing_club.registry.FCRegistry;

public class FishDisplayBlockEntity extends BlockEntity {
    FishRecord displayedFish;
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
        if (this.fishSong == null) {
            return;
        }
        if (!this.isPlaying) {
            return;
        }
        this.isPlaying = false;

        if (!(this.world instanceof ServerWorld serverWorld)) {
            return;
        }
        StopSoundS2CPacket stopSoundS2CPacket = new StopSoundS2CPacket(this.fishSong.getId(), SoundCategory.NEUTRAL);
        serverWorld.getPlayers(player -> player.isInRange(fishEntity, 16)).forEach(playerInRange -> {
            playerInRange.networkHandler.sendPacket(stopSoundS2CPacket);
        });
    }

    public void startPlaying() {
        if (this.fishSong == null) {
            return;
        }
        if (this.world == null) {
            return;
        }
        if (this.isPlaying) {
            return;
        }
        this.duration = 1200;
        this.isPlaying = true;
        if (this.world.isClient()) {
            return;
        }
        this.fishEntity.playSound(
          this.fishSong,
          1,1
        );
    }

    public FishEntity getFishEntity() {
        return this.fishEntity;
    }

    public int getDuration() {
        return this.duration;
    }


    private void init(FishRecord fishRecord) {
        if (this.displayedFish == fishRecord) {
            return;
        }
        this.displayedFish = fishRecord;
        if (fishRecord == null) {
            this.fishEntity = null;
            return;
        }
        this.fishEntity = new FCFishEntity(this.world);
        this.fishEntity.setPosition(this.pos.getX(), this.pos.getY(), this.pos.getZ());
        FishComponent.of(this.fishEntity).set(fishRecord);
        this.fishSong = FCRegistry.SPECIES_TO_TUNE.get(this.displayedFish.speciesName());
    }


    public static void tick(World world, BlockPos blockPos, BlockState blockState, FishDisplayBlockEntity fishDisplayBlockEntity) {
        fishDisplayBlockEntity.init(FishComponent.FISH_COMPONENT.get(fishDisplayBlockEntity).record());
        if (!fishDisplayBlockEntity.isPlaying) {
            return;
        }
        if (fishDisplayBlockEntity.duration < 0) {
            fishDisplayBlockEntity.stopPlaying();
        }
        fishDisplayBlockEntity.duration--;
    }

}
