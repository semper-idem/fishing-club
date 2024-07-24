package net.semperidem.fishing_club.mixin.client;

import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.semperidem.fishing_club.client.FishingClubClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(SoundManager.class)
public class SoundManagerMixin {

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"))
    private void onPlay(SoundInstance sound, CallbackInfo ci){
        if (!(sound instanceof PositionedSoundInstance soundInstance)) {
            return;
        }
        String soundKey = FishingClubClient.getSoundPositionKey(soundInstance);
        ArrayList<SoundInstance> sounds = FishingClubClient.getSoundsAtPosition(soundKey);
        sounds.add(soundInstance);
        FishingClubClient.SOUNDS.put(soundKey, sounds);
    }
    @Inject(method = "stop", at = @At("HEAD"))
    private void onStop(SoundInstance sound, CallbackInfo ci){
        if (!(sound instanceof PositionedSoundInstance soundInstance)) {
            return;
        }
        String soundKey = FishingClubClient.getSoundPositionKey(soundInstance);
        ArrayList<SoundInstance> sounds = FishingClubClient.getSoundsAtPosition(soundKey);
        if (!sounds.contains(soundInstance)) {
            return;
        }
        sounds.remove(soundInstance);
        FishingClubClient.SOUNDS.put(soundKey, sounds);
    }
}
