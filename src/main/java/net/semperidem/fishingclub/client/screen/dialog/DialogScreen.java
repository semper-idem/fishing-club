package net.semperidem.fishingclub.client.screen.dialog;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogScreen extends Screen {
    int textSpeed = 1;
    int textTick = 0;

    String message = "Test message 123, more test. wow!!! amazing? lol";
    protected DialogScreen(Text text) {
        super(text);
    }

    public void tick(){
        textTick++;
    }

    class DialogMessage {
        String text;
        int textTimeLength;
        ArrayList<DialogMessage> choices;

        final static int cDuration = 2;
        final static HashMap<Character, Integer> pauseMap = new HashMap<>() {{
            put('.', 10);
            put(',', 5);
            put('?', 10);
            put('!', 10);
        }};

        DialogMessage(String text){
            this.text = text;
            calculateTimeLength();
        }

        void calculateTimeLength(){
            int time = 0;
            for(int i = 0; i < text.length(); i++) {
                time += pauseMap.getOrDefault(text.charAt(i), cDuration);
            }
            this.textTimeLength = time;
        }

        String getTextForTime(int time){
            float textPercent = 1f * time / textTimeLength;
            return text.substring(0, (int) (text.length() * textPercent));
        }

        void addChoice(DialogMessage child){
            choices.add(child);
        }
    }

    class DialogChoiceWidget extends ClickableWidget {
        private DialogMessage displayedMessage;
        public DialogChoiceWidget(int x, int y, int width, int height, Text message) {
            super(x, y, width, height, message);
        }

        @Override
        public void appendNarrations(NarrationMessageBuilder narrationMessageBuilder) {

        }
    }
}
