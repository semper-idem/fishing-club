package net.semperidem.fishingclub.mixin.client;


import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.FishingClubClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin {
    private static final Identifier KING_CAPE = FishingClub.getIdentifier("textures/king_cape.png");

    @Shadow public abstract GameProfile getProfile();

    //TODO
//    @Inject(method="getCapeTexture", at = @At("TAIL"), cancellable = true)
//    private void onGetCapeTexture(CallbackInfoReturnable<Identifier> cir) {
//        if (FishingClubClient.FISHING_KING_UUID == null) {
//            return;
//        }
//
//        if (getProfile().getId().equals(FishingClubClient.FISHING_KING_UUID)) {
//            cir.setReturnValue(KING_CAPE);
//        }
//    }

}
