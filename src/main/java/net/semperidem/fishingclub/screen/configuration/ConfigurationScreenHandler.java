package net.semperidem.fishingclub.screen.configuration;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfigurationComponent;
import net.semperidem.fishingclub.item.fishing_rod.components.RodPartComponent;
import net.semperidem.fishingclub.network.payload.ConfigurationPayload;
import net.semperidem.fishingclub.registry.FCItems;
import net.semperidem.fishingclub.registry.FCScreenHandlers;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static net.semperidem.fishingclub.item.fishing_rod.components.RodConfigurationComponent.*;
import static net.semperidem.fishingclub.registry.FCComponents.*;

public class ConfigurationScreenHandler extends ScreenHandler {
    private final static int SLOTS_PER_ROW = 9;
    private final static int SLOT_SIZE = 20;
    private final PlayerInventory playerInventory;
    RodConfigurationComponent configuration;

    public ConfigurationScreenHandler(int syncId, PlayerInventory playerInventory, ConfigurationPayload payload) {
        super(FCScreenHandlers.CONFIGURATION_SCREEN, syncId);
        this.playerInventory = playerInventory;
        this.addPlayerInventorySlots();
        this.configuration = playerInventory.getMainHandStack().getOrDefault(ROD_CONFIGURATION, DEFAULT);
        SimpleInventory rodInventory = this.configuration.getParts();
        addSlot(new PartSlot(rodInventory, 0,83, 46, FCItems.CORE_WOODEN_OAK, equipPart(this::equipCore)));
        addSlot(new PartSlot(rodInventory, 1,41, 64, FCItems.LINE_SPIDER, equipPart(this::equipLine)));
        addSlot(new PartSlot(rodInventory, 2,147, 84, FCItems.EMPTY_COMPONENT, equipPart(this::equipCore)));
        addSlot(new PartSlot(rodInventory, 3,92, 115, FCItems.EMPTY_COMPONENT, equipPart(this::equipCore)));
        addSlot(new PartSlot(rodInventory, 4,48, 115, FCItems.EMPTY_COMPONENT, equipPart(this::equipCore)));
    }

    public RodConfigurationComponent getConfiguration() {
        return configuration;
    }

    public Consumer<ItemStack> equipPart(Function<ItemStack, UnaryOperator<RodConfigurationComponent>> equipFunction){
        return itemStack -> {
            this.playerInventory.getMainHandStack().apply(ROD_CONFIGURATION, DEFAULT, equipFunction.apply(itemStack));
            this.configuration = this.playerInventory.getMainHandStack().getOrDefault(ROD_CONFIGURATION, DEFAULT);
        };
    }

    public UnaryOperator<RodConfigurationComponent> equipCore(ItemStack stack) {
        return o -> o.equipCore(RodPartComponent.of(stack));
    }
    public UnaryOperator<RodConfigurationComponent> equipLine(ItemStack stack) {
        return o -> o.equipLine(RodPartComponent.of(stack));
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
