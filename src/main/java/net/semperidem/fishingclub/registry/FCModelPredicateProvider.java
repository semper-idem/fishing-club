package net.semperidem.fishingclub.registry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import static net.minecraft.client.item.ModelPredicateProviderRegistry.register;

public class FCModelPredicateProvider {

    public static void registerClient(){
        //todo use tags or smt
        register(FCItems.CORE_BAMBOO, Identifier.ofVanilla("cast"), (itemStack, clientWorld, livingEntity, clamp) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            if (!(livingEntity instanceof PlayerEntity)) {
                return 0.0F;
            }
            return ((PlayerEntity)livingEntity).fishHook != null && itemStack.isOf(FCItems.CORE_BAMBOO) ? 1.0F : 0.0F;
        });
        register(FCItems.CORE_BAMBOO, Identifier.ofVanilla("casting"), (itemStack, clientWorld, livingEntity, clamp) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.isUsingItem() && itemStack.isOf(FCItems.CORE_BAMBOO) ? 1.0F : 0.0F;
        });
    }
}
