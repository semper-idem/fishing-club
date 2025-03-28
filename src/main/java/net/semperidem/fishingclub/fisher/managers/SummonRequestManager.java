package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fisher.Card;

import java.util.Objects;

public class SummonRequestManager extends DataManager {
    private static final int REQUEST_VALID_TIME = 600;
    public String targetUUID = "";
    public long requestTick = 0L;

    public SummonRequestManager(Card requestFor){
        super(requestFor);
    }

    public void set(ServerPlayerEntity target){
        targetUUID = target.getUuidAsString();
        requestTick = target.getWorld().getTime();
        sync();
    }

    public boolean canAccept(){
        return trackedFor.holder().getWorld().getTime() - requestTick < REQUEST_VALID_TIME;
    }

    public boolean isTarget(ServerPlayerEntity possibleTarget) {
        return possibleTarget.getUuidAsString().equalsIgnoreCase(targetUUID);
    }

    public void execute() {
        if (!(trackedFor.holder() instanceof ServerPlayerEntity source)) {
            return;
        }
        if (!canAccept()){
            return;
        }
        targetUUID = "";
        requestTick = 0;
        source.server.getPlayerManager().getPlayerList().stream()
                .filter(Objects::nonNull)
                .filter(this::isTarget)
                .findAny()
                .ifPresent(target -> teleport(source, target));
    }

    private static void teleport(ServerPlayerEntity source, ServerPlayerEntity target) {
        source.teleport(
                target.getServerWorld(),
                target.getX(),
                target.getY(),
                target.getZ(),
                target.getYaw(),
                target.getPitch()
        );
    }

    @Override
    public void readNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        if (!nbtCompound.contains(TAG)) {
            return;
        }
        NbtCompound summonTag = nbtCompound.getCompound(TAG);
        targetUUID = summonTag.contains(TARGET_TAG) ? summonTag.getString(TARGET_TAG) : "";
        requestTick = summonTag.contains(REQUEST_TICK_TAG) ? summonTag.getLong(REQUEST_TICK_TAG) : 0;
    }

    @Override
    public void writeNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        if (targetUUID == null) {
            return;
        }
        NbtCompound summonTag = new NbtCompound();
        summonTag.putString(TARGET_TAG, targetUUID);
        summonTag.putLong(REQUEST_TICK_TAG, requestTick);
        nbtCompound.put(TAG, summonTag);
    }

    private static final String TAG = "summon";
    private static final String TARGET_TAG = "target_UUID";
    private static final String REQUEST_TICK_TAG = "request_tick";
}