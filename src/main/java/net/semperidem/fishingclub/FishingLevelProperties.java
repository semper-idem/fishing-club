package net.semperidem.fishingclub;

import net.minecraft.entity.player.PlayerEntity;
import net.semperidem.fishingclub.fisher.cape.Claim;

import java.util.UUID;

public interface FishingLevelProperties {
    UUID getFishingKingUUID();

    String getFishingKingName();

    int getClaimPrice();
    int getMinFishingKingClaimPrice(PlayerEntity player);
    void setFishingKing(UUID playerUUID, String playerName);

    boolean claimCape(PlayerEntity claimedBy, int claimPrice);
    void setClaimPrice(int claimPrice);
    void clearClaimTimestamps();
    void addClaimTimestamp(Claim claim);
    long getCurrentClaimTime();
}
