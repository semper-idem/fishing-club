package net.semperidem.fishingclub.registry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import static net.minecraft.client.item.ModelPredicateProviderRegistry.*;

public class FCModelPredicateProvider {

    public static void registerClient(){
        register(FCItems.MEMBER_FISHING_ROD, Identifier.ofVanilla("cast"), (itemStack, clientWorld, livingEntity, clamp) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            if (!(livingEntity instanceof PlayerEntity)) {
                return 0.0F;
            }
            return ((PlayerEntity)livingEntity).fishHook != null ? 1.0F : 0.0F;
        });
        register(FCItems.MEMBER_FISHING_ROD, Identifier.ofVanilla("casting"), (itemStack, clientWorld, livingEntity, clamp) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.isUsingItem() ? 1.0F : 0.0F;
        });
    }
}
