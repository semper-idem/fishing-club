package net.semperidem.fishingclub.screen.dialog;

import net.minecraft.entity.player.PlayerEntity;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.fisher.FishingCard;

import java.util.*;

import static net.semperidem.fishingclub.screen.dialog.DialogNode.*;


public class DialogController {
    private static final HashMap<Set<DialogKey>, DialogNode> DIALOG_START_NODE = new HashMap<>();
    private static final ArrayList<DialogNode> REFUSE_INTERACTION_NODES = new ArrayList<>();


    public static void initialize() {
        DIALOG_START_NODE.put(Set.of(
                DialogKey.FISH,
                DialogKey.FIRST,
                DialogKey.SUMMONER
            ), start(JOIN_fishingclub_START)
                .next(action(JOIN_fishingclub_R1_RESPONSE, Action.ACCEPT).hear(JOIN_fishingclub_R2))
                .option(action(JOIN_fishingclub_FINAL_RESPONSE_TRADE, Action.TRADE))
                .option(action(JOIN_fishingclub_FINAL_RESPONSE_LEAVE, Action.EXIT))
            .root()
        );
        DIALOG_START_NODE.put(Set.of(
                DialogKey.FISH,
                DialogKey.FIRST,
                DialogKey.CARD
            ), start(FISH_START)
                .option(TRADE_NODE)
                .option(DISMISS_NODE)
                .option(EXIT_NODE)
            .root()
        );
        DIALOG_START_NODE.put(Set.of(
                DialogKey.SPELL,
                DialogKey.FIRST,
                DialogKey.CARD
            ), start(SPELL_START)
                .option(TRADE_NODE)
                .option(DISMISS_NODE)
                .option(EXIT_NODE)
                .root()
        );

        DIALOG_START_NODE.put(Set.of(
                DialogKey.CARD
            ), start(STANDARD_START)
                .option(TRADE_NODE)
                .option(DISMISS_NODE)
                .option(EXIT_NODE)
                .root()
        );

        REFUSE_INTERACTION_NODES.add(DialogNode.start(REFUSE_INTERACTION_1)
            .option(DISMISS_NODE)
            .option(QUITE_EXIT_NODE)
        );
        REFUSE_INTERACTION_NODES.add(DialogNode.start(REFUSE_INTERACTION_2)
            .option(DISMISS_NODE)
            .option(QUITE_EXIT_NODE)
        );
        REFUSE_INTERACTION_NODES.add(DialogNode.start(REFUSE_INTERACTION_3)
            .option(DISMISS_NODE)
            .option(QUITE_EXIT_NODE)
        );
        REFUSE_INTERACTION_NODES.add(DialogNode.start(REFUSE_INTERACTION_4)
            .option(DISMISS_NODE)
            .option(QUITE_EXIT_NODE)
        );
        REFUSE_INTERACTION_NODES.add(DialogNode.start(REFUSE_INTERACTION_5)
            .option(DISMISS_NODE)
            .option(QUITE_EXIT_NODE)
        );
    }

    private static final String JOIN_fishingclub_START =
        """
        God damn, you threw this away?
        This fish is worth at least 1000 coins amongst fishing enthusiast
        and even more within the collectors circle...
        Ah you aren't licensed fisherman yet, it makes sense now that you didn't know true worth of a good fish.
        I am Derek, head of local Fishing Club.
        This title allows me to issue new Fishing Cards to whoever I please,
        and you look like a good fisherman material.
        """;
    private static final String JOIN_fishingclub_R1_RESPONSE =
        " - Huh";

    private static final String JOIN_fishingclub_R2 =
        """
        Here's your card and this basic rod we issue together
        and 1000 coins for that gold fish. Rod ain't much but it gets the job done,
        unlike some sticks with little bit of string attached to it I see all the impostors use...
        Anyway I also run a lot of other services for fellow members.
        If you ever need something fishing related, I'm interested.
        """;

    private static final String JOIN_fishingclub_FINAL_RESPONSE_TRADE =
        " - Thanks! Let's trade";
    private static final String JOIN_fishingclub_FINAL_RESPONSE_LEAVE =
        " - Thanks?, bye.";


    private static final String STANDARD_START =
        "Howdy, need anything?";
    private static final String STANDARD_TRADE =
        "Yes";
    private static final String STANDARD_DISMISS =
        "You're scaring the fish!";
    private static final String STANDARD_EXIT =
        "Nothing for now.";



    private static final String SPELL_START =
        "How did I get here...? Never mind... hello, need anything?";

    private static final String FISH_START =
        "Greeting fellow. I was just picking up that fine fish somebody left. Do you need anything?";

    private static final String REFUSE_INTERACTION_1 =
        "(Derek doesn't seem to acknowledge your existence)";
    private static final String REFUSE_INTERACTION_2 =
        "Talk to me when your real fisherman...";
    private static final String REFUSE_INTERACTION_3 =
        """
        Who are you to bother me? Ever caught a fish worth more then 1000 coins?
        No? Then we don't have anything to talk about";
        """;
    private static final String REFUSE_INTERACTION_4 =
        "You don't fish, you don't exist.";
    private static final String REFUSE_INTERACTION_5 =
        "Just a string on a stick and you call THAT a fishing rod? What has world come to...";


    private static final DialogNode TRADE_NODE = action(STANDARD_TRADE, Action.TRADE);
    private static final DialogNode DISMISS_NODE = action(STANDARD_DISMISS, Action.DISMISS);
    private static final DialogNode EXIT_NODE = action(STANDARD_EXIT, Action.EXIT);
    private static final DialogNode QUITE_EXIT_NODE = action("", Action.EXIT);

    public static DialogNode getStartQuestion(Set<DialogKey> keySet) {
        int bestScore = -1;
        DialogNode bestNode = null;
            for (Set<DialogKey> initialNodeKey : DIALOG_START_NODE.keySet()) {
                int matchScore = keysMatch(keySet, initialNodeKey);
                if (matchScore > bestScore) {
                    bestScore = matchScore;
                    bestNode = DIALOG_START_NODE.get(initialNodeKey);
                }
            }
        return bestNode == null ? getRandomRefuse() : bestNode;
    }

    private static DialogNode getRandomRefuse() {
        return REFUSE_INTERACTION_NODES.get((int) (Math.random() * REFUSE_INTERACTION_NODES.size()));
    }

    public static HashSet<DialogKey> getKeysFromString(String keysString) {
        HashSet<DialogKey> result = new HashSet<>();
        for(String keyString : keysString.split(";")) {
            result.add(DialogKey.valueOf(keyString));
        }
        return result;
    }

    public static String getStringFromKeys(HashSet<DialogKey> keys) {
        StringBuilder result = new StringBuilder();
        for(DialogKey key : keys) {
            result.append(key).append(";");
        }
        return result.toString();
    }

    private static int keysMatch(Set<DialogKey> keySet, Set<DialogKey> against) {
        int score = 0;
        for(DialogKey key : against) {
            if (keySet.contains(key)) {
                score++;
                continue;
            }
            score--;
        }
        return score;
    }

    public static HashSet<DialogKey> getKeys(PlayerEntity playerEntity, FishermanEntity fishermanEntity) {
        HashSet<DialogKey> keys = fishermanEntity.getKeys(playerEntity);
        keys.addAll(FishingCard.of(playerEntity).getKeys());
        return keys;
    }
}
