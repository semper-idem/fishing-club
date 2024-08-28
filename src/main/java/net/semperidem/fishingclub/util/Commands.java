package net.semperidem.fishingclub.util;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.item.IllegalGoodsItem;
import net.semperidem.fishingclub.network.payload.SummonAcceptPayload;
import net.semperidem.fishingclub.registry.FCComponents;
import net.semperidem.fishingclub.registry.FCItems;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Commands {
    public static LiteralArgumentBuilder<ServerCommandSource> rootCommand = literal(FishingClub.MOD_ID);
    public static void registerSummonAccept(){
        rootCommand.then(literal("summon_accept").executes(context -> {
            ClientPlayNetworking.send(new SummonAcceptPayload());
            return 0;
        }));
    }

    public static void registerLevelUp(){
        rootCommand.then(literal("level_up").executes(context -> {
            FishingCard fishingCard = FishingCard.of(context.getSource().getPlayer());
            int xpForLevel = fishingCard.nextLevelXP() - fishingCard.getExp();
            fishingCard.grantExperience(xpForLevel);
            return 0;
        }));
    }

    public static void registerIllegalGoods(){
        rootCommand.then(literal("illegal_goods").then(argument("tier", integer()).executes(context -> {
            int tier = getInteger(context, "tier");
            context.getSource().getPlayer().giveItemStack(IllegalGoodsItem.getStackWithTier(tier));
            return 0;
        })));
    }
    public static void registerGiveStarterRod(){
        rootCommand.then(literal("starter_rod").executes(context -> {
            context.getSource().getPlayer().giveItemStack(FCItems.CORE_BAMBOO.getDefaultStack());
            return 0;
        }));
    }
    public static void registerTestFirework(){
        rootCommand.then(literal("firework").executes(context -> {
            return 0;
        }));
    }

    public static void registerInfo(){
        rootCommand.then(literal("info").executes(context -> {
            context.getSource().sendMessage(Text.literal(FishingCard.of(context.getSource().getPlayer()).toString()));
            return 0;
        }));
    }

    public static void registerGoldFish() {
        rootCommand.then(literal("gold_fish").executes(context -> {
            ItemStack goldFish = FCItems.GOLD_FISH.getDefaultStack();
            goldFish.set(FCComponents.CAUGHT_BY, context.getSource().getPlayer().getUuid());
            context.getSource().getPlayer().giveItemStack(goldFish);
            return 0;
        }));
    }

    public static void registerResetSpellCooldown(){
        rootCommand.then(literal("reset_cooldown").executes(context -> {
            FishingCard.of(context.getSource().getPlayer()).resetCooldown();
            return 0;
        }));
    }

    private static int addSkillPoints(CommandContext<ServerCommandSource> context, String targetName){
        ServerPlayerEntity target = context.getSource().getServer().getPlayerManager().getPlayer(targetName);
        if (target != null) {
            int amount = getInteger(context, "amount");
            FishingCard.of(target).addSkillPoints(amount);
            context.getSource().sendMessage(Text.literal("Added " + amount + " to " + targetName + " skill points"));
        } else {
            context.getSource().sendMessage(Text.literal("Player " + targetName + " not found"));
        }
        return 0;
    }

    private static int addCredit(CommandContext<ServerCommandSource> context, String targetName){
        ServerPlayerEntity target = context.getSource().getServer().getPlayerManager().getPlayer(targetName);
        if (target != null) {
            int amount = getInteger(context, "amount");
            FishingCard.of(target).addCredit(amount);
            context.getSource().sendMessage(Text.literal("Added " + amount + " to self credit"));
        } else {
            context.getSource().sendMessage(Text.literal("Player " + targetName + " not found"));
        }
        return 0;
    }

    private static int setCredit(CommandContext<ServerCommandSource> context, String targetName){
        ServerPlayerEntity target = context.getSource().getServer().getPlayerManager().getPlayer(targetName);
        if (target != null) {
            int amount = getInteger(context, "amount");
            FishingCard.of(target).setCredit(amount);
            context.getSource().sendMessage(Text.literal("Set available credit of " + targetName + " to " + amount));
        } else {
            context.getSource().sendMessage(Text.literal("Player " + targetName + " not found"));
        }
        return 0;
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
                ).then(literal("level")
                        .then(argument("amount", integer()).executes(context -> {
                            FishingCard.of(context.getSource().getPlayer()).setLevel(getInteger(context, "amount"));
                            return 0;
                        }))
                )
        );
    }



    public static void register(){
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
            registerSummonAccept();
            registerInfo();
            registerGoldFish();
            registerResetSpellCooldown();
            registerAdd();
            registerSet();
            registerLevelUp();
            registerIllegalGoods();
            registerGiveStarterRod();
            registerTestFirework();
            dispatcher.register(rootCommand);
        });
    }
}
