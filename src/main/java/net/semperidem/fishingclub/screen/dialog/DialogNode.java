package net.semperidem.fishingclub.screen.dialog;

import java.util.ArrayList;

public class DialogNode {
    public String title;
    public final String content;
    public final ArrayList<DialogNode> questions;
    public String specialAction;

    public DialogNode(String title, String response) {
        this.questions = new ArrayList<>();
        this.content = response;
        this.title = title;
    }

    public void setSpecialAction(String specialAction) {
        this.specialAction = specialAction;
        this.title =  title + " " + specialAction;
    }

    public DialogNode(String response) {
        this("", response);
    }

    public void chain(DialogNode question) {
        questions.add(question);
    }
}
