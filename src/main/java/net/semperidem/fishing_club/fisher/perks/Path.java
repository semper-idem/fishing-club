package net.semperidem.fishing_club.fisher.perks;

public enum Path {
    HOBBYIST("Hobbyist"),
    OPPORTUNIST("Opportunist"),
    SOCIALIST("Socialist"),
    GENERAL("General");
    public final String name;

    Path(String name) {
        this.name = name;
    }
}
