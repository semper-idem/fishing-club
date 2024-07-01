package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.semperidem.fishingclub.network.payload.*;

public class NetworkRegistry {

    private static void registerPayload() {
        PayloadTypeRegistry.playS2C().register(HookPayload.ID, HookPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CoinFlipResultPayload.ID, CoinFlipResultPayload.CODEC);

       //PayloadTypeRegistry.playC2S().register(HookClientPayload.ID, HookClientPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CoinFlipPayload.ID, CoinFlipPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(TitleClaimPayload.ID, TitleClaimPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SellFishPayload.ID, SellFishPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(FishingCardPayload.ID, FishingCardPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(DialogResponsePayload.ID, DialogResponsePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(DialogPayload.ID, DialogPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CheckoutPayload.ID, CheckoutPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SpellCastPayload.ID, SpellCastPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SellFishDirectPayload.ID, SellFishDirectPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SummonAcceptPayload.ID, SummonAcceptPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(FishingGameInputPayload.ID, FishingGameInputPayload.CODEC);
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(HookPayload.ID, HookPayload::consumePayload);
        ClientPlayNetworking.registerGlobalReceiver(CoinFlipResultPayload.ID, CoinFlipResultPayload::consumePayload);
    }

    public static void registerServer() {
        registerPayload();
        ServerPlayNetworking.registerGlobalReceiver(FishingCardPayload.ID, FishingCardPayload::consumePayload);
      //  ServerPlayNetworking.registerGlobalReceiver(HookClientPayload.ID, HookClientPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(CoinFlipPayload.ID, CoinFlipPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(TitleClaimPayload.ID, TitleClaimPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(SellFishPayload.ID, SellFishPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(DialogResponsePayload.ID, DialogResponsePayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(DialogPayload.ID, DialogPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(CheckoutPayload.ID, CheckoutPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(SpellCastPayload.ID, SpellCastPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(SellFishDirectPayload.ID, SellFishDirectPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(SummonAcceptPayload.ID, SummonAcceptPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(FishingGameInputPayload.ID, FishingGameInputPayload::consumePayload);
    }
}
