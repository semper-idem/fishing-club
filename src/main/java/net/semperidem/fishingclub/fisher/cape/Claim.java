package net.semperidem.fishingclub.fisher.cape;

public record Claim(String uuidString, long timestamp) {

    @Override
    public String toString() {
        return uuidString + ";" + timestamp;
    }
}
