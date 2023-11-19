package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartType;

import static net.semperidem.fishingclub.registry.FItemRegistry.CUSTOM_FISHING_ROD;

public class FishingRodSlot extends Slot {
    FisherWorkbenchScreenHandler parent;
    public FishingRodSlot(FisherWorkbenchScreenHandler parent, int x, int y) {
        super(parent.getInventory(), parent.getNextIndex(), x, y);
        this.parent = parent;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isOf(CUSTOM_FISHING_ROD) || stack.isOf(Items.FISHING_ROD);
    }

    @Override
    public void setStack(ItemStack stack) {
        if (stack.isOf(Items.FISHING_ROD)) {//TODO Maybe add prompt to convert to custom rod
            double dmgPercent = stack.getDamage() / (stack.getMaxDamage() * 1f);
            stack = CUSTOM_FISHING_ROD.getDefaultStack();
            stack.setDamage((int) (stack.getMaxDamage() * dmgPercent));
        }

        unPackFishingRod(stack);
        super.setStack(stack);
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        packFishingRod(stack);
        super.onTakeItem(player, stack);
    }

    public void unPackFishingRod(ItemStack stack){
        if (!stack.isOf(CUSTOM_FISHING_ROD)) return;
        for(FishingRodPartType partType : FishingRodPartType.values()) {
            this.inventory.setStack(partType.slotIndex, FishingRodPartController.removePart(stack, partType));
        }
        markDirty();
    }

    private void clearRodNbt(ItemStack stack) {
        if (!stack.isOf(CUSTOM_FISHING_ROD)) return;
        if (!stack.hasNbt()) return;
        NbtCompound stackNbt = stack.getNbt();
        if (!stackNbt.contains("parts")) return;
        stack.getNbt().remove("parts");
    }


    public void packFishingRod(ItemStack rodStack){
        for(FishingRodPartType partType : FishingRodPartType.values()) {
            ItemStack partStack = this.inventory.getStack(partType.slotIndex);
            FishingRodPartController.putPart(rodStack, partStack);
            this.inventory.setStack(partType.slotIndex, ItemStack.EMPTY);
        }
        markDirty();
    }
}
