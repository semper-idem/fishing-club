package net.semperidem.fishingclub.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.semperidem.fishingclub.entity.LineArrowEntity;

public class LineArrowItem extends ArrowItem {
    public LineArrowItem(Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new LineArrowEntity(world, shooter);
    }
}
