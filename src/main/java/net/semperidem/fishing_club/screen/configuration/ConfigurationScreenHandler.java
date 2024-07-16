package net.semperidem.fishing_club.screen.configuration;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishing_club.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishing_club.network.payload.ConfigurationPayload;
import net.semperidem.fishing_club.registry.FCItems;
import net.semperidem.fishing_club.registry.FCScreenHandlers;

import java.util.function.BiConsumer;

import static net.semperidem.fishing_club.registry.FCComponents.*;

public class ConfigurationScreenHandler extends ScreenHandler {
    private final static int SLOTS_PER_ROW = 9;
    private final static int SLOT_SIZE = 20;
    final PlayerInventory playerInventory;
    RodConfiguration configuration;
    ItemStack fishingRod;
    private int rodSlot = 0;

    public ConfigurationScreenHandler(int syncId, PlayerInventory playerInventory, ConfigurationPayload payload) {

        super(FCScreenHandlers.CONFIGURATION_SCREEN, syncId);

        this.playerInventory = playerInventory;
        this.fishingRod = playerInventory.getMainHandStack();
        this.configuration = RodConfiguration.of(this.fishingRod);

        this.addPlayerInventorySlots();
        this.addRodInventorySlots();
    }

    private void addRodInventorySlots() {

        RodInventory rodInventory = this.configuration.getParts(this.playerInventory.player);
        addPartSlot(rodInventory,83, 46, FCItems.CORE_OAK_WOOD);
        addPartSlot(rodInventory,41, 64, FCItems.REEL_WOOD);
        addPartSlot(rodInventory,147, 84, FCItems.LINE_SPIDER);
        addPartSlot(rodInventory,84, 115, FCItems.BOBBER_PLANT_SLIME);
        addPartSlot(rodInventory,64, 115, FCItems.HOOK_IRON);
        addPartSlot(rodInventory,44, 115, FCItems.BAIT_ROTTEN_FLESH);
    }

    private void addPartSlot(RodInventory rodInventory, int x, int y, Item boundItem){

        addSlot(new PartSlot(rodInventory, rodSlot++,x, y, boundItem, equipPart(), this));
    }

    public RodConfiguration getConfiguration() {

        return configuration;
    }

    public BiConsumer<ItemStack, RodConfiguration.PartType> equipPart(){

        return (partStack, partType) -> {

            RodConfiguration rodConfiguration = this.fishingRod.get(ROD_CONFIGURATION);

            if (rodConfiguration == null) {
                return;
            }

            this.configuration = rodConfiguration.equip(partStack, partType);

            this.configuration.calculateRodDurability(this.fishingRod);
            this.fishingRod.set(ROD_CONFIGURATION, this.configuration);
        };
    }

    public void addPlayerInventorySlots(){

        for(int x = 0; x < SLOTS_PER_ROW; ++x) {

            addSlot(new Slot(this.playerInventory, x,3 + x * SLOT_SIZE, 221));
        }
        for(int y = 0; y < 3; ++y) {

            for(int x = 0; x < 9; ++x) {

                addSlot(new Slot(this.playerInventory, x + (y * SLOTS_PER_ROW) + SLOTS_PER_ROW,
                        3 + x * SLOT_SIZE,
                        161 + y * SLOT_SIZE
                ));
            }
        }
    }
    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {

        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {

        return this.fishingRod.isOf(FCItems.MEMBER_FISHING_ROD);
    }

    @Override
    public void onClosed(PlayerEntity player) {

        super.onClosed(player);
    }
}
