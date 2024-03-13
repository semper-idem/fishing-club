package net.semperidem.fishingclub;

import java.util.UUID;

public interface FishingLevelProperties {
    UUID getFishingKingUUID();

    String getFishingKingName();

    int getClaimPrice();
    int getMinFishingKingClaimPrice();
    void setFishingKing(UUID playerUUID, String playerName);

    boolean claimCape(UUID claimedBy, String claimedByName, int claimPrice);
    void setClaimPrice(int claimPrice);
}
