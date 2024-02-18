package net.semperidem.fishingclub.registry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ModelPredicateProviderRegistry {

    public static void registerClient(){
        net.minecraft.client.item.ModelPredicateProviderRegistry.register(ItemRegistry.CUSTOM_FISHING_ROD, new Identifier("casting"), (itemStack, clientWorld, livingEntity, clamp) -> {
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
