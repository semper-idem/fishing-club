package net.semperidem.fishingclub.mixin.common;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.storage.SaveVersionInfo;
import net.semperidem.fishingclub.FishingLevelProperties;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.UUID;

@Mixin(LevelProperties.class)
public class LevelPropertiesMixin implements FishingLevelProperties {
    @Unique
    private static final String FISHING_KING_UUID_TAG = "FishingKingUUID";
    @Unique
    private static final String FISHING_KING_NAME_TAG = "FishingKingName";
    @Unique
    private static final String CLAIM_PRICE_TAG = "ClaimPrice";
    @Unique
    private static final String FISHING_CLUB_TAG = "FishingClub";
    @Unique
    private static final int MIN_CLAIM_PRICE = 10000;
    @Unique
    private UUID fishingKingUUID;
    @Unique
    private String fishingKingName = "";
    @Unique
    private int claimPrice = MIN_CLAIM_PRICE;

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
    public int getMinFishingKingClaimPrice() {
        if (getClaimPrice() <= MIN_CLAIM_PRICE) {
            return MIN_CLAIM_PRICE;
        }
        return (int) (getClaimPrice() * 1.1f);
    }

    @Override
    public void setFishingKing(UUID playerUUID, String playerName) {
        this.fishingKingUUID = playerUUID;
        this.fishingKingName = playerName;
    }


    @Override
    public boolean claimCape(UUID claimedBy, String claimedByName, int claimPrice) {
        if (claimPrice < getMinFishingKingClaimPrice()) {
            return false;
        }
        this.fishingKingUUID = claimedBy;
        this.fishingKingName = claimedByName;
        this.claimPrice = claimPrice;
        return true;
    }

    @Override
    public void setClaimPrice(int claimPrice) {
        this.claimPrice = claimPrice;
    }
}
