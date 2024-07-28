package net.semperidem.fishing_club.screen.dialog;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class DialogNode {
    public Text playerSays;
    public String response;
    public DialogNode parent;
    public ArrayList<DialogNode> questions;
    public List<Text> responseLines = new ArrayList<>();
    public Action action;


    public DialogNode(String title, String response) {
        this.questions = new ArrayList<>();
        this.playerSays = Text.of(title);
        this.hear(response);
    }

    public DialogNode(String title, Action action) {
        this(title + " " + action.response, action.response);
        this.action = action;
    }

    public static DialogNode start(String response) {//response to player right click
        return new DialogNode("", response);
    }

    public static DialogNode say(String title) {
        return new DialogNode(title, "");
    }

    public DialogNode hear(String response) {
        this.response = response;
        for(String line : response.split("\n")) {
            if (line.isEmpty()) {
                continue;
            }
            this.responseLines.add(Text.of(line));
        }
        return this;
    }

    public static DialogNode action(String title, Action action) {
        return new DialogNode(title, action);
    }

    public DialogNode option(DialogNode question) {
        this.questions.add(question);
        question.parent = this;
        return this;
    }

    public DialogNode next(DialogNode question) {
        this.questions.add(question);
        question.parent = this;
        return question;
    }

    public DialogNode root() {
        if (parent == null) {
            return this;
        }
        return parent.root();
    }

    public enum Action {
        EXIT("[EXIT]"),
        TRADE("[TRADE]"),
        ACCEPT("[ACCEPT]"),
        DISMISS("[DISMISS]");
        final String response;
        Action(String response) {
            this.response = response;
        }
    }

    public enum DialogKey {
        FISH, SPELL,
        SUMMONER,
        CARD,
        FIRST,
    }
}
