package net.semperidem.fishingclub.registry;

public class FRegistry {

    public static void register(){
        FBlockRegistry.register();
        FItemRegistry.register();
        FEntityRegistry.register();
        FNetworkRegistry.register();
        FScreenHandlerRegistry.register();
        FStatusEffectRegistry.register();
    }

    public static void registerClient(){
        FNetworkRegistry.registerClient();
        FScreenHandlerRegistry.registerClient();
        FKeybindingRegistry.registerClient();
        FModelPredicateProviderRegistry.registerClient();
    }
}
