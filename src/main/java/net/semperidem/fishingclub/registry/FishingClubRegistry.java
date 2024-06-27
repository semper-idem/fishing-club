package net.semperidem.fishingclub.registry;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.level_reward.LevelRewardRule;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfigurationComponent;
import net.semperidem.fishingclub.item.fishing_rod.components.RodPartComponent;
import net.semperidem.fishingclub.screen.dialog.DialogUtil;

public class FishingClubRegistry {

// in the initializer
    public static void register(){
        BlockRegistry.register();
        ItemRegistry.register();
        EntityTypeRegistry.register();
        NetworkRegistry.register();
        ScreenHandlerRegistry.register();
        StatusEffectRegistry.register();
        EnchantmentRegistry.register();
        LevelRewardRule.initDefaultRewards();
        FishingPerks.register();
        DialogUtil.register();
        ComponentRegistry.register();
    }

    public static void registerClient(){
        NetworkRegistry.registerClient();
        ScreenHandlerRegistry.registerClient();
        KeybindingRegistry.registerClient();
        ModelPredicateProviderRegistry.registerClient();
    }
}
