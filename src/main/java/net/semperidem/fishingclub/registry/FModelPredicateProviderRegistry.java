package net.semperidem.fishingclub.registry;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class FModelPredicateProviderRegistry {

    public static void registerClient(){
        ModelPredicateProviderRegistry.register(FItemRegistry.CUSTOM_FISHING_ROD, new Identifier("casting"), (itemStack, clientWorld, livingEntity, clamp) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            if (!(livingEntity instanceof PlayerEntity)) {
                return 0.0F;
            }
            return ((PlayerEntity)livingEntity).fishHook != null ? 1.0F : 0.0F;
        });
    }
}
