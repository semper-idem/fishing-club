package net.semperidem.fishingclub.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.semperidem.fishingclub.client.screen.hud.CastScreen;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecret;
import net.semperidem.fishingclub.registry.Components;

import java.util.List;
import java.util.Optional;

public class FishItem extends Item {
    public FishItem(Settings settings) {
        super(settings);
    }


    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        //todo moved from lore component
        return super.getTooltipData(stack);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.isSneaking()) {
            return super.use(world, user, hand);
        }
        boolean canCast = user.getMainHandStack().getOrDefault(Components.SPECIMEN, SpecimenData.DEFAULT).quality() >= 4;
        if (!canCast) {
            return super.use(world, user, hand);
        }
        List<TradeSecret.Instance> usableTradeSecrets = Card.of(user)
                .tradeSecrets()
                .stream()
                .filter(tradeSecret -> tradeSecret.root().hasActive())
                .toList();

        if (usableTradeSecrets.isEmpty()) {
            return super.use(world, user, hand);
        }
        if (world.isClient) {
            MinecraftClient.getInstance().setScreen(new CastScreen(usableTradeSecrets));
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
