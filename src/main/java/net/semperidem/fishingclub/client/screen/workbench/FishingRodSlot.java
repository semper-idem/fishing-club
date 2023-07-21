package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.item.FishingRodPartItem;

import static net.semperidem.fishingclub.FishingClub.CUSTOM_FISHING_ROD;

public class FishingRodSlot extends Slot {
    public FishingRodSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isOf(CUSTOM_FISHING_ROD) || stack.isOf(Items.FISHING_ROD);//TODO Add vanilla fishing rod transformmation
    }

    @Override
    public void setStack(ItemStack stack) {
        if (stack.isOf(Items.FISHING_ROD)) {
            stack = CUSTOM_FISHING_ROD.getDefaultStack();
        }
        unPackFishingRod(stack);
        super.setStack(stack);
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        packFishingRod(stack);
        super.onTakeItem(player, stack);
    }

    private void unPackFishingRod(ItemStack stack){
        if (stack.isOf(CUSTOM_FISHING_ROD)) {
            for(FishingRodPartItem.PartType partType : FishingRodPartItem.PartType.values()) {
                this.inventory.setStack(partType.slotIndex, CUSTOM_FISHING_ROD.getPart(stack, partType));
            }
            clearRodNbt(stack);
            markDirty();
        }
    }

    private void clearRodNbt(ItemStack rodStack) {
        if (rodStack.isOf(CUSTOM_FISHING_ROD)) {
            if (rodStack.hasNbt()) {
                NbtCompound stackNbt = rodStack.getNbt();
                if (stackNbt.contains("parts")) {
                    rodStack.getNbt().remove("parts");
                }
            }
        }
    }


    public void packFishingRod(ItemStack rodStack){
        for(FishingRodPartItem.PartType partType : FishingRodPartItem.PartType.values()) {
            ItemStack partStack = this.inventory.getStack(partType.slotIndex);
            if (!partStack.isEmpty()) {
                CUSTOM_FISHING_ROD.addPart(rodStack,this.inventory.getStack(partType.slotIndex), partType);
            }
            this.inventory.setStack(partType.slotIndex, ItemStack.EMPTY);
        }
        markDirty();
    }
}
