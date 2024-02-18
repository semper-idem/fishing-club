package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartItem;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartType;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodUtil;

import static net.semperidem.fishingclub.registry.ItemRegistry.CUSTOM_FISHING_ROD;

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
            stack = convertVanillaRod(stack);
        }

        unPackFishingRod(stack);
        super.setStack(stack);
    }

    private ItemStack convertVanillaRod(ItemStack rodToConvert){
            double dmgPercent = rodToConvert.getDamage() / (rodToConvert.getMaxDamage() * 1f);
            ItemStack convertedRod = FishingRodUtil.getBasicRod();
            convertedRod.setDamage((int) (rodToConvert.getMaxDamage() * dmgPercent));
            return convertedRod;
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        packFishingRod(stack);
        super.onTakeItem(player, stack);
    }

    public void unPackFishingRod(ItemStack stack){
        for(ItemStack partStack : FishingRodPartController.takeParts(stack)) {
            this.inventory.setStack(((FishingRodPartItem)partStack.getItem()).getPartType().slotIndex, partStack);
        }
        markDirty();
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
