package net.semperidem.fishingclub;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fisher.FishingCardManager;
import net.semperidem.fishingclub.network.ClientPacketSender;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandsUtil {
    public static LiteralArgumentBuilder<ServerCommandSource> rootCommand = literal(FishingClub.MOD_ID);
    public static void registerSummonAccept(){
        rootCommand.then(literal("summon_accept").executes(context -> {
            ClientPacketSender.sendSummonAccept();
            return 1;
        }));
    }

    public static void registerInfo(){
        rootCommand.then(literal("info").executes(context -> {
            context.getSource().sendMessage(Text.literal(FishingCardManager.getPlayerCard(context.getSource().getPlayer()).toString()));
            return 1;
        }));
    }

    public static void registerResetSpellCooldown(){
        rootCommand.then(literal("reset_cooldown").executes(context -> {
            FishingCardManager.getPlayerCard(context.getSource().getPlayer()).resetCooldown();
            return 1;
        }));
    }

    private static int addSkillPoints(CommandContext<ServerCommandSource> context, String targetName){
        ServerPlayerEntity target = context.getSource().getServer().getPlayerManager().getPlayer(targetName);
        if (target != null) {
            int amount = getInteger(context, "amount");
            FishingCardManager.addSkillPoint(target, amount);
            context.getSource().sendMessage(Text.literal("Added " + amount + " to " + targetName + " skill points"));
        } else {
            context.getSource().sendMessage(Text.literal("Player " + targetName + " not found"));
        }
        return 1;
    }


    private static int setSkillPoints(CommandContext<ServerCommandSource> context, String targetName){
        ServerPlayerEntity target = context.getSource().getServer().getPlayerManager().getPlayer(targetName);
        if (target != null) {
            int amount = getInteger(context, "amount");
            FishingCardManager.setSkillPoint(target, amount);
            context.getSource().sendMessage(Text.literal("Set available skill points of " + targetName + " to " + amount));
        } else {
            context.getSource().sendMessage(Text.literal("Player " + targetName + " not found"));
        }
        return 1;
    }

    private static int addCredit(CommandContext<ServerCommandSource> context, String targetName){
        ServerPlayerEntity target = context.getSource().getServer().getPlayerManager().getPlayer(targetName);
        if (target != null) {
            int amount = getInteger(context, "amount");
            FishingCardManager.addCredit(target, amount);
            context.getSource().sendMessage(Text.literal("Added " + amount + " to self credit"));
        } else {
            context.getSource().sendMessage(Text.literal("Player " + targetName + " not found"));
        }
        return 1;
    }

    private static int setCredit(CommandContext<ServerCommandSource> context, String targetName){
        ServerPlayerEntity target = context.getSource().getServer().getPlayerManager().getPlayer(targetName);
        if (target != null) {
            int amount = getInteger(context, "amount");
            FishingCardManager.setCredit(target, amount);
            context.getSource().sendMessage(Text.literal("Set available credit of " + targetName + " to " + amount));
        } else {
            context.getSource().sendMessage(Text.literal("Player " + targetName + " not found"));
        }
        return 1;
    }

    public static void registerAdd(){
        rootCommand.then(literal("add")
                .then(literal("credit")
                        .then(argument("amount", integer()).executes(context -> addCredit(context, context.getSource().getName())))
                        .then(argument("target", word()).then(argument("amount", integer()).executes(context -> addCredit(context, getString(context, getString(context, "target"))))))
                ).then(literal("skill_point")
                        .then(argument("amount", integer()).executes(context -> addSkillPoints(context, context.getSource().getName())))
                        .then(argument("target", word()).then(argument("amount", integer()).executes(context -> addSkillPoints(context, getString(context, getString(context, "target"))))))
                )
        );
    }
    public static void registerSet(){
        rootCommand.then(literal("set")
                .then(literal("credit")
                        .then(argument("amount", integer()).executes(context -> addCredit(context, context.getSource().getName())))
                        .then(argument("target", word()).then(argument("amount", integer()).executes(context -> setCredit(context, getString(context, getString(context, "target"))))))
                ).then(literal("skill_point")
                        .then(argument("amount", integer()).executes(context -> addSkillPoints(context, context.getSource().getName())))
                        .then(argument("target", word()).then(argument("amount", integer()).executes(context -> setSkillPoints(context, getString(context, getString(context, "target"))))))
                )
        );
    }

    public static int removePerk(CommandContext<ServerCommandSource> context, String targetName){
        ServerPlayerEntity target = context.getSource().getServer().getPlayerManager().getPlayer(targetName);
        if (target != null) {
            String perkName = getString(context, "perkName");
            FishingCardManager.removePerk(context.getSource().getPlayer(), perkName);
            context.getSource().sendMessage(Text.literal("Removed " + perkName + " from" + targetName));
        } else {
            context.getSource().sendMessage(Text.literal("Player " + targetName + " not found"));
        }
        return 1;
    }

    public static void registerRemovePerk(){
        rootCommand.then(literal("remove_perk")
                        .then(argument("perkName", word()).executes(context -> removePerk(context, context.getSource().getName())))
                        .then(argument("target", word()).then(argument("perkName", word()).executes(context -> removePerk(context, getString(context, getString(context, "target"))))))
        );
    }


    public static void register(){
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
            registerSummonAccept();
            registerInfo();
            registerSummonAccept();
            registerResetSpellCooldown();
            registerAdd();
            registerSet();
            registerRemovePerk();
            dispatcher.register(rootCommand);
        });
    }
}
