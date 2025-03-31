package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.semperidem.fishingclub.network.payload.*;

public class Networking {

    private static void registerPayload() {
        PayloadTypeRegistry.playS2C().register(CoinFlipResultPayload.ID, CoinFlipResultPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(HookLinePayload.ID, HookLinePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(FishingUpdatePayload.ID, FishingUpdatePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(StopPlayingPayload.ID, StopPlayingPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(LinePayload.ID, LinePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CoinFlipPayload.ID, CoinFlipPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(TitleClaimPayload.ID, TitleClaimPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SellFishPayload.ID, SellFishPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CardPayload.ID, CardPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CheckoutPayload.ID, CheckoutPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SpellCastWithTargetPayload.ID, SpellCastWithTargetPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SpellCastPayload.ID, SpellCastPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CardSellFishPayload.ID, CardSellFishPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SummonAcceptPayload.ID, SummonAcceptPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(FishingInputMousePayload.ID, FishingInputMousePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(FishingInputKeyboardPayload.ID, FishingInputKeyboardPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(LearnTradeSecretPayload.ID, LearnTradeSecretPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(RodConfigurationPayload.ID, RodConfigurationPayload.CODEC);
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(HookLinePayload.ID, HookLinePayload::consumePayload);
        ClientPlayNetworking.registerGlobalReceiver(CoinFlipResultPayload.ID, CoinFlipResultPayload::consumePayload);
        ClientPlayNetworking.registerGlobalReceiver(FishingUpdatePayload.ID, FishingUpdatePayload::consumePayload);
        ClientPlayNetworking.registerGlobalReceiver(StopPlayingPayload.ID, StopPlayingPayload::consumePayload);
    }

    public static void registerServer() {
        registerPayload();
        ServerPlayNetworking.registerGlobalReceiver(CardPayload.ID, CardPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(LearnTradeSecretPayload.ID, LearnTradeSecretPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(LinePayload.ID, LinePayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(CoinFlipPayload.ID, CoinFlipPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(TitleClaimPayload.ID, TitleClaimPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(SellFishPayload.ID, SellFishPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(CheckoutPayload.ID, CheckoutPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(SpellCastPayload.ID, SpellCastPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(SpellCastWithTargetPayload.ID, SpellCastWithTargetPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(CardSellFishPayload.ID, CardSellFishPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(SummonAcceptPayload.ID, SummonAcceptPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(FishingInputMousePayload.ID, FishingInputMousePayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(FishingInputKeyboardPayload.ID, FishingInputKeyboardPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(RodConfigurationPayload.ID, RodConfigurationPayload::consumePayload);
    }
}
