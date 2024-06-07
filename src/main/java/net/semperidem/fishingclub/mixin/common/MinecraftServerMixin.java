package net.semperidem.fishingclub.mixin.common;

import net.minecraft.server.MinecraftServer;
import net.semperidem.fishingclub.leaderboard.LeaderboardManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "save", at=@At("TAIL"))
    private void onSave(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        LeaderboardManager.saveLeaderboards((MinecraftServer) (Object)this);
    }
    @Inject(method = "loadWorld", at=@At("TAIL"))
    private void onLoadWorld(CallbackInfo ci) {
        LeaderboardManager.loadLeaderboards((MinecraftServer) (Object)this);
    }
}
