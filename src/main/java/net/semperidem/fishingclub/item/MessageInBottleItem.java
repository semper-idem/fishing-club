package net.semperidem.fishingclub.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.semperidem.fishingclub.fisher.Card;


public class MessageInBottleItem extends Item {


    public MessageInBottleItem(Settings settings) {
        super(settings);
    }


    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack messageInBottleStack = user.getStackInHand(hand);
        if (messageInBottleStack.getItem() != this) {
            return super.use(world, user, hand);
        }
        Card.of(user).hearMessage();
        messageInBottleStack.setCount(messageInBottleStack.getCount() - 1);
        return super.use(world, user, hand);
    }
}
