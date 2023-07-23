package net.semperidem.fishingclub.fisher;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class FisherInfos {
    private static final String FISHER_INFO_TAG_NAME = "fisher_info";
    private static final String DELIMITER = ",";
    private static FisherInfo clientFisherInfo;


    public static PacketByteBuf getPlayerFisherInfoBuf(UUID uuid){
        return getFisherInfoBuf(getPlayerFisherInfo(uuid));
    }


    public static FisherInfo getClientInfo(){
        return clientFisherInfo;
    }

    public static void setClientInfo(FisherInfo cfi){
        if (cfi == null) return;
        clientFisherInfo = cfi;
    }

    public static void resetClientInfo(){
        clientFisherInfo = null;
    }

    public static FisherInfo getPlayerFisherInfo(UUID uuid){
        return FisherInfoDB.get(uuid);
    }


    public static void grantExperience(UUID uuid, int expGained){
        FisherInfo fisherInfo = FisherInfoDB.get(uuid);
        fisherInfo.grantExperience(expGained);
        FisherInfoDB.put(uuid, fisherInfo);
    }

    public static String getStringFromPlayerPerks(HashMap<String, FishingPerk> perks){
        StringBuilder sb = new StringBuilder();
        for(String perk : perks.keySet()) {
            sb.append(perk).append(DELIMITER);
        }
        return sb.toString();
    }

    public static HashMap<String, FishingPerk> getPlayerPerksFromString(String fishingPerksString){
        HashMap<String, FishingPerk> fishingPerks = new HashMap<>();
        String[] fishingPerksStringArray = fishingPerksString.split(DELIMITER);
        for(String fishingPerkName : fishingPerksStringArray) {
            FishingPerks.getPerkFromName(fishingPerkName).ifPresent(perk -> {
                fishingPerks.put(fishingPerkName, perk);
            });
        }
        return fishingPerks;
    }


    public static void addCredit(UUID uuid, int credit){
        FisherInfo fi = getPlayerFisherInfo(uuid);
        fi.addCredit(credit);
        FisherInfoDB.put(uuid, fi);
    }

    public static boolean removeCredit(UUID uuid, int credit){
        FisherInfo fi = getPlayerFisherInfo(uuid);
        boolean result = fi.removeCredit(credit);
        FisherInfoDB.put(uuid, fi);
        return result;
    }

    public static void zeroCredit(UUID uuid){
        FisherInfo fi = getPlayerFisherInfo(uuid);
        fi.removeCredit(fi.getFisherCredit());
        FisherInfoDB.put(uuid, fi);
    }


    public static PacketByteBuf getFisherInfoBuf(FisherInfo fisherInfo){
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(fisherInfo.level);
        buf.writeInt(fisherInfo.exp);
        buf.writeInt(fisherInfo.fisherCredit);
        buf.writeString(FisherInfos.getStringFromPlayerPerks(fisherInfo.getPerks()));
        buf.writeUuid(fisherInfo.fisherUUID);
        return buf;
    }

    public static FisherInfo readFisherInfoFromBuf(PacketByteBuf buf){
        int level = buf.readInt();
        int exp = buf.readInt();
        int fisherCredit = buf.readInt();
        String perksString = buf.readString();
        UUID fisherUUID = buf.readUuid();
        return new FisherInfo(
                level,
                exp,
                perksString,
                fisherCredit,
                fisherUUID
        );
    }

    public static void writeNBT(UUID uuid, NbtCompound playerTag) {
        if (!FisherInfoDB.contains(uuid)) {
            FisherInfoDB.putDefault(uuid);
        }
        FisherInfo fisherInfo = getPlayerFisherInfo(uuid);
        writeNBT(fisherInfo, playerTag);
    }

    private static void writeNBT(FisherInfo fisherInfo, NbtCompound playerTag){
        NbtCompound fisherInfoTag = new NbtCompound();
        fisherInfoTag.putInt("level", fisherInfo.level);
        fisherInfoTag.putInt("exp", fisherInfo.exp);
        fisherInfoTag.putInt("fisherCredit", fisherInfo.fisherCredit);
        NbtList perksList = new NbtList();
        for(String perk : fisherInfo.perks.keySet()) {
            perksList.add(NbtString.of(perk));
        }
        fisherInfoTag.put("perks", perksList);
        fisherInfoTag.putInt("perkCount", perksList.size());
        fisherInfoTag.putInt("skillPoints", fisherInfo.skillPoints);
        playerTag.put(FISHER_INFO_TAG_NAME, fisherInfoTag);
    }

    public static void readNBT(UUID uuid, NbtCompound playerTag) {
        FisherInfoDB.put(uuid, readNBT(playerTag));
    }

    private static FisherInfo readNBT(NbtCompound playerTag){
        FisherInfo fisherInfo = new  FisherInfo(playerTag.getUuid("UUID"));
        if (playerTag.contains(FISHER_INFO_TAG_NAME)) {
            NbtCompound fisherInfoTag = playerTag.getCompound(FISHER_INFO_TAG_NAME);
            fisherInfo.level = getIntFromTag(fisherInfoTag, "level");
            fisherInfo.exp = getIntFromTag(fisherInfoTag, "exp");
            fisherInfo.fisherCredit = getIntFromTag(fisherInfoTag, "fisherCredit");
            int perkCount = getIntFromTag(fisherInfoTag, "perkCount");
            NbtList perkList = fisherInfoTag.getList("perks", NbtElement.STRING_TYPE);
            for(int i = 0; i < perkCount; i++) {
                FishingPerks.getPerkFromName(perkList.getString(i)).ifPresent(fishingPerk -> fisherInfo.perks.put(fishingPerk.name, fishingPerk));
            }
            fisherInfo.skillPoints = getIntFromTag(fisherInfoTag, "skillPoints");
        }
        return fisherInfo;
    }

    private static int getIntFromTag(NbtCompound fisherInfoTag, String key){
        return Optional.of(fisherInfoTag.getInt(key)).orElse(0);
    }
}
