package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.semperidem.fishingclub.network.payload.*;

public class FCNetworking {

    private static void registerPayload() {
        PayloadTypeRegistry.playS2C().register(CoinFlipResultPayload.ID, CoinFlipResultPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(HookLinePayload.ID, HookLinePayload.CODEC);

        PayloadTypeRegistry.playC2S().register(LinePayload.ID, LinePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CoinFlipPayload.ID, CoinFlipPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(TitleClaimPayload.ID, TitleClaimPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SellFishPayload.ID, SellFishPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(FishingCardPayload.ID, FishingCardPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(DialogResponsePayload.ID, DialogResponsePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(DialogPayload.ID, DialogPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CheckoutPayload.ID, CheckoutPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SpellCastWithTargetPayload.ID, SpellCastWithTargetPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SellFishDirectPayload.ID, SellFishDirectPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SummonAcceptPayload.ID, SummonAcceptPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(FishingGameInputPayload.ID, FishingGameInputPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(AddPerkPayload.ID, AddPerkPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(ConfigurationPayload.ID, ConfigurationPayload.CODEC);
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(HookLinePayload.ID, HookLinePayload::consumePayload);
        ClientPlayNetworking.registerGlobalReceiver(CoinFlipResultPayload.ID, CoinFlipResultPayload::consumePayload);
    }

    public static void registerServer() {
        registerPayload();
        ServerPlayNetworking.registerGlobalReceiver(FishingCardPayload.ID, FishingCardPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(AddPerkPayload.ID, AddPerkPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(LinePayload.ID, LinePayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(CoinFlipPayload.ID, CoinFlipPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(TitleClaimPayload.ID, TitleClaimPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(SellFishPayload.ID, SellFishPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(DialogResponsePayload.ID, DialogResponsePayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(DialogPayload.ID, DialogPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(CheckoutPayload.ID, CheckoutPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(SpellCastWithTargetPayload.ID, SpellCastWithTargetPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(SellFishDirectPayload.ID, SellFishDirectPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(SummonAcceptPayload.ID, SummonAcceptPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(FishingGameInputPayload.ID, FishingGameInputPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(ConfigurationPayload.ID, ConfigurationPayload::consumePayload);
    }
}
