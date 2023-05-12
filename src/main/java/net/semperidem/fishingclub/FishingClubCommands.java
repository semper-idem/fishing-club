package net.semperidem.fishingclub;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fish.fisher.FisherInfo;
import net.semperidem.fishingclub.fish.fisher.FisherInfos;
import net.semperidem.fishingclub.network.ServerPacketSender;

import static net.minecraft.server.command.CommandManager.literal;

public class FishingClubCommands {

    public static void register(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("fisherinfo")
                .executes(context -> {
                    FisherInfo fs = FisherInfos.getPlayerFisherInfo(context.getSource().getPlayer().getUuid());
                    context.getSource().sendMessage(Text.literal(fs.toString()));
                    ServerPacketSender.sendFisherInfoSyncPacket(context.getSource().getPlayer());
                    return 1;
                })));

    }
}
