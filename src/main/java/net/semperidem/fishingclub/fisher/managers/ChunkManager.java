package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.ChunkPos;
import net.semperidem.fishingclub.fisher.FishingCard;

import java.util.ArrayList;

public class ChunkManager extends DataManager {
    private static final String TAG = "used_chunks";

    ArrayList<Chunk> usedChunks;
    Chunk lastFishedInChunk;

    public ChunkManager(FishingCard trackedFor) {
        super(trackedFor);
        this.usedChunks = new ArrayList<>();
    }

    public boolean fishedInChunk(ChunkPos chunkPos) {
        Chunk chunk = Chunk.create(chunkPos);
        lastFishedInChunk = chunk;
        for(Chunk usedChunk : usedChunks) {
            if (usedChunk.matches(chunkPos)) {
                return true;
            }
        }
        usedChunks.add(chunk);
        return false;
    }

    @Override
    public void readNbt(NbtCompound nbtCompound) {
        NbtList tag = nbtCompound.getList(TAG, NbtElement.COMPOUND_TYPE);
        usedChunks = new ArrayList<>();
        tag.forEach(chunk -> {
            Chunk usedChunk = new Chunk();
            usedChunk.readNbt((NbtCompound) chunk);
            usedChunks.add(usedChunk);
        });
    }

    @Override
    public void writeNbt(NbtCompound nbtCompound) {
        NbtList nbtList = new NbtList();
        usedChunks.forEach(chunk -> {
            NbtCompound chunkNbt = new NbtCompound();
            chunk.writeNbt(chunkNbt);
            nbtList.add(chunk.toNbt());
        });
        nbtCompound.put(TAG, nbtList);
    }


    public static class Chunk implements NbtData{
        private static final String X_TAG = "x";
        private static final String Z_TAG = "z";
        int x;
        int z;

        public static Chunk create(ChunkPos chunkPos) {
            Chunk chunk = new Chunk();
            chunk.x = chunkPos.x;
            chunk.z = chunkPos.z;
            return chunk;
        }

        public boolean matches(ChunkPos chunkPos){
            return chunkPos.x == x && chunkPos.z == z;
        }

        private NbtCompound toNbt() {
            NbtCompound chunkNbt = new NbtCompound();
            writeNbt(chunkNbt);
            return chunkNbt;
        }

        @Override
        public void readNbt(NbtCompound nbtCompound) {
            this.x = nbtCompound.getInt(X_TAG);
            this.z = nbtCompound.getInt(Z_TAG);
        }

        @Override
        public void writeNbt(NbtCompound nbtCompound) {
            nbtCompound.putInt(X_TAG, x);
            nbtCompound.putInt(Z_TAG, z);
        }
    }

}
