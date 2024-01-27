package net.semperidem.fishingclub.fisher.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fisher.FishingCard;

public class TeleportRequest{
    private final FishingCard requestFor;
    public final String targetUUID;
    public final long requestTick;

    private static final String TARGET_TAG = "target_UUID";
    private static final String REQUEST_TICK_TAG = "request_tick";


    public TeleportRequest(FishingCard requestFor, String summonerUUID, long worldTime){
        this.requestFor = requestFor;
        this.targetUUID = summonerUUID;
        this.requestTick = worldTime;
    }


    public boolean canAccept(){
        return requestFor.getOwner().getWorld().getTime() - requestTick < 600;
    }

    public boolean isTarget(ServerPlayerEntity possibleTarget) {
        return possibleTarget.getUuidAsString().equalsIgnoreCase(targetUUID);
    }

    public static void execute(ServerPlayerEntity from, ServerPlayerEntity to) {
        from.teleport(to.getWorld(), to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
    }


    public static TeleportRequest fromNbt(FishingCard fishingCard, NbtCompound teleportRequestTag){
        String summonerUUID = "";
        if (teleportRequestTag.contains(TARGET_TAG)) {
            teleportRequestTag.getString(TARGET_TAG);
        }
        long requestTick = 0;
        if (teleportRequestTag.contains(REQUEST_TICK_TAG)) {
            teleportRequestTag.getLong(REQUEST_TICK_TAG);
        }
        return new TeleportRequest(fishingCard, summonerUUID, requestTick);
    }

    public static NbtCompound toNbt(TeleportRequest teleportRequest){
        NbtCompound tag = new NbtCompound();
        if (teleportRequest != null) {
            tag.putString(TARGET_TAG, teleportRequest.targetUUID);
            tag.putLong(REQUEST_TICK_TAG, teleportRequest.requestTick);
        }
        return tag;
    }
}