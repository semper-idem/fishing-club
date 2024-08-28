package net.semperidem.fishingclub.screen.configuration;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.item.fishing_rod.components.CorePartItem;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;

import java.util.function.BiConsumer;

public class PartSlot extends Slot {
    private final Item boundItem;
    private final BiConsumer<ItemStack, RodConfiguration.PartType> equipPartAction;
    public PartSlot(RodInventory inventory, int index, int x, int y, Item boundItem, BiConsumer<ItemStack, RodConfiguration.PartType> equipPartAction) {
        super(inventory, index, x, y);
        this.boundItem = boundItem;
        this.equipPartAction = equipPartAction;
    }

    @Override
    public void setStack(ItemStack stack, ItemStack previousStack) {
        RodConfiguration.PartType partType = stack.isEmpty() ? RodConfiguration.PartType.of(previousStack) : RodConfiguration.PartType.of(stack);
        equipPartAction.accept(stack, partType);
        super.setStack(stack, previousStack);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return !(boundItem instanceof CorePartItem) && boundItem.getClass().isAssignableFrom(stack.getItem().getClass());
    }
}
