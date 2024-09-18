package net.semperidem.fishingclub.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.semperidem.fishingclub.client.screen.hud.TradeSecretCastScreen;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecret;
import net.semperidem.fishingclub.registry.FCComponents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FishItem extends Item {
    public FishItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.isSneaking()) {
            return super.use(world, user, hand);
        }
        boolean canCast = user.getMainHandStack().getOrDefault(FCComponents.SPECIMEN, SpecimenData.DEFAULT).quality() >= 4;
        if (!canCast) {
            return super.use(world, user, hand);
        }
        List<TradeSecret.Instance> usableTradeSecrets = FishingCard.of(user)
                .tradeSecrets()
                .stream()
                .filter(tradeSecret -> tradeSecret.root().hasActive())
                .toList();

        if (usableTradeSecrets.isEmpty()) {
            return super.use(world, user, hand);
        }
        if (world.isClient) {
            MinecraftClient.getInstance().setScreen(new TradeSecretCastScreen(usableTradeSecrets));
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
