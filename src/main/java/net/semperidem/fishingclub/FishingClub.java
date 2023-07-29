package net.semperidem.fishingclub;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.item.*;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.semperidem.fishingclub.block.FisherWorkbenchBlock;
import net.semperidem.fishingclub.client.game.fish.FishTypes;
import net.semperidem.fishingclub.client.screen.fisher_info.FisherInfoScreenHandler;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil;
import net.semperidem.fishingclub.client.screen.workbench.FisherWorkbenchScreenHandler;
import net.semperidem.fishingclub.entity.CustomFishingBobberEntity;
import net.semperidem.fishingclub.entity.CustomFishingBobberEntityRenderer;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.FishermanEntityRenderer;
import net.semperidem.fishingclub.item.CustomFishingRod;
import net.semperidem.fishingclub.item.FishingNetItem;
import net.semperidem.fishingclub.item.FishingRodPartItems;
import net.semperidem.fishingclub.network.ClientPacketReceiver;
import net.semperidem.fishingclub.network.ServerPacketReceiver;

import java.util.ArrayList;

public class FishingClub implements ModInitializer {
    public static final String MOD_ID = "fishing-club";
    public static ArrayList<Item> BOATS = new ArrayList<>();

    public static final ItemGroup FISHING_CLUB_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "fishing_club_group"), () -> new ItemStack(Items.COD));
    public static final CustomFishingRod CUSTOM_FISHING_ROD = new CustomFishingRod(new Item.Settings().group(FISHING_CLUB_GROUP).maxCount(1).maxDamage(128));
    public static final Block FISHER_WORKBENCH_BLOCK = new FisherWorkbenchBlock(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE));
    public static final Item FISHER_WORKBENCH_BLOCK_ITEM = new BlockItem(FISHER_WORKBENCH_BLOCK, new Item.Settings().group(FISHING_CLUB_GROUP));
    public static Item FISHING_NET = new FishingNetItem(new Item.Settings().group(FISHING_CLUB_GROUP));
    public static ScreenHandlerType<FisherWorkbenchScreenHandler> FISHER_WORKBENCH_SCREEN_HANDLER;
    public static ScreenHandlerType<FisherInfoScreenHandler> FISHER_INFO_SCREEN;


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
                    .trackRangeBlocks(128)
                    .trackedUpdateRate(5)
                    .build()
    );


    @Override
    public void onInitialize() {

        //Screens
        ShopScreenUtil.register();

        //Fish
        FishTypes.initFishTypes();

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
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "fisher_workbench"), FISHER_WORKBENCH_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "fisher_workbench"), FISHER_WORKBENCH_BLOCK_ITEM);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "fishing_net"), FISHING_NET);


        FISHER_WORKBENCH_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(FishingClub.MOD_ID, "fisher_workbench_gui"), FisherWorkbenchScreenHandler::new);
        FISHER_INFO_SCREEN = ScreenHandlerRegistry.registerExtended(new Identifier(FishingClub.MOD_ID, "fisher_info_screen"), FisherInfoScreenHandler::new);

        FishingRodPartItems.registerParts();
    }
}
