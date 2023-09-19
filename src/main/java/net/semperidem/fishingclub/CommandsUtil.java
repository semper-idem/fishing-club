package net.semperidem.fishingclub;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fisher.FishingCardManager;
import net.semperidem.fishingclub.network.ClientPacketSender;

public class CommandsUtil {

    public static void register(){
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
            // Root command
            LiteralArgumentBuilder<ServerCommandSource> rootCommand = CommandManager.literal(FishingClub.MOD_ID);

            rootCommand.then(CommandManager.literal("summon_accept").executes(context -> {
                ClientPacketSender.sendSummonAccept();
                return 1;
            }));

            rootCommand.then(CommandManager.literal("info").executes(context -> {
                context.getSource().sendMessage(Text.literal(FishingCardManager.getPlayerCard(context.getSource().getPlayer()).toString()));
                return 1;
            }));

            rootCommand.then(CommandManager.literal("add")
                    .then(CommandManager.literal("credit")
                            .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                                    .executes(context -> {
                                        int amount = IntegerArgumentType.getInteger(context, "amount");
                                        FishingCardManager.addCredit(context.getSource().getPlayer(), amount);
                                        context.getSource().sendMessage(Text.literal("Added " + amount + " to self credit"));
                                        return 1;
                                    })
                            )
                    )
            );

            rootCommand.then(CommandManager.literal("set")
                    .then(CommandManager.literal("skill_points")
                            .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                                    .executes(context -> {
                                        int amount = IntegerArgumentType.getInteger(context, "amount");
                                        FishingCardManager.setSkillPoint(context.getSource().getPlayer(), amount);
                                        context.getSource().sendMessage(Text.literal("Set available skill points to " + amount));
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
                                        FishingCardManager.addCredit(context.getSource().getPlayer(), -amount);
                                        context.getSource().sendMessage(Text.literal("Removed" + amount + "credit from self"));
                                        return 1;
                                    })
                            )
                    )
                    .then(CommandManager.literal("perk")
                            .then(CommandManager.argument("amount", StringArgumentType.string())
                                    .executes(context -> {
                                        String perkName = StringArgumentType.getString(context, "perkName");
                                        FishingCardManager.removePerk(context.getSource().getPlayer(), perkName);
                                        context.getSource().sendMessage(Text.literal("Removed" + perkName + " perk from self"));
                                        return 1;
                                    })
                            )
                    )
            );

            dispatcher.register(rootCommand);
        });

    }
}
