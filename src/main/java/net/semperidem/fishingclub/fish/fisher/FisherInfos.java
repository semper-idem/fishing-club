package net.semperidem.fishingclub.fish.fisher;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
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
        clientFisherInfo = cfi;
    }

    public static FisherInfo getPlayerFisherInfo(UUID uuid){
        if (!FisherInfoDB.contains(uuid)) {
            FisherInfoDB.put(uuid, new FisherInfo(1,0));
        }
        return FisherInfoDB.get(uuid);
    }


    public static void grantExperience(UUID uuid, int expGained){
        FisherInfo fisherInfo = FisherInfoDB.get(uuid);
        fisherInfo.grantExperience(expGained);
        FisherInfoDB.put(uuid, fisherInfo);
    }

    public static String getStringFromPlayerPerks(ArrayList<FishingPerk> perks){
        StringBuilder sb = new StringBuilder();
        for(FishingPerk perk : perks) {
            sb.append(perk.name).append(DELIMITER);
        }
        return sb.toString();
    }

    public static ArrayList<FishingPerk> getPlayerPerksFromString(String fishingPerksString){
        ArrayList<FishingPerk> fishingPerks = new ArrayList<>();
        String[] fishingPerksStringArray = fishingPerksString.split(DELIMITER);
        for(String fishingPerkName : fishingPerksStringArray) {
            FishingPerks.getPerkFromName(fishingPerkName).ifPresent(fishingPerks::add);
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
        return buf;
    }

    public static FisherInfo readFisherInfoFromBuf(PacketByteBuf buf){
        int level = buf.readInt();
        int exp = buf.readInt();
        int fisherCredit = buf.readInt();
        String perksString = buf.readString();
        return new FisherInfo(level, exp, perksString, fisherCredit);
    }

    public static void writeNBT(UUID uuid, NbtCompound playerTag) {
        writeNBT(getPlayerFisherInfo(uuid), playerTag);
    }

    private static void writeNBT(FisherInfo fisherInfo, NbtCompound playerTag){
        NbtCompound fishingTag = new NbtCompound();
        fishingTag.putInt("level", fisherInfo.level);
        fishingTag.putInt("exp", fisherInfo.exp);
        fishingTag.putInt("fisherCredit", fisherInfo.fisherCredit);
        NbtList perksList = new NbtList();
        for(FishingPerk perk : fisherInfo.perks) {
            perksList.add(NbtString.of(perk.name));
        }
        fishingTag.put("perks", perksList);
        fishingTag.putInt("perkCount", perksList.size());
        playerTag.put(FISHER_INFO_TAG_NAME, fishingTag);
    }

    public static void readNBT(UUID uuid, NbtCompound playerTag) {
        FisherInfoDB.put(uuid, readNBT(playerTag));
    }

    private static FisherInfo readNBT(NbtCompound playerTag){
        FisherInfo fisherInfo = new FisherInfo();
        if (playerTag.contains(FISHER_INFO_TAG_NAME, 10)) {
            NbtCompound fishingTag = playerTag.getCompound(FISHER_INFO_TAG_NAME);
            fisherInfo.level = fishingTag.getInt("level");
            fisherInfo.exp = fishingTag.getInt("exp");
            fisherInfo.fisherCredit = fishingTag.getInt("fisherCredit");
            int perkCount = fishingTag.getInt("perkCount");
            NbtList perkList = fishingTag.getList("perks", perkCount);
            for(int i = 0; i < perkCount; i++) {
                FishingPerks.getPerkFromName(perkList.getString(i)).ifPresent(fishingPerk -> fisherInfo.perks.add(fishingPerk));
            }

        }
        return fisherInfo;
    }

}
