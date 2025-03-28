package net.semperidem.fishingclub.registry;

import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.level_reward.LevelRewardRule;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;

import java.util.HashMap;

public class Registry {

    public static HashMap<String, SoundEvent> SPECIES_TO_TUNE = new HashMap<>();
    public static final SoundEvent YELLOW_FISH_TUNE = registerSoundEvent("yellow_fish_tune");
    static {
      //  SPECIES_TO_TUNE.put(SpeciesLibrary.BUTTERFISH.label, YELLOW_FISH_TUNE);
    }


      private static SoundEvent registerSoundEvent(String name) {
        Identifier id = FishingClub.identifier(name);
        return net.minecraft.registry.Registry.register(Registries.SOUND_EVENT, FishingClub.identifier(name), SoundEvent.of(id));
      }

// in the initializer
    public static void register(){
        Blocks.register();
        Items.register();
        EntityTypes.register();
        Components.register();
        Networking.registerServer();
        ScreenHandlers.register();
        StatusEffects.register();
        LevelRewardRule.initDefaultRewards();
        TradeSecrets.register();
        Features.register();
        LootTables.register();
        Enchantments.register();
    }

    //Server-side only
    // this is so we can modify vanilla fishing rod
}
