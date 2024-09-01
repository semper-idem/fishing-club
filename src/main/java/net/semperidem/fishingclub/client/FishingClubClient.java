package net.semperidem.fishingclub.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.util.math.Vec3d;
import net.semperidem.fishingclub.registry.FCRegistry;
import net.semperidem.fishingclub.registry.FCRegistryClient;

import java.util.ArrayList;
import java.util.HashMap;

public class FishingClubClient implements ClientModInitializer {
    public static final HashMap<String, ArrayList<SoundInstance>> SOUNDS = new HashMap<>();

    public static String getSoundPositionKey(PositionedSoundInstance soundInstance) {
        return new Vec3d(soundInstance.getX(), soundInstance.getY(), soundInstance.getZ()).toString();
    }

    public static ArrayList<SoundInstance> getSoundsAtPosition(String soundKey) {
        ArrayList<SoundInstance> sounds = new ArrayList<>();
        if (FishingClubClient.SOUNDS.containsKey(soundKey)) {
            sounds = FishingClubClient.SOUNDS.get(soundKey);
        }
        return sounds;
    }

    @Override
    public void onInitializeClient() {
       FCRegistryClient.register();
    }
}
