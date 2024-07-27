package net.semperidem.fishing_club.screen.dialog;

import java.util.ArrayList;

public class DialogNode {
    public String playerSays;
    public String response;
    public ArrayList<DialogNode> questions;
    public Action action;

    public DialogNode(String title, String response) {
        this.questions = new ArrayList<>();
        this.response = response;
        this.playerSays = title;
    }

    public DialogNode(String title, Action action) {
        this.questions = new ArrayList<>();
        this.action = action;
        this.response = action.response;
        this.playerSays = title + " " + action.response;
    }

    public DialogNode(String response) {
        this("", response);
    }

    public static DialogNode start(String response) {//response to player right click
        return new DialogNode("", response);
    }

    public static DialogNode say(String title) {
        return new DialogNode(title, "");
    }

    public DialogNode hear(String response) {
        this.response = response;
        return this;
    }

    public static DialogNode action(String title, Action action) {
        return new DialogNode(title, action);
    }

    public DialogNode option(DialogNode question) {
        this.questions.add(question);
        return this;
    }

    public DialogNode next(DialogNode question) {
        this.questions.add(question);
        return question;
    }

    public enum Action {
        EXIT("[EXIT]"),
        TRADE("[TRADE]"),
        ACCEPT("[ACCEPT]"),
        REFUSE("[REFUSE]");
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
