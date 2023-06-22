package net.semperidem.fishingclub;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fisher.FisherInfos;

import java.util.UUID;

public class FishingClubCommands {

    public static void register(){
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
            // Root command
            LiteralArgumentBuilder<ServerCommandSource> rootCommand = CommandManager.literal(FishingClub.MOD_ID);

            rootCommand.then(CommandManager.literal("info").executes(context -> {
                context.getSource().sendMessage(Text.literal(FisherInfos.getClientInfo().toString()));
                return 1;
            }));

            rootCommand.then(CommandManager.literal("add")
                    .then(CommandManager.literal("credit")
                            .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                                    .executes(context -> {
                                        int amount = IntegerArgumentType.getInteger(context, "amount");
                                        UUID uuid = context.getSource().getPlayer().getUuid();
                                        FisherInfos.addCredit(uuid, amount);
                                        context.getSource().sendMessage(Text.literal("Added " + amount + " to self credit"));

                                        return 1;
                                    })
                            )
                    )
            );

            rootCommand.then(CommandManager.literal("remove")
                    .then(CommandManager.literal("credit")
                            .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                                    .executes(context -> {
                                        int amount = IntegerArgumentType.getInteger(context, "amount");
                                        UUID uuid = context.getSource().getPlayer().getUuid();
                                        String response = FisherInfos.removeCredit(uuid, amount) ?
                                                "Removed" + amount + "credit from self" :
                                                "Tried to remove too high amount";

                                                context.getSource().sendMessage(Text.literal(response));

                                        return 1;
                                    })
                            )
                    )
            );
            rootCommand.then(CommandManager.literal("zero")
                    .then(CommandManager.literal("credit")
                                    .executes(context -> {
                                        UUID uuid = context.getSource().getPlayer().getUuid();
                                        FisherInfos.zeroCredit(uuid);
                                        context.getSource().sendMessage(Text.literal("Zeroed self credit"));

                                        return 1;
                                    })
                    )
            );

            dispatcher.register(rootCommand);
        });

    }
}
