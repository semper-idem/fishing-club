package net.semperidem.fishingclub.mixin.common;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.storage.SaveVersionInfo;
import net.semperidem.fishingclub.FishingLevelProperties;
import net.semperidem.fishingclub.LeaderboardTrackingServer;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.cape.Claim;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mixin(LevelProperties.class)
public class LevelPropertiesMixin implements FishingLevelProperties {
    @Unique
    private static final float CLAIM_MULTIPLIER = 1.5f;

    @Shadow private long time;
    @Unique
    private static final String FISHING_KING_UUID_TAG = "FishingKingUUID";
    @Unique
    private static final String FISHING_KING_NAME_TAG = "FishingKingName";
    @Unique
    private static final String CLAIM_PRICE_TAG = "ClaimPrice";
    @Unique
    private static final String FISHING_CLUB_TAG = "FishingClub";
    @Unique
    private static final String CLAIM_TS_TAG = "ClaimTs";
    @Unique
    private static final int MIN_CLAIM_PRICE = 10000;
    @Unique
    private UUID fishingKingUUID;
    @Unique
    private String fishingKingName = "";
    @Unique
    private int claimPrice = MIN_CLAIM_PRICE;

    private ArrayList<Claim> capeClaimTimestamps = new ArrayList<>();

    @Inject(method =  "readProperties", at=@At("RETURN"))
    private static void onReadProperties(Dynamic<NbtElement> dynamic, DataFixer dataFixer, int dataVersion, @Nullable NbtCompound playerData, LevelInfo levelInfo, SaveVersionInfo saveVersionInfo, GeneratorOptions generatorOptions, Lifecycle lifecycle, CallbackInfoReturnable<LevelProperties> cir) {
        dynamic.get(FISHING_CLUB_TAG).result().ifPresent(fishingClubNbt -> {
            String fishingKingUUIDString =  fishingClubNbt.get(FISHING_KING_UUID_TAG).asString("");
            String fishingKingUUIDName =  fishingClubNbt.get(FISHING_KING_NAME_TAG).asString("");
            int claimPrice = fishingClubNbt.get(CLAIM_PRICE_TAG).asInt(MIN_CLAIM_PRICE);
            LevelProperties levelProperties = cir.getReturnValue();
            if (levelProperties instanceof  FishingLevelProperties fishingLevelProperties) {
                if (!Objects.equals(fishingKingUUIDString, "")) {
                    fishingLevelProperties.setFishingKing(UUID.fromString(fishingKingUUIDString), fishingKingUUIDName);
                }
                fishingLevelProperties.setClaimPrice(claimPrice);
                fishingLevelProperties.clearClaimTimestamps();
                List<DataResult<String>> claimTS = fishingClubNbt.get(CLAIM_TS_TAG).asList(Dynamic::asString);
                for(DataResult<String> claimRecord : claimTS) {
                    String [] claimString = claimRecord.get().left().orElse("").split(";");
                    if (claimString.length > 0) {
                        fishingLevelProperties.addClaimTimestamp(new Claim(claimString[0], Long.parseLong(claimString[1])));
                    }
                }
            }
        });
    }

    @Inject(method = "updateProperties", at=@At("RETURN"))
    private void onUpdateProperties(DynamicRegistryManager registryManager, NbtCompound levelNbt, NbtCompound playerNbt, CallbackInfo ci) {
        String fishingKingUUIDString = fishingKingUUID == null ? "" : fishingKingUUID.toString();
        NbtCompound fishingClubNbt = new NbtCompound();
        fishingClubNbt.putString(FISHING_KING_UUID_TAG, fishingKingUUIDString);
        fishingClubNbt.putString(FISHING_KING_NAME_TAG, fishingKingName);
        fishingClubNbt.putInt(CLAIM_PRICE_TAG, claimPrice);
        NbtList claimTsNbt = new NbtList();
        for(Claim claim : capeClaimTimestamps) {
            claimTsNbt.add(NbtString.of(claim.toString()));
        }
        fishingClubNbt.put(CLAIM_TS_TAG, claimTsNbt);
        levelNbt.put(FISHING_CLUB_TAG, fishingClubNbt);
    }

    @Override
    public UUID getFishingKingUUID() {
        return fishingKingUUID;
    }

    @Override
    public String getFishingKingName() {
        return fishingKingName;
    }

    @Override
    public int getClaimPrice() {
        return claimPrice;
    }

    @Override
    public int getMinFishingKingClaimPrice(PlayerEntity player) {
        if (getClaimPrice() <= MIN_CLAIM_PRICE) {
            return MIN_CLAIM_PRICE;
        }
        float discount = 0;
        boolean hasCape = this.fishingKingUUID.compareTo(player.getUuid()) == 0;
        if (!hasCape && player.getServer() instanceof LeaderboardTrackingServer leaderboardTrackingServer) {
            discount = leaderboardTrackingServer.getLeaderboardTracker().getDiscount(player);
        }
        return (int) (getClaimPrice() * (CLAIM_MULTIPLIER - discount));
    }

    @Override
    public void setFishingKing(UUID playerUUID, String playerName) {
        this.fishingKingUUID = playerUUID;
        this.fishingKingName = playerName;
    }


    @Override
    public boolean claimCape(PlayerEntity claimedBy, int claimPrice) {
        if (claimPrice < getMinFishingKingClaimPrice(claimedBy)) {
            return false;
        }
        this.fishingKingUUID = claimedBy.getUuid();
        this.fishingKingName = claimedBy.getName().getString();
        this.claimPrice = claimPrice;
        if (!capeClaimTimestamps.isEmpty()) {
            long timeOwner = time - capeClaimTimestamps.get(capeClaimTimestamps.size() - 1).timestamp();
            FishingCard.getPlayerCard(claimedBy).addCapeTime(timeOwner);
        }
        capeClaimTimestamps.add(new Claim(fishingKingUUID.toString(), time));
        return true;
    }

    @Override
    public void setClaimPrice(int claimPrice) {
        this.claimPrice = claimPrice;
    }

    @Override
    public void clearClaimTimestamps() {
        this.capeClaimTimestamps = new ArrayList<>();
    }

    @Override
    public void addClaimTimestamp(Claim claim) {
        this.capeClaimTimestamps.add(claim);
    }

    @Override
    public long getCurrentClaimTime() {
        if (capeClaimTimestamps.isEmpty()) {
            return 0;
        }
        return time - capeClaimTimestamps.get(capeClaimTimestamps.size() - 1).timestamp();
    }
}
