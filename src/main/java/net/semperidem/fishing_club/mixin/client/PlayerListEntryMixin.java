package net.semperidem.fishing_club.mixin.client;


import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import net.semperidem.fishing_club.FishingClub;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
