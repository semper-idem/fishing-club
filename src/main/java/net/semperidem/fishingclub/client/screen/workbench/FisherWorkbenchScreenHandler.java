package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.semperidem.fishingclub.FishingClub;

import static net.semperidem.fishingclub.FishingClub.FISHER_WORKBENCH_BLOCK;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOTS_PER_ROW;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOT_SIZE;
import static net.semperidem.fishingclub.item.FishingRodPartItem.PartType.*;

public class FisherWorkbenchScreenHandler extends ScreenHandler {
    private static final int SLOT_COUNT = 6;
    private final Inventory benchInventory;
    private final ScreenHandlerContext context;
    private final BlockPos blockPos;

    public FisherWorkbenchScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, inventory.player.getBlockPos()), inventory.player.getBlockPos());
    }
    public FisherWorkbenchScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, BlockPos blockPos) {
        super(FishingClub.FISHER_WORKBENCH_SCREEN_HANDLER, syncId);
        this.context = context;
        this.blockPos = blockPos;
        this.benchInventory = new SimpleInventory(6);
        addRodPartSlots();
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    private void addRodPartSlots(){
        addSlot(new FishingRodSlot(this.benchInventory, 0, 16, 17));

        addSlot(new RodPartSlot(this.benchInventory, 1, 145, 17, CORE));
        addSlot(new RodPartSlot(this.benchInventory, 2, 145, 17 + 26,  BOBBER));
        addSlot(new RodPartSlot(this.benchInventory, 3, 145, 17 + 26 * 2, LINE));
        addSlot(new RodPartSlot(this.benchInventory, 4, 145, 17 + 26 * 3, HOOK));
        addSlot(new RodPartSlot(this.benchInventory, 5, 54, 17 + 26 * 3, BAIT));
    }

    private void addPlayerInventory(PlayerInventory playerInventory){
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
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
