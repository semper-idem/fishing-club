package net.semperidem.fishingclub;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil;
import net.semperidem.fishingclub.entity.CustomFishingBobberEntity;
import net.semperidem.fishingclub.entity.CustomFishingBobberEntityRenderer;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.FishermanEntityRenderer;
import net.semperidem.fishingclub.fish.FishTypes;
import net.semperidem.fishingclub.fisher.FisherInfoDB;
import net.semperidem.fishingclub.item.CustomFishingRod;
import net.semperidem.fishingclub.network.ClientPacketReceiver;
import net.semperidem.fishingclub.network.ServerPacketReceiver;

public class FishingClub implements ModInitializer {
    public static final String MOD_ID = "fishing-club";

    public static final Item CUSTOM_FISHING_ROD = new CustomFishingRod(new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));

    public static final EntityType<FishermanEntity> FISHERMAN = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "fisherman"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, FishermanEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                    .build()
    );
    public static final EntityType<CustomFishingBobberEntity> CUSTOM_FISHING_BOBBER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "custom_fishing_bobber"),
            FabricEntityTypeBuilder.<CustomFishingBobberEntity>create(SpawnGroup.MISC, CustomFishingBobberEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .disableSummon()
                    .disableSaving()
                    .trackRangeBlocks(4)
                    .trackedUpdateRate(5)
                    .build()
    );
// public static final EntityType<FishingBobberEntity> FISHING_BOBBER = EntityType.register("fishing_bobber",
// Builder.create(FishingBobberEntity::new, SpawnGroup.MISC).disableSaving().disableSummon().setDimensions(0.25f, 0.25f).maxTrackingRange(4).trackingTickInterval(5));
//
    @Override
    public void onInitialize() {
        //Screens
        ShopScreenUtil.register();

        //Fish
        FishTypes.initFishTypes();
        ServerLifecycleEvents.SERVER_STARTED.register(FisherInfoDB::linkServer);

        //Network
        ServerPacketReceiver.registerServerPacketHandlers();
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketReceiver.registerClientPacketHandlers();
        }

        //Commands
        FishingClubCommands.register();

        //Entity
        FabricDefaultAttributeRegistry.register(FISHERMAN, WanderingTraderEntity.createMobAttributes());

        ServerEntityWorldChangeEvents.AFTER_ENTITY_CHANGE_WORLD.register((originalEntity, newEntity, origin, destination) -> {
            if(newEntity instanceof WanderingTraderEntity) {
                WanderingTraderEntity oldEntity = (WanderingTraderEntity) originalEntity;
                FishermanEntity fishermanEntity = FISHERMAN.create(destination);
                fishermanEntity.setPos(oldEntity.getX(), oldEntity.getY(), oldEntity.getZ());
                destination.spawnEntity(newEntity);
                oldEntity.setDespawnDelay(0);
            }
        });

        EntityRendererRegistry.register(FISHERMAN, FishermanEntityRenderer::new);
        EntityRendererRegistry.register(CUSTOM_FISHING_BOBBER, CustomFishingBobberEntityRenderer::new);

        //Items
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "custom_fishing_rod"), CUSTOM_FISHING_ROD);
    }
}
