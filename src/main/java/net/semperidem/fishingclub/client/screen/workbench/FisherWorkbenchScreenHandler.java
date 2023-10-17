package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingCardManager;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.registry.FScreenHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOTS_PER_ROW;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOT_SIZE;
import static net.semperidem.fishingclub.item.FishingRodPartItem.PartType.*;
import static net.semperidem.fishingclub.registry.FBlockRegistry.FISHER_WORKBENCH_BLOCK;

public class FisherWorkbenchScreenHandler extends ScreenHandler {
    private static final int SLOT_COUNT = 6;
    private final Inventory benchInventory;
    private final ScreenHandlerContext context;
    private FisherWorkbenchScreen screen;
    private FishingCard fishingCard;
    private List<Slot> nonRodSlots = new ArrayList<>();

    public FisherWorkbenchScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, inventory.player.getBlockPos()));
    }
    public FisherWorkbenchScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(FScreenHandlerRegistry.FISHER_WORKBENCH_SCREEN_HANDLER, syncId);
        this.context = context;
        this.benchInventory = new SimpleInventory(6);
        this.fishingCard = FishingCardManager.getPlayerCard((ServerPlayerEntity) playerInventory.player);
        initSlots();
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    public void setRepairMode(boolean repairRequired){
        screen.changeRepairMode(repairRequired);
        if (repairRequired) {
            initRepairSlots();
        } else {
            initDefaultSlots();
        }
    }

    public void  repairRod(){
       Slot repairMatSlot = this.slots.get(4);
       ItemStack repairMatStack = repairMatSlot.getStack();
       if (repairMatStack.getCount() <= 0) return;
       ItemStack rodStack = this.slots.get(0).getStack();
       float damagePercent = rodStack.getDamage() / (rodStack.getMaxDamage() * 1f);
       int matToConsume = 1 + (int) (damagePercent / 0.2f);
       int matAvailable = repairMatStack.getCount();
       int matConsumed = Math.min(matAvailable, matToConsume);
       int damageFixed = (int) (0.2f * matConsumed * rodStack.getMaxDamage());
       int resultDamage = Math.max(0, rodStack.getDamage() - damageFixed);
       rodStack.setDamage(resultDamage);
       repairMatStack.setCount(matAvailable - matConsumed);
        if (this.fishingCard.hasPerk(FishingPerks.DURABLE_RODS)) {
            NbtCompound rodNbt = rodStack.getOrCreateNbt();
            rodNbt.putInt("handmade", damageFixed);
            rodStack.setNbt(rodNbt);
        }
    }

    @Override
    protected Slot addSlot(Slot slot) {
        if (!(slot instanceof FishingRodSlot)) {
            nonRodSlots.add(slot);
        }
        return super.addSlot(slot);
    }

    public void setScreenCallback(FisherWorkbenchScreen screen){
        this.screen = screen;
    }
    private void addRodPartSlots(){
        addSlot(new RodPartSlot(this.benchInventory, 1, 145, 17, CORE));
        addSlot(new RodPartSlot(this.benchInventory, 2, 145, 17 + 26,  BOBBER));
        addSlot(new RodPartSlot(this.benchInventory, 3, 145, 17 + 26 * 2, LINE));
        addSlot(new RodPartSlot(this.benchInventory, 4, 145, 17 + 26 * 3, HOOK));
        addSlot(new RodPartSlot(this.benchInventory, 5, 54, 17 + 26 * 3, BAIT));
    }

    private void initSlots(){
        addRodSlot();
        if (screen.repairMode) {
            initRepairSlots();
        } else {
            initDefaultSlots();
        }
    }
    private void initDefaultSlots(){
        clearNoneRodSlots();
        addRodPartSlots();
    }

    private void initRepairSlots(){
        clearNoneRodSlots();
        addRepairMaterialSlot();
    }

    private void clearNoneRodSlots(){
        this.slots.removeAll(nonRodSlots);
        nonRodSlots.clear();
    }
    private void addRodSlot(){
        addSlot(new FishingRodSlot(this.benchInventory, 0, 16, 17, this));
    }

    private void addRepairMaterialSlot(){
        addSlot(new Slot(this.benchInventory, 4, 145, 17 + 26 * 3){
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() == Items.STRING && super.canInsert(stack);
            }
        });

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
