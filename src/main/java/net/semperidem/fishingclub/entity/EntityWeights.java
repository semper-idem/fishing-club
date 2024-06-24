package net.semperidem.fishingclub.entity;

import net.minecraft.entity.*;
import java.util.HashMap;

import static net.minecraft.entity.EntityType.*;
import static net.semperidem.fishingclub.registry.EntityTypeRegistry.HOOK_ENTITY;

public class EntityWeights {
    private static final HashMap<EntityType<?>, Integer> ENTITY_TYPE_TO_MAGNITUDE = new HashMap<>();
    private static final int DEFAULT = 0;
    public static double getEntityMagnitude(EntityType<?> entityType) {
        return ENTITY_TYPE_TO_MAGNITUDE.getOrDefault(entityType, DEFAULT);
    }
    //-4 for super heavy, 4 for super light

    static {
        ENTITY_TYPE_TO_MAGNITUDE.put(ALLAY, 4);
        ENTITY_TYPE_TO_MAGNITUDE.put(ARMOR_STAND, 1);
        ENTITY_TYPE_TO_MAGNITUDE.put(ARROW, 4);
        ENTITY_TYPE_TO_MAGNITUDE.put(AXOLOTL, 2);
        ENTITY_TYPE_TO_MAGNITUDE.put(BAT, 3);
        ENTITY_TYPE_TO_MAGNITUDE.put(BEE, 3);
        ENTITY_TYPE_TO_MAGNITUDE.put(BLAZE, 1);
        ENTITY_TYPE_TO_MAGNITUDE.put(BOAT, 1);
        ENTITY_TYPE_TO_MAGNITUDE.put(CHEST_BOAT, 1);
        ENTITY_TYPE_TO_MAGNITUDE.put(CAT, 2);
        ENTITY_TYPE_TO_MAGNITUDE.put(CHICKEN, 2);
        ENTITY_TYPE_TO_MAGNITUDE.put(COW, -1);
        ENTITY_TYPE_TO_MAGNITUDE.put(CREEPER, 1);
        ENTITY_TYPE_TO_MAGNITUDE.put(DONKEY, -1);
        ENTITY_TYPE_TO_MAGNITUDE.put(ELDER_GUARDIAN, -3);
        ENTITY_TYPE_TO_MAGNITUDE.put(ENDER_DRAGON, -5);
        ENTITY_TYPE_TO_MAGNITUDE.put(ENDERMITE, 3);
        ENTITY_TYPE_TO_MAGNITUDE.put(FOX, 1);
        ENTITY_TYPE_TO_MAGNITUDE.put(FROG, 2);
        ENTITY_TYPE_TO_MAGNITUDE.put(GIANT, -5);
        ENTITY_TYPE_TO_MAGNITUDE.put(GLOW_SQUID, 1);
        ENTITY_TYPE_TO_MAGNITUDE.put(GUARDIAN, -1);
        ENTITY_TYPE_TO_MAGNITUDE.put(HOGLIN, -2);
        ENTITY_TYPE_TO_MAGNITUDE.put(HORSE, -2);
        ENTITY_TYPE_TO_MAGNITUDE.put(IRON_GOLEM, -3);
        ENTITY_TYPE_TO_MAGNITUDE.put(LLAMA, -1);
        ENTITY_TYPE_TO_MAGNITUDE.put(MAGMA_CUBE, 1);
        ENTITY_TYPE_TO_MAGNITUDE.put(MULE, -1);
        ENTITY_TYPE_TO_MAGNITUDE.put(MOOSHROOM, -1);
        ENTITY_TYPE_TO_MAGNITUDE.put(OCELOT, 1);
        ENTITY_TYPE_TO_MAGNITUDE.put(PANDA, -1);
        ENTITY_TYPE_TO_MAGNITUDE.put(PARROT, 3);
        ENTITY_TYPE_TO_MAGNITUDE.put(POLAR_BEAR, -2);
        ENTITY_TYPE_TO_MAGNITUDE.put(PUFFERFISH, 2);
        ENTITY_TYPE_TO_MAGNITUDE.put(RABBIT, 2);
        ENTITY_TYPE_TO_MAGNITUDE.put(RAVAGER, -2);
        ENTITY_TYPE_TO_MAGNITUDE.put(SALMON, 2);
        ENTITY_TYPE_TO_MAGNITUDE.put(SHULKER, -1);
        ENTITY_TYPE_TO_MAGNITUDE.put(SILVERFISH, 2);
        ENTITY_TYPE_TO_MAGNITUDE.put(SKELETON, 1);
        ENTITY_TYPE_TO_MAGNITUDE.put(SLIME, 1);
        ENTITY_TYPE_TO_MAGNITUDE.put(SQUID, 1);
        ENTITY_TYPE_TO_MAGNITUDE.put(STRAY, 1);
        ENTITY_TYPE_TO_MAGNITUDE.put(STRIDER, -1);
        ENTITY_TYPE_TO_MAGNITUDE.put(TADPOLE, 3);
        ENTITY_TYPE_TO_MAGNITUDE.put(TRIDENT, 1);
        ENTITY_TYPE_TO_MAGNITUDE.put(TRADER_LLAMA, -1);
        ENTITY_TYPE_TO_MAGNITUDE.put(TROPICAL_FISH, 2);
        ENTITY_TYPE_TO_MAGNITUDE.put(TURTLE, -1);
        ENTITY_TYPE_TO_MAGNITUDE.put(WARDEN, -3);
        ENTITY_TYPE_TO_MAGNITUDE.put(WITHER, -2);
        ENTITY_TYPE_TO_MAGNITUDE.put(WITHER_SKELETON, 1);
        ENTITY_TYPE_TO_MAGNITUDE.put(WOLF, 1);
        ENTITY_TYPE_TO_MAGNITUDE.put(ZOGLIN, -2);
        ENTITY_TYPE_TO_MAGNITUDE.put(ZOMBIE_HORSE, -1);
        ENTITY_TYPE_TO_MAGNITUDE.put(FISHING_BOBBER, 3);
        ENTITY_TYPE_TO_MAGNITUDE.put(HOOK_ENTITY, 3);
    }
}
