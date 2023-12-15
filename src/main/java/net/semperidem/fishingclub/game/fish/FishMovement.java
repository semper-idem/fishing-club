package net.semperidem.fishingclub.game.fish;

public class FishMovement {
    private static final int STAMINA_PER_LEVEL = 250;
    private static final int STAMINA_BASE = 200;

    private int stamina;
    private final int minStamina;
    private final int maxStamina;
    private final FishPatternInstance pattern;

    public FishMovement(FishType fishType, int fishLevel){
        stamina = STAMINA_BASE + fishType.staminaLevel * STAMINA_PER_LEVEL;
        minStamina = stamina / 2;
        maxStamina = stamina;
        pattern = new FishPatternInstance(fishType.fishPattern, fishLevel);

    }
}
