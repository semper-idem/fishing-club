package net.semperidem.fishingclub.game.components;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodStatType;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;

public class BobberComponent {
    private static final float STARTING_BOBBER_LENGTH = 0.25f;
    final float bobberSize;
    float bobberPos;
    float bobberSpeed;
    float lastFishPos;
    float fishBaseForce;

    public BobberComponent(int fishLevel, PlayerEntity player, FishingCard fishingCard, ItemStack caughtUsing, boolean boatFishing) {
        bobberSize = getBobberLength(player, fishingCard, caughtUsing, boatFishing);
        bobberPos = 0.5f - bobberSize / 2;
        fishBaseForce = ((fishLevel + 50f) / 50);
    }

    public void tick(float reelForce, float fishPos) {
        lastFishPos = fishPos;
        bobberSpeed = getBobberSpeed(reelForce);
        bobberPos = getNextBobberPos();

    }

    public float getNextBobberPos(){
        return Math.max(Math.min(bobberPos + bobberSpeed, 1 - bobberSize), 0);
    }

    public float getBobberPos() {
        return bobberPos;
    }


    private float getBobberSpeed(float reelForce){
        return MathHelper.clamp(getFishForce() + reelForce, -1f, 1f);
    }
    private float getFishForce() {
        return -((lastFishPos - 0.5f) / 50f) * fishBaseForce;
    }



    private float getBobberLength(PlayerEntity player, FishingCard fishingCard, ItemStack caughtUsing, boolean boatFishing){
        float bobberLengthMultiplier = 0.9f;

        bobberLengthMultiplier += boatFishing ? fishingCard.hasPerk(FishingPerks.BOAT_BOBBER_SIZE) ? 0.2f : 0.1f : 0;
        bobberLengthMultiplier += FishingRodPartController.getStat(caughtUsing, FishingRodStatType.BOBBER_WIDTH);
        bobberLengthMultiplier += player.hasStatusEffect(FStatusEffectRegistry.BOBBER_BUFF) ? 0.1f : 0;

        return STARTING_BOBBER_LENGTH * bobberLengthMultiplier;
    }

    public float getBobberSize() {
        return bobberSize;
    }
}
