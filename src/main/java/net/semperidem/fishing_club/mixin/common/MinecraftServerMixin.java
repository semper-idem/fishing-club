package net.semperidem.fishing_club.mixin.common;

import net.minecraft.server.MinecraftServer;
import net.semperidem.fishing_club.LeaderboardTrackingServer;
import net.semperidem.fishing_club.leaderboard.LeaderboardSerializer;
import net.semperidem.fishing_club.leaderboard.LeaderboardTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements LeaderboardTrackingServer {
    @Unique
    LeaderboardTracker tracker;

    @Override
    public LeaderboardTracker getLeaderboardTracker() {
        if (tracker == null) {
            tracker = new LeaderboardTracker();
        }
        return tracker;
    }

    @Inject(method = "save", at=@At("TAIL"))
    private void onSave(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        LeaderboardSerializer.saveLeaderboards(getLeaderboardTracker(), (MinecraftServer) (Object)this);
    }
    @Inject(method = "loadWorld", at=@At("TAIL"))
    private void onLoadWorld(CallbackInfo ci) {
        LeaderboardSerializer.loadLeaderboards(getLeaderboardTracker(), (MinecraftServer) (Object)this);
    }

}
