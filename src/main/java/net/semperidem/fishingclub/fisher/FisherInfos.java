package net.semperidem.fishingclub.fisher;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.network.ServerPacketSender;

import java.util.HashMap;
import java.util.UUID;

public class FisherInfos {
    private static final String FISHER_INFO_TAG_NAME = "fisher_info";
    private static final String DELIMITER = ",";
    private static FisherInfo clientFisherInfo;

    private static final HashMap<UUID, FisherInfo> FISHER_INFOS = new HashMap<>();

    public static FisherInfo getClientInfo(){
        ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
        return new FisherInfo(clientPlayerEntity, clientPlayerEntity.writeNbt(new NbtCompound()));
    }

    public static void setClientInfo(FisherInfo cfi){
        if (cfi == null) return;
        clientFisherInfo = cfi;
    }

    public static void resetClientInfo(){
        clientFisherInfo = null;
    }

    public static FisherInfo getPlayerFisherInfo(PlayerEntity playerEntity){
        return new FisherInfo(playerEntity, playerEntity.writeNbt(new NbtCompound()));
    }

    public static void updateFisher(UUID uuid, FisherInfo fisherInfo){
        FISHER_INFOS.put(uuid, fisherInfo);
    }

    public static FisherInfo getFisher(PlayerEntity user){
        return new FisherInfo(user, user.getDataTracker().get(FishingClub.FISHER_NBT));
        //return user.world.isClient() ? getPlayerFisherInfo(user) : FISHER_INFOS.get(user.getUuid());
    }

    public static void addExperience(PlayerEntity playerEntity, int expGained){
        NbtCompound playerTag = playerEntity.writeNbt(new NbtCompound()); // Read from PLAYER write into TAG
        FisherInfo fisherInfo = new FisherInfo(playerEntity, playerTag); // Read from TAG write into INFO
        fisherInfo.grantExperience(expGained);
        fisherInfo.writeNbt(playerTag); // Read from INFO write into TAG
        playerEntity.readNbt(playerTag); // Read from TAG into PLAYER
        ServerPacketSender.sendFisherInfoSync((ServerPlayerEntity) playerEntity);
    }

    public static void addPerk(PlayerEntity playerEntity, String perkName) {
        NbtCompound playerTag = playerEntity.writeNbt(new NbtCompound());
        FisherInfo fisherInfo = new FisherInfo(playerEntity, playerTag);
        fisherInfo.addPerk(perkName);
        fisherInfo.writeNbt(playerTag);
        playerEntity.readNbt(playerTag);
        ServerPacketSender.sendFisherInfoSync((ServerPlayerEntity) playerEntity);
    }

    public static boolean addCredit(PlayerEntity playerEntity, int credit){
        NbtCompound playerTag = playerEntity.writeNbt(new NbtCompound());
        FisherInfo fisherInfo = new FisherInfo(playerEntity, playerTag);
        boolean response = fisherInfo.addCredit(credit);
        if (response) {
            fisherInfo.writeNbt(playerTag);
            playerEntity.readNbt(playerTag);
            ServerPacketSender.sendFisherInfoSync((ServerPlayerEntity) playerEntity);
        }
        return response;
    }

    public static void setSkillPoint(PlayerEntity playerEntity, int count){
        NbtCompound playerTag = playerEntity.writeNbt(new NbtCompound());
        FisherInfo fisherInfo = new FisherInfo(playerEntity, playerTag);
        fisherInfo.setSkillPoints(count);
        fisherInfo.writeNbt(playerTag);
        playerEntity.readNbt(playerTag);
        ServerPacketSender.sendFisherInfoSync((ServerPlayerEntity) playerEntity);
    }

    public static void removePerk(PlayerEntity playerEntity, String perkName){
        NbtCompound playerTag = playerEntity.writeNbt(new NbtCompound());
        FisherInfo fisherInfo = new FisherInfo(playerEntity, playerTag);
        fisherInfo.removePerk(perkName);
        fisherInfo.writeNbt(playerTag);
        playerEntity.readNbt(playerTag);
        ServerPacketSender.sendFisherInfoSync((ServerPlayerEntity) playerEntity);
    }
}
