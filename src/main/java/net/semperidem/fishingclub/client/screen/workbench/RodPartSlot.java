package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.item.MemberFishingRodItem;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartItem;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartType;
import net.semperidem.fishingclub.registry.FItemRegistry;

import java.util.Objects;

public class RodPartSlot extends Slot {
    FishingRodPartType partType;
    FisherWorkbenchScreenHandler parent;

    public RodPartSlot(FisherWorkbenchScreenHandler parent, FishingRodPartType partType, int x, int y){
        super(parent.getInventory(), parent.getNextIndex(), x, y);
        this.partType = partType;
        this.parent = parent;
    }


    @Override
    public boolean isEnabled() {
        return parent.isSlotEnabled(this);
    }


    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        if (inventory.getStack(getIndex()).getItem() instanceof FishingRodPartItem rodPart) {
            boolean isRodPartCore = rodPart.getPartType() == FishingRodPartType.CORE;
            if (isRodPartCore) {
                return !inventory.getStack(0).isDamaged();
            }
        }
        return true;
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
            NbtCompound partsNbt = rodNbt.getCompound("parts");//TODO CONVERT NBT TO OBJECTS FOR CLEAR CODE
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
                MemberFishingRodItem rodItem = FItemRegistry.CUSTOM_FISHING_ROD;
                rodItem.addPart(rodStack, stack, partType);
            }
        } else {
            MemberFishingRodItem rodItem = FItemRegistry.CUSTOM_FISHING_ROD;
            rodItem.removePart(rodStack, partType);
        }
        this.inventory.setStack(getIndex(), stack);
        this.markDirty();
    }


}
