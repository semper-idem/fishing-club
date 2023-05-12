package net.semperidem.fishingclub;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fish.fishingskill.FishingSkill;
import net.semperidem.fishingclub.fish.fishingskill.FishingSkillManager;
import net.semperidem.fishingclub.network.ClientPacketReceiver;
import net.semperidem.fishingclub.network.ServerPacketReceiver;
import net.semperidem.fishingclub.network.ServerPacketSender;

import static net.minecraft.server.command.CommandManager.literal;

public class FishingClub implements ModInitializer {
    public static final String MODID = "fishing-club";

    @Override
    public void onInitialize() {
        ServerPacketReceiver.registerServerPacketHandlers();
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPacketReceiver.registerClientPacketHandlers();
        }

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("grantperks")
                .executes(context -> {
                    // For versions below 1.19, replace "Text.literal" with "new LiteralText".
                    context.getSource().sendMessage(Text.literal("Called /foo with no arguments"));
                    FishingSkill fs = FishingSkillManager.getPlayerFishingSkill(context.getSource().getPlayer().getUuid());
                    fs.grantPerk("all");
                    ServerPacketSender.sendFishingSkillSyncPacket(context.getSource().getPlayer());
                    return 1;
                })));

    }
}
