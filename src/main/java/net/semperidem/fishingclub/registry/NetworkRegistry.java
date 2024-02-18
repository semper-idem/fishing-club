package net.semperidem.fishingclub.registry;

import net.semperidem.fishingclub.network.ClientPacketReceiver;
import net.semperidem.fishingclub.network.ServerPacketReceiver;

public class NetworkRegistry {

    public static void register(){
        ServerPacketReceiver.registerServerPacketHandlers();
    }

    public static void registerClient(){
        ClientPacketReceiver.registerClientPacketHandlers();
    }
}
