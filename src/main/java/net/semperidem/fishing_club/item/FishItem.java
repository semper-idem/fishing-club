package net.semperidem.fishing_club.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FishItem extends Item {

	public FishItem(Settings settings) {
		super(settings);
	}

	public static boolean isFish(ItemStack stack) {
		return stack.getItem() instanceof FishItem;
	}
}
