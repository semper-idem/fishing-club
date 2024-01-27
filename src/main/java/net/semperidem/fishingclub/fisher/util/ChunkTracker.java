package net.semperidem.fishingclub.fisher.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.ChunkPos;
import net.semperidem.fishingclub.fisher.FishingCard;

import java.util.ArrayList;

public class ChunkTracker {
    FishingCard fishedInBy;
    ArrayList<Chunk> usedChunks;
    Chunk lastFishedInChunk;

    public ChunkTracker(FishingCard fishedInBy, ArrayList<Chunk> usedChunks) {
        this.fishedInBy = fishedInBy;
        this.usedChunks = usedChunks;
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

    public NbtList toNbt() {
        NbtList fishedInChunksTag = new NbtList();
        for(Chunk chunk : usedChunks) {
            fishedInChunksTag.add(chunk.toNbt());
        }
        return fishedInChunksTag;
    }

    public static ChunkTracker fromNbt(FishingCard fishedInBy, NbtList usedChunksTag) {
        ArrayList<Chunk> usedChunks = new ArrayList<>();
        for(int i = 0; i < usedChunksTag.size(); i++) {
            usedChunks.add(Chunk.fromNbt(usedChunksTag.getCompound(i)));
        }
        return new ChunkTracker(fishedInBy, usedChunks);
    }


    public static class Chunk{
        private static final String X_TAG = "x";
        private static final String Z_TAG = "z";
        int x;
        int z;

        public Chunk(int x, int z){
            this.x = x;
            this.z = z;
        }

        public static Chunk create(ChunkPos chunkPos) {
            return new Chunk(chunkPos.x, chunkPos.z);
        }

        public boolean matches(ChunkPos chunkPos){
            return chunkPos.x == x && chunkPos.z == z;
        }

        public static Chunk fromNbt(NbtCompound chunkNbt) {
            return new Chunk(chunkNbt.getInt(X_TAG), chunkNbt.getInt(Z_TAG));
        }

        public NbtCompound toNbt() {
            NbtCompound tag = new NbtCompound();
            tag.putInt(X_TAG, x);
            tag.putInt(Z_TAG, z);
            return tag;
        }
    }

}
