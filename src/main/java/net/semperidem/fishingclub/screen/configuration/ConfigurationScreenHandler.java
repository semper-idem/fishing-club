package net.semperidem.fishingclub.screen.configuration;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.fisher.perks.Path;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfigurationComponent;
import net.semperidem.fishingclub.item.fishing_rod.components.RodPartComponent;
import net.semperidem.fishingclub.network.payload.ConfigurationPayload;
import net.semperidem.fishingclub.registry.FCComponents;
import net.semperidem.fishingclub.registry.FCItems;
import net.semperidem.fishingclub.registry.FCScreenHandlers;
import net.semperidem.fishingclub.screen.fishing_card.TabSlot;
import org.jetbrains.annotations.Nullable;

public class ConfigurationScreenHandler extends ScreenHandler {
    private final static int SLOT_COUNT = 5;
    private final static int SLOTS_PER_ROW = 9;
    private final static int SLOT_SIZE = 20;
    private Inventory playerInventory;
    RodConfigurationComponent configuration;

    public ConfigurationScreenHandler(int syncId, PlayerInventory playerInventory, ConfigurationPayload payload) {
        super(FCScreenHandlers.CONFIGURATION_SCREEN, syncId);
        this.playerInventory = playerInventory;
        this.addPlayerInventorySlots();
        this.configuration = playerInventory.getMainHandStack().getOrDefault(FCComponents.ROD_CONFIGURATION, RodConfigurationComponent.DEFAULT);
        SimpleInventory rodInventory = new SimpleInventory(5) {
            @Override
            public void markDirty() {
                super.markDirty();
            }
        };
        rodInventory.setStack(0, this.configuration.coreComponent().partStack().orElse(ItemStack.EMPTY));
        rodInventory.setStack(1, this.configuration.lineComponent().partStack().orElse(ItemStack.EMPTY));
        rodInventory.setStack(2, ItemStack.EMPTY);
        rodInventory.setStack(3, ItemStack.EMPTY);
        rodInventory.setStack(4, ItemStack.EMPTY);;
        addSlot(new PartSlot(rodInventory, 0,83, 46, FCItems.CORE_WOODEN_OAK, coreStack -> {
            if (payload.isMainHand()) {
                playerInventory.getMainHandStack().apply(FCComponents.ROD_CONFIGURATION, RodConfigurationComponent.DEFAULT, o -> o.equipCore(RodPartComponent.of(coreStack)));
            }
        }));
        addSlot(new PartSlot(rodInventory, 1,41, 64, FCItems.LINE_SPIDER, lineStack -> {
            RodPartComponent rodPart = RodPartComponent.of(lineStack);
            this.configuration = this.configuration.equipLine(rodPart);
            if (payload.isMainHand()) {
                playerInventory.getMainHandStack().apply(FCComponents.ROD_CONFIGURATION, RodConfigurationComponent.DEFAULT, o -> o.equipLine(rodPart));
            }
        }));
        addSlot(new PartSlot(rodInventory, 2,147, 84, FCItems.EMPTY_COMPONENT, coreStack -> this.configuration = this.configuration.equipCore(RodPartComponent.of(coreStack))));
        addSlot(new PartSlot(rodInventory, 3,92, 115, FCItems.EMPTY_COMPONENT, coreStack -> this.configuration = this.configuration.equipCore(RodPartComponent.of(coreStack))));
        addSlot(new PartSlot(rodInventory, 4,48, 115, FCItems.EMPTY_COMPONENT, coreStack -> this.configuration = this.configuration.equipCore(RodPartComponent.of(coreStack))));
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
