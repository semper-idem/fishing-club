package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.item.FishingRodPartItem;
import net.semperidem.fishingclub.registry.FScreenHandlerRegistry;

import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.*;
import static net.semperidem.fishingclub.item.FishingRodPartItem.PartType.*;
import static net.semperidem.fishingclub.registry.FBlockRegistry.FISHER_WORKBENCH_BLOCK;

public class FisherWorkbenchScreenHandler extends ScreenHandler {
    private static final int SLOT_COUNT = 6;

    static final int ROD_SLOT_X = 16;
    static final int ROD_SLOT_Y = 17;
    static final int PART_SLOT_X = 145;
    static final int PART_SLOT_OFFSET = 26; //Space between slots
    static final int BAIT_SLOT_X = 54;

    private final Inventory benchInventory = new SimpleInventory(SLOT_COUNT);
    private final ScreenHandlerContext context;

    public FisherWorkbenchScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, inventory.player.getBlockPos()));
    }
    public FisherWorkbenchScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(FScreenHandlerRegistry.FISHER_WORKBENCH_SCREEN_HANDLER, syncId);
        this.context = context;

        addWorkbenchSlots();
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }


    private void addWorkbenchSlots(){
        addSlot(new FishingRodSlot(this.benchInventory, 0, ROD_SLOT_X, ROD_SLOT_Y));
        addPartSlot(PART_SLOT_X, ROD_SLOT_Y, CORE);
        addPartSlot(PART_SLOT_X, ROD_SLOT_Y + PART_SLOT_OFFSET, BOBBER);
        addPartSlot(PART_SLOT_X, ROD_SLOT_Y + PART_SLOT_OFFSET * 2, LINE);
        addPartSlot(PART_SLOT_X, ROD_SLOT_Y + PART_SLOT_OFFSET * 3, HOOK);
        addPartSlot(BAIT_SLOT_X,  ROD_SLOT_Y + PART_SLOT_OFFSET * 3, BAIT);
    }

    protected Slot addPartSlot(int x, int y, FishingRodPartItem.PartType partType) {
        return super.addSlot(new RodPartSlot(this.benchInventory, partType.slotIndex, x, y, partType));
    }

    private void addPlayerInventory(PlayerInventory playerInventory){
        for(int y = 0; y < PLAYER_INVENTORY_ROWS; ++y) {
            for(int x = 0; x < SLOTS_PER_ROW; ++x) {
                addSlot(new Slot(playerInventory, x + y * SLOTS_PER_ROW + 9, 8 + x * SLOT_SIZE, 140 + y * SLOT_SIZE));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory){
        for(int x = 0; x < SLOTS_PER_ROW; ++x) {
            addSlot(new Slot(playerInventory, x, 8 + x * SLOT_SIZE, 198));
        }
    }

    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStackInSlot = slot.getStack();
            itemStackCopy = itemStackInSlot.copy();
            if (index < SLOT_COUNT) {
                if (index == 0) {
                    ((FishingRodSlot) slot).packFishingRod(itemStackInSlot);
                }
                if (!this.insertItem(itemStackInSlot, SLOT_COUNT, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(itemStackInSlot, 0, SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemStackInSlot.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (itemStackInSlot.getCount() == itemStackCopy.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStackInSlot);
        }
        return itemStackCopy;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return ScreenHandler.canUse(this.context, player, FISHER_WORKBENCH_BLOCK);
    }

    public void close(PlayerEntity player) {
        super.close(player);
        ((FishingRodSlot)this.slots.get(0)).packFishingRod(benchInventory.getStack(0));
        this.context.run((world, pos) -> this.dropInventory(player, this.benchInventory));
    }
}
