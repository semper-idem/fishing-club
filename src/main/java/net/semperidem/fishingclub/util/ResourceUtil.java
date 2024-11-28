package net.semperidem.fishingclub.util;
import java.util.HashMap;

public class ResourceUtil {
    public static HashMap<Integer, String> MESSAGE_IN_BOTTLE = new HashMap<>();

    public static void loadMessageInBottle() {
        addMessageInBottle("Hook cast distance influences a lot of things, like avg. wait time, fish species, quality and chance of treasure.");
        addMessageInBottle("In a perfect world casting close should reward you just as much as casting long in regards to time spend... but this is not a perfect world");
        addMessageInBottle("Fishing deep in the caves yields much more trash");
        addMessageInBottle("Species are tied to environment not exactly biome, so sometimes there may be places one species can be found even if it's not stated within atlas");
        addMessageInBottle("Atlas gets filled progressively as you catch more of a specie");
        addMessageInBottle("Beside status, high standing in the leaderboard will grant discount in Derek's shop");
        addMessageInBottle("Being the King of Fishers grants buffs to everyone around him but not himself");
        addMessageInBottle("Sneak while looking at displayed fish to see it's details like length and weight");
        addMessageInBottle("Sometimes fish may still your bait without you noticing, better hooks usually help with that problem");
        addMessageInBottle("Environment temperature maybe does not affect you, but it behaviour of your rod");
        addMessageInBottle("Enchanting rods beside increasing fish and treasure can also improve it's usability in cold or hot environments");
        addMessageInBottle("You can always reel line to shorten or lengthen current line distance");
        addMessageInBottle("Ever heard of ninja fishermen? They use their rods to swing around and navigate difficult environments");
        addMessageInBottle("There's sure more junk in the water these days");
        addMessageInBottle("Aquatic flora is usually a good sign in search of the best fishing spot");
        addMessageInBottle("Fishing spots are identical, while some offer great views, some may offer better fish");
        addMessageInBottle("Overfishing can cause fish quality and quantity to degrade make sure you change locations");
        addMessageInBottle("While initial state of aquatic flora and found has impact on spot quality you some work it can always be improved a little");
        addMessageInBottle("Diversity of aquatic flora is the key metric in find the right spot");
        addMessageInBottle("Hello, I'm under the water, please send help");
        addMessageInBottle("Blub blub blub");
        addMessageInBottle("Why was the fish in the circus? It was a clownfish");
        addMessageInBottle(".... ... ...... ...........");
        addMessageInBottle("1652 57 12997");
        addMessageInBottle("Never trust a piece of paper you just found");
        addMessageInBottle("My treasure is buried at...");
        addMessageInBottle("fish");
        addMessageInBottle("Ten tickles...");
        addMessageInBottle("very funny");
        addMessageInBottle("Important Information");
        addMessageInBottle("huh");
        addMessageInBottle("how do you use these");
        addMessageInBottle("");
        addMessageInBottle("");
        addMessageInBottle("");
    }
    private static void addMessageInBottle(String message) {
        MESSAGE_IN_BOTTLE.put(MESSAGE_IN_BOTTLE.size(), message);
    }


}
