package net.semperidem.fishingclub.screen.configuration;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.item.fishing_rod.components.PartItem;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishingclub.network.payload.RodConfigurationPayload;
import net.semperidem.fishingclub.registry.Items;
import net.semperidem.fishingclub.registry.ScreenHandlers;
import net.semperidem.fishingclub.registry.Tags;

import java.util.function.Consumer;

import static net.semperidem.fishingclub.registry.Components.*;

public class RodConfigurationScreenHandler extends ScreenHandler {
    private final static int SLOTS_PER_ROW = 9;
    private final static int SLOT_SIZE = 20;
    final PlayerInventory playerInventory;
    RodConfiguration configuration;
    ItemStack core;
    private int rodSlot = 0;

    public RodConfigurationScreenHandler(int syncId, PlayerInventory playerInventory, RodConfigurationPayload payload) {

        super(ScreenHandlers.CONFIGURATION_SCREEN, syncId);

        this.playerInventory = playerInventory;
        this.core = playerInventory.getMainStacks().getFirst();
        this.configuration = RodConfiguration.of(this.core);

        this.addPlayerInventorySlots();
        this.addRodInventorySlots();
    }

    private void addRodInventorySlots() {

        RodInventory rodInventory = this.configuration.inventory(this.playerInventory.player);
        addPartSlot(rodInventory,41, 64, (PartItem) Items.REEL_WOOD);
        addPartSlot(rodInventory,147, 84, (PartItem) Items.LINE_SPIDER);
        addPartSlot(rodInventory,84, 115, (PartItem) Items.BOBBER_PLANT_SLIME);
        addPartSlot(rodInventory,64, 115, (PartItem) Items.HOOK_IRON);
        addPartSlot(rodInventory,44, 115, (PartItem) Items.BAIT_ROTTEN_FLESH);
    }

    private void addPartSlot(RodInventory rodInventory, int x, int y, PartItem boundItem){

        addSlot(new PartSlot(rodInventory, rodSlot++,x, y, boundItem, equipPart(boundItem.type())));
    }

    public RodConfiguration getConfiguration() {

        return configuration;
    }

    public Consumer<ItemStack> equipPart(RodConfiguration.PartType partType){

        return (partStack) -> {

            RodConfiguration rodConfiguration = this.core.get(ROD_CONFIGURATION);

            if (rodConfiguration == null) {
                this.configuration = RodConfiguration.EMPTY;
            }
            this.configuration = partStack.isEmpty() ? this.configuration.unEquip(partType) : this.configuration.equip(partStack);

            this.core.set(ROD_CONFIGURATION, this.configuration);
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

        return this.core.isIn(Tags.CORE);
    }

    @Override
    public void onClosed(PlayerEntity player) {

        super.onClosed(player);
    }
}
