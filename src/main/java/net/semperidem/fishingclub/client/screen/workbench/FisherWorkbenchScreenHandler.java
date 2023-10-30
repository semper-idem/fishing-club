package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingCardManager;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartType;
import net.semperidem.fishingclub.registry.FItemRegistry;
import net.semperidem.fishingclub.registry.FScreenHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOTS_PER_ROW;
import static net.semperidem.fishingclub.item.fishing_rod.FishingRodPartType.*;
import static net.semperidem.fishingclub.registry.FBlockRegistry.FISHER_WORKBENCH_BLOCK;

public class FisherWorkbenchScreenHandler extends ScreenHandler {

    static final int ROD_X = 16;
    static final int ROD_Y = 17;
    static final int SLOT_SIZE = 18;
    static final int SLOT_SPACING = 8;
    static final int SLOT_OFFSET = SLOT_SIZE + SLOT_SPACING;
    static final int SLOTS_X = 145;
    static final int SLOTS_X_BAIT = 54;
    static final int SLOTS_Y = ROD_Y;

    private static final int SLOT_COUNT = 7;
    private int repairSlotIndex;
    private final Inventory benchInventory;
    private final ScreenHandlerContext context;
    private FishingCard fishingCard;
    List<Slot> enabledSlots = new ArrayList<>();
    List<Slot> repairSlots = new ArrayList<>();
    List<Slot> standardSlots = new ArrayList<>();

    public FisherWorkbenchScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, inventory.player.getBlockPos()));
    }

    public FisherWorkbenchScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(FScreenHandlerRegistry.FISHER_WORKBENCH_SCREEN_HANDLER, syncId);
        this.context = context;
        this.benchInventory = new SimpleInventory(SLOT_COUNT);
        this.fishingCard = FishingCardManager.getPlayerCard(playerInventory.player);
        addSlots();
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

    }

    public int getNextIndex() {
        return this.slots.size();
    }

    public Inventory getInventory() {
        return benchInventory;
    }

    boolean isSlotEnabled(Slot slot) {
        return enabledSlots.contains(slot);
    }

    public boolean isRepairPossible(){
        return this.slots.get(repairSlotIndex).getStack().getCount() > 0 && this.slots.get(0).getStack().isDamaged();
    }

    private void addRodSlot(){
        addSlot(new FishingRodSlot(this, ROD_X, ROD_Y));
    }


    private void addStandardSlot(FishingRodPartType partType, int x, int y){
        RodPartSlot slot = new RodPartSlot(this, partType, x,y);
        standardSlots.add(slot);
        addSlot(slot);
    }

    private void addStandardSlots(){
        addStandardSlot(CORE, SLOTS_X, SLOTS_Y);
        addStandardSlot(BOBBER, SLOTS_X, SLOTS_Y + SLOT_OFFSET);
        addStandardSlot(LINE, SLOTS_X, SLOTS_Y + SLOT_OFFSET * 2);
        addStandardSlot(HOOK, SLOTS_X, SLOTS_Y + SLOT_OFFSET * 3);
        addStandardSlot(BAIT, SLOTS_X_BAIT, SLOTS_Y + SLOT_OFFSET * 3);
    }

    private void addRepairSlot(){
        repairSlotIndex = getNextIndex();
        RepairMaterialSlot repairMaterialSlot = new RepairMaterialSlot(this.benchInventory, repairSlotIndex, SLOTS_X, SLOTS_Y + SLOT_OFFSET * 3, this);
        addSlot(repairMaterialSlot);
        repairSlots.add(repairMaterialSlot);
    }



    private void addSlots(){
        addRodSlot();
        addStandardSlots();
        addRepairSlot();
        activateStandardSlots();
    }

    public void activateRepairSlots(){
        enabledSlots = repairSlots;

    }

    public void activateStandardSlots(){
        enabledSlots = standardSlots;
    }

    private void addPlayerInventory(PlayerInventory playerInventory){//TODO Extract all methods related to player inventory to a separate class and extend it
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
            if (index < enabledSlots.size()) {
                if (index == 0) {
                    ((FishingRodSlot) slot).packFishingRod(itemStackInSlot);
                }
                if (!this.insertItem(itemStackInSlot, enabledSlots.size(), this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(itemStackInSlot, 0, enabledSlots.size(), false)) {
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

    public void  repairRod(){
        Slot repairMatSlot = this.slots.get(repairSlotIndex);
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
        FItemRegistry.CUSTOM_FISHING_ROD.getPart(rodStack, CORE).setDamage(resultDamage);
        repairMatStack.setCount(matAvailable - matConsumed);
        if (this.fishingCard.hasPerk(FishingPerks.DURABLE_RODS)) {
            NbtCompound rodNbt = rodStack.getOrCreateNbt();
            rodNbt.putInt("handmade", damageFixed);//TODO MOVE ALL HANDMADE LOGIC INTO ONE PLACE
            rodStack.setNbt(rodNbt);
        }
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
