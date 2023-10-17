package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.item.FishingRodPartItem;

import static net.semperidem.fishingclub.registry.FItemRegistry.CUSTOM_FISHING_ROD;

public class FishingRodSlot extends Slot {
    FisherWorkbenchScreenHandler fisherWorkbenchScreenHandler;
    public FishingRodSlot(Inventory inventory, int index, int x, int y, FisherWorkbenchScreenHandler fisherWorkbenchScreenHandler) {
        super(inventory, index, x, y);
        this.fisherWorkbenchScreenHandler = fisherWorkbenchScreenHandler;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isOf(CUSTOM_FISHING_ROD) || stack.isOf(Items.FISHING_ROD);
    }

    @Override
    public void setStack(ItemStack stack) {
        if (stack.isOf(Items.FISHING_ROD)) {
            double dmgPercent = stack.getDamage() / (stack.getMaxDamage() * 1f);
            stack = CUSTOM_FISHING_ROD.getDefaultStack();
            stack.setDamage((int) (stack.getMaxDamage() * dmgPercent));
        }
        boolean repairRequired = stack.getDamage() > 0;
        if (repairRequired) {
            fisherWorkbenchScreenHandler.setRepairMode(true);
        } else {
            unPackFishingRod(stack);
        }
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
