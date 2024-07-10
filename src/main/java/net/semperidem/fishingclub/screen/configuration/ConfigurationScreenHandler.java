package net.semperidem.fishingclub.screen.configuration;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishingclub.network.payload.ConfigurationPayload;
import net.semperidem.fishingclub.registry.FCItems;
import net.semperidem.fishingclub.registry.FCScreenHandlers;

import java.util.function.Consumer;

import static net.semperidem.fishingclub.registry.FCComponents.*;

public class ConfigurationScreenHandler extends ScreenHandler {
    private final static int SLOTS_PER_ROW = 9;
    private final static int SLOT_SIZE = 20;
    private final PlayerInventory playerInventory;
    RodConfiguration configuration;

    public ConfigurationScreenHandler(int syncId, PlayerInventory playerInventory, ConfigurationPayload payload) {
        super(FCScreenHandlers.CONFIGURATION_SCREEN, syncId);
        this.playerInventory = playerInventory;
        this.addPlayerInventorySlots();
        this.configuration = RodConfiguration.of(playerInventory.getMainHandStack());
        SimpleInventory rodInventory = this.configuration.getParts();
        addSlot(new PartSlot(rodInventory, 0,83, 46, FCItems.CORE_WOODEN_OAK, equipPart()));
        addSlot(new PartSlot(rodInventory, 1,41, 64, FCItems.EMPTY_COMPONENT, equipPart()));
        addSlot(new PartSlot(rodInventory, 2,147, 84, FCItems.LINE_SPIDER, equipPart()));
        addSlot(new PartSlot(rodInventory, 3,92, 115, FCItems.EMPTY_COMPONENT, equipPart()));
        addSlot(new PartSlot(rodInventory, 4,48, 115, FCItems.EMPTY_COMPONENT, equipPart()));
    }

    public RodConfiguration getConfiguration() {
        return configuration;
    }

    public Consumer<ItemStack> equipPart(){
        return partStack -> {
            this.playerInventory.getMainHandStack().apply(ROD_CONFIGURATION, RodConfiguration.getDefault(), configurationComponent -> configurationComponent.equip(partStack));
            this.configuration = RodConfiguration.of(this.playerInventory.getMainHandStack());
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
        return true;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
    }
}
