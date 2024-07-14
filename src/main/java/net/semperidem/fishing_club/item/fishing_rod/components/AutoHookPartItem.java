package net.semperidem.fishing_club.item.fishing_rod.components;

public class AutoHookPartItem extends HookPartItem{
    float autoHookChance = 1;
    public AutoHookPartItem(Settings settings) {
        super(settings);
    }

    public AutoHookPartItem(Settings settings, int weightCapacity, int minOperatingTemperature, int maxOperatingTemperature, float fishQuality) {
        super(settings, weightCapacity, minOperatingTemperature, maxOperatingTemperature, fishQuality);
    }

    public AutoHookPartItem(Settings settings, int weightCapacity, int minOperatingTemperature, int maxOperatingTemperature) {
        super(settings, weightCapacity, minOperatingTemperature, maxOperatingTemperature);
    }

    public AutoHookPartItem(Settings settings, int weightCapacity) {
        super(settings, weightCapacity);
    }

    public AutoHookPartItem autoHookChance(float autoHookChance) {
        this.autoHookChance = autoHookChance;
        return this;
    }

    public float getAutoHookChance() {
        return autoHookChance;
    }

}
