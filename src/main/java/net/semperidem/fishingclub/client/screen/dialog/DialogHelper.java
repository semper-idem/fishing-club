package net.semperidem.fishingclub.client.screen.dialog;

import net.minecraft.entity.player.PlayerEntity;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.fisher.FishingCard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DialogHelper {
    private static HashMap<Set<String>, DialogNode> DEREK_ROOT_QUESTION;

    private static final String GOLDEN = "GOLDEN";
    private static final String GRADE = "GRADE";
    private static final String SPELL = "SPELL";
    private static final String UNIQUE = "UNIQUE";
    private static final String NOT_UNIQUE = "NOT_UNIQUE";
    private static final String SUMMONER = "SUMMONER";
    private static final String NOT_SUMMONER = "NOT_SUMMONER";
    private static final String REPEATED = "REPEATED";
    private static final String NOT_REPEATED = "NOT_REPEATED";
    private static final String WELCOME = "WELCOME";
    private static final String NOT_WELCOME = "NOT_WELCOME";

    public static void register() {
        DEREK_ROOT_QUESTION = new HashMap<>();
        DialogNode itMustBeYours = new DialogNode(Responses.start_itMustBeYours);
        DialogNode ohItsYouAgain = new DialogNode(Responses.start_ohItsYouAgain);
        DialogNode canIHaveIt = new DialogNode(Responses.start_soCanIHaveIt);
        DialogNode hello = new DialogNode(Responses.start_hello);
        DialogNode angry = new DialogNode(Responses.youDontExist);
        DEREK_ROOT_QUESTION.put(Set.of(GOLDEN, NOT_WELCOME, NOT_REPEATED, UNIQUE, SUMMONER), itMustBeYours);
        DEREK_ROOT_QUESTION.put(Set.of(GOLDEN, WELCOME, NOT_REPEATED, NOT_UNIQUE, SUMMONER), ohItsYouAgain);
        DEREK_ROOT_QUESTION.put(Set.of(GOLDEN, NOT_WELCOME, NOT_REPEATED, NOT_UNIQUE, SUMMONER), canIHaveIt);
        DEREK_ROOT_QUESTION.put(Set.of(GOLDEN, WELCOME, NOT_UNIQUE), hello);
        DEREK_ROOT_QUESTION.put(Set.of(NOT_SUMMONER), angry);
        DialogNode hereTakeThisFishingRod = new DialogNode("Sure, my name is $PLAYER_NAME.", Responses.hereTakeThisFishingRod);
        canIHaveIt.chain(hereTakeThisFishingRod);
        itMustBeYours.chain(hereTakeThisFishingRod);
        DialogNode waitTrade = new DialogNode("Wait, Can I take a look?", Responses.TRADE);
        hereTakeThisFishingRod.chain(waitTrade);
        DialogNode politeExit = new DialogNode("Thanks, bye.", Responses.EXIT);
        hereTakeThisFishingRod.chain(politeExit);
        DialogNode trade = new DialogNode("Hi, let's trade.", Responses.TRADE);
        DialogNode neverMind = new DialogNode("Umm... never mind.", Responses.EXIT);
        hello.chain(trade);
        hello.chain(neverMind);
        DialogNode areYouSure = new DialogNode("No, sorry.", Responses.areYouSure);
        itMustBeYours.chain(areYouSure);
        canIHaveIt.chain(areYouSure);
        DialogNode itsAllRight = new DialogNode("No, If it's worth so much for you I'd like to keep it.", Responses.itsAllRight);
        areYouSure.chain(itsAllRight);
        areYouSure.chain(hereTakeThisFishingRod);
        DialogNode silentExit = new DialogNode("...", Responses.EXIT);
        DialogNode questionableExit = new DialogNode("...?", Responses.EXIT);
        itsAllRight.chain(silentExit);
        angry.chain(questionableExit);

        DialogNode wellNoRod = new DialogNode("Sure.", Responses.wellNoRod);
        ohItsYouAgain.chain(wellNoRod);


        //SUMMON
        DialogNode huhWhat = new DialogNode(Responses.start_huhWhat);
        DialogNode huhWhatInsane = new DialogNode(Responses.start_huhWhatSlightlyInsane);
        DialogNode whatDoYouWant = new DialogNode(Responses.start_whatDoYouWant);
        DEREK_ROOT_QUESTION.put(Set.of(SPELL, NOT_REPEATED, WELCOME, UNIQUE), huhWhat);
        DEREK_ROOT_QUESTION.put(Set.of(SPELL, NOT_REPEATED, WELCOME, NOT_UNIQUE), huhWhatInsane);
        DEREK_ROOT_QUESTION.put(Set.of(SPELL, REPEATED, WELCOME), whatDoYouWant);
        huhWhat.chain(trade);
        huhWhat.chain(neverMind);
        huhWhatInsane.chain(neverMind);
        DialogNode helpfulTrade = new DialogNode("I can help you by buying all your stock! Let's trade.", Responses.TRADE);
        huhWhatInsane.chain(helpfulTrade);
        DialogNode questionTrade = new DialogNode("Trade?", Responses.TRADE);
        DialogNode nothing = new DialogNode("Nothing...", Responses.EXIT);
        whatDoYouWant.chain(questionTrade);
        whatDoYouWant.chain(nothing);



        //GRADE
        DialogNode memberDamn = new DialogNode(Responses.start_goddamn);
        DialogNode notMemberDamn = new DialogNode(Responses.start_goddamn);
        DialogNode disappointed = new DialogNode(Responses.start_Disrespect);
        DEREK_ROOT_QUESTION.put(Set.of(GRADE, NOT_UNIQUE, WELCOME), hello);
        DEREK_ROOT_QUESTION.put(Set.of(GRADE, REPEATED, NOT_UNIQUE, NOT_WELCOME, SUMMONER), disappointed);
        DEREK_ROOT_QUESTION.put(Set.of(GRADE, NOT_REPEATED, UNIQUE, NOT_WELCOME, SUMMONER), notMemberDamn);
        DEREK_ROOT_QUESTION.put(Set.of(GRADE, NOT_REPEATED, UNIQUE, WELCOME, SUMMONER), memberDamn);
        DialogNode gradeTrade = new DialogNode("Well it was me but you can have it. Let's trade.", Responses.TRADE);
        memberDamn.chain(gradeTrade);
        DialogNode giveItBackMember = new DialogNode("Hey it was mine, give it back!", Responses.hahaWhy);
        memberDamn.chain(giveItBackMember);
        DialogNode luckyYou = new DialogNode("Lucky you...", Responses.EXIT);
        memberDamn.chain(luckyYou);
        DialogNode yeahRightAnyway = new DialogNode("Yeah right, anyway can we at least trade.", Responses.TRADE);
        giveItBackMember.chain(yeahRightAnyway);
        DialogNode okBye = new DialogNode("Okay... bye.", Responses.EXIT);
        giveItBackMember.chain(okBye);
        DialogNode youCanHaveIt = new DialogNode("Well it was me but you can have it I guess.", Responses.hereTakeThisFishingRod);
        notMemberDamn.chain(youCanHaveIt);
        DialogNode youCantHaveIt = new DialogNode("Hey it was mine, give it back!", Responses.silly);
        youCantHaveIt.chain(silentExit);
        disappointed.chain(silentExit);

    }


    public static DialogNode getStartQuestion(Set<String> keySet) {
        int bestScore = 0;
        DialogNode bestNode = new DialogNode("MMmmmm", "MMmmmm");
            for (Set<String> initialNodeKey : DEREK_ROOT_QUESTION.keySet()) {
                int matchScore = keysMatch(keySet, initialNodeKey);
                if (matchScore > bestScore) {
                    bestScore = matchScore;
                    bestNode = DEREK_ROOT_QUESTION.get(initialNodeKey);
                }
            }
        return bestNode;
    }

    private static int keysMatch(Set<String> keySet, Set<String> against) {
        int score = 0;
        for(String key : against) {
            if (keySet.contains(key)) {
                score++;
            }
        }
        return score;
    }

    public static HashSet<String> getKeys(PlayerEntity playerEntity, FishermanEntity fishermanEntity) {
        HashSet<String> keys = fishermanEntity.getKeys(playerEntity);
        keys.addAll(FishingCard.getPlayerCard(playerEntity).getKeys(fishermanEntity.getSummonType()));
        return keys;
    }

    public static String getTextForTick(String text, int tick) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        int lastCharIndex = 0;
        int tickCount = 0;
        while(tickCount < tick && lastCharIndex < text.length()) {
            tickCount += getTicksPerCharacter(text.charAt(lastCharIndex));
            lastCharIndex++;
        }
        return text.substring(0, lastCharIndex);
    }

    public static int getTickForText(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        int tickCount = 0;
        for(int i = 0; i < text.length(); i++) {
            tickCount+= getTicksPerCharacter(text.charAt(i));
        }
        return tickCount;
    }

    private static int getTicksPerCharacter(char c) {
        return switch (c) {
            case '.' -> 15;
            case '?', '!' -> 10;
            case ',' -> 3;
            case ' ' -> 2;
            default -> 1;
        };
    }
}
