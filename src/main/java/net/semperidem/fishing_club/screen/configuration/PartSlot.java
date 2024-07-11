package net.semperidem.fishing_club.screen.configuration;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishing_club.item.fishing_rod.components.CorePartItem;
import net.semperidem.fishing_club.item.fishing_rod.components.RodConfiguration;

import java.util.function.BiConsumer;

public class PartSlot extends Slot {
    private final Item boundItem;
    private final BiConsumer<ItemStack, RodConfiguration.PartType> equipPartAction;
    private final ConfigurationScreenHandler parent;
    public PartSlot(RodInventory inventory, int index, int x, int y, Item boundItem, BiConsumer<ItemStack, RodConfiguration.PartType> equipPartAction, ConfigurationScreenHandler parent) {
        super(inventory, index, x, y);
        this.boundItem = boundItem;
        this.parent = parent;
        this.equipPartAction = equipPartAction;
    }

    @Override
    public void setStack(ItemStack stack, ItemStack previousStack) {
        if (previousStack.getItem() instanceof CorePartItem coreItem) {
            this.parent.configuration.line().ifPresent(this.parent.playerInventory::insertStack);
            this.parent.fishingRod.setCount(0);
        }
        RodConfiguration.PartType partType = stack.isEmpty() ? RodConfiguration.PartType.of(previousStack) : RodConfiguration.PartType.of(stack);
        equipPartAction.accept(stack, partType);
        super.setStack(stack, previousStack);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return !(boundItem instanceof CorePartItem) && boundItem.getClass().isAssignableFrom(stack.getItem().getClass());
    }

//    @Override
//    public boolean canTakeItems(PlayerEntity playerEntity) {
//        return !(boundItem instanceof CorePartItem);
//    }
}
