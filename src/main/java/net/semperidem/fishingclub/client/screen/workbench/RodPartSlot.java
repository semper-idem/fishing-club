package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.item.CustomFishingRod;
import net.semperidem.fishingclub.item.FishingRodPartItem;

import java.util.Objects;

public class RodPartSlot extends Slot {
    FishingRodPartItem.PartType partType;
    int index;

    public RodPartSlot(Inventory inventory, int index, int x, int y, FishingRodPartItem.PartType partType) {
        super(inventory, index, x, y);
        this.partType = partType;
        this.index = index;
    }


    @Override
    public boolean canInsert(ItemStack itemStack) {
        return !this.inventory.getStack(0).isEmpty()
                && itemStack.getItem() instanceof FishingRodPartItem
                && ((FishingRodPartItem) itemStack.getItem()).getPartType() == partType;
    }

    @Override
    public void setStack(ItemStack stack) {
        ItemStack rodStack = this.inventory.getStack(0);
        if (!stack.isOf(Items.AIR)){
            NbtCompound rodNbt = rodStack.getNbt();
            NbtCompound partsNbt = rodNbt.getCompound("parts");
            if (partsNbt.contains(partType.name())) {
                NbtCompound partNbt = partsNbt.getCompound(partType.name());
                if (partNbt.contains("key")) {
                    String rodPartKey = partNbt.getString("key");
                    if (stack.hasNbt()) {
                        NbtCompound stackNbt = stack.getNbt();
                        if (stackNbt.contains("key")) {
                            String stackKey = stackNbt.getString("key");
                            if (Objects.equals(stackKey, rodPartKey)) {
                                return;
                            }
                        }
                    }
                }
            } else {
                CustomFishingRod rodItem = FishingClub.CUSTOM_FISHING_ROD;
                rodItem.addPart(rodStack, stack, partType);
            }
        } else {
            CustomFishingRod rodItem = FishingClub.CUSTOM_FISHING_ROD;
            rodItem.removePart(rodStack, partType);
        }
        this.inventory.setStack(this.index, stack);
        this.markDirty();
    }


}
