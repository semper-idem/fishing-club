package net.semperidem.fishingclub.util;
import java.nio.file.Path;
import java.util.HashMap;

public class ResourceUtil {
    public static HashMap<Integer, String> MESSAGE_IN_BOTTLE = new HashMap<>();

    public static void loadMessageInBottle() {
        addMessageInBottle("Message 1");
        addMessageInBottle("Message 2");
    }
    private static void addMessageInBottle(String message) {
        MESSAGE_IN_BOTTLE.put(MESSAGE_IN_BOTTLE.size(), message);
    }


}
