package net.semperidem.fishing_club.screen.dialog;

import net.minecraft.entity.player.PlayerEntity;
import net.semperidem.fishing_club.entity.FishermanEntity;
import net.semperidem.fishing_club.fisher.FishingCard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static net.semperidem.fishing_club.screen.dialog.DialogNode.*;


public class DialogController {
    private static final HashMap<Set<DialogKey>, DialogNode> DIALOG_START_NODE = new HashMap<>();
    private static final String CONTINUE_RESPONSE = "hmm";

    public static void initialize() {
        DialogNode join = start(JOIN_FISHING_CLUB_START);
        join.next(say(JOIN_FISHING_CLUB_START_RESPONSE).hear(JOIN_FISHING_CLUB_R1))
            .next(say(JOIN_FISHING_CLUB_R1_RESPONSE).hear(JOIN_FISHING_CLUB_R2))
                .next(say(JOIN_FISHING_CLUB_R2_RESPONSE).hear(JOIN_FISHING_CLUB_FINAL))
                    .option(action(JOIN_FISHING_CLUB_FINAL_RESPONSE_TRADE, Action.TRADE))
                    .option(action(JOIN_FISHING_CLUB_FINAL_RESPONSE_LEAVE, Action.EXIT))


        ;
        DIALOG_START_NODE.put(Set.of(
                DialogKey.FISH,
                DialogKey.FIRST,
                DialogKey.SUMMONER
            ), join
        );
    }

    private static final String JOIN_FISHING_CLUB_START =
        """
        God damn, you threw this away?
        This fish is worth at least 1000 coins amongst fishing enthusiast...
        ...and even more within the collectors circle...
        """;

    private static final String JOIN_FISHING_CLUB_START_RESPONSE = CONTINUE_RESPONSE;

    private static final String JOIN_FISHING_CLUB_R1 =
        """
        Ah you aren't licensed fisherman yet, it makes sense now that you didn't know true worth of a good fish.
        I am Derek, head of local Fishing Club.
        This title allows me to issue new Fishing Cards to whoever I please...
        ...and you look like a good fisherman material.
        """;
    private static final String JOIN_FISHING_CLUB_R1_RESPONSE = CONTINUE_RESPONSE;

    private static final String JOIN_FISHING_CLUB_R2 =
         """
         Here's your card and this basic rod we issue together...
         and 1000 coins for that gold fish. Rod ain't much but it gets the job done...
         Unlike some sticks with little bit of string attached to it I see all the impostors use...
         You can always upgrade it with parts you can buy from me or ones you've found [Fishing Card key with rod in hand]
         """;

    private static final String JOIN_FISHING_CLUB_R2_RESPONSE = CONTINUE_RESPONSE;

    private static final String JOIN_FISHING_CLUB_FINAL =
        """
        I also run a lot of other services for fellow members.
        If you ever need something fishing related, I'm interested.
        """;

    private static final String JOIN_FISHING_CLUB_FINAL_RESPONSE_TRADE =
        """
        Thanks! Let's trade
        """;
    private static final String JOIN_FISHING_CLUB_FINAL_RESPONSE_LEAVE =
        """
        Thanks?, bye.
        """;



    public static DialogNode getStartQuestion(Set<DialogKey> keySet) {
        int bestScore = -1;
        DialogNode bestNode = new DialogNode("MMmmmm", "MMmmmm");
            for (Set<DialogKey> initialNodeKey : DIALOG_START_NODE.keySet()) {
                int matchScore = keysMatch(keySet, initialNodeKey);
                if (matchScore > bestScore) {
                    bestScore = matchScore;
                    bestNode = DIALOG_START_NODE.get(initialNodeKey);
                }
            }
        return bestNode;
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
            }
        }
        return score;
    }

    public static HashSet<DialogKey> getKeys(PlayerEntity playerEntity, FishermanEntity fishermanEntity) {
        HashSet<DialogKey> keys = fishermanEntity.getKeys(playerEntity);
        keys.addAll(FishingCard.of(playerEntity).getKeys());
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
