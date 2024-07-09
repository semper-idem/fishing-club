package net.semperidem.fishingclub.item.fishing_rod.components;

import static net.semperidem.fishingclub.item.fishing_rod.components.RodConfigurationComponent.EMPTY;

public class RodConfigurationController {
    private final RodConfigurationComponent fromRecord;

    int weightCapacity = EMPTY.weightCapacity();
    int weightMagnitude = EMPTY.weightMagnitude();
    int maxLineLength = EMPTY.maxLineLength();
    float castPower = EMPTY.castPower();
    boolean canCast = EMPTY.canCast();

    public RodConfigurationComponent toRecord() {
        return new RodConfigurationComponent(
                this.fromRecord.coreComponent(),
                this.fromRecord.lineComponent(),
                this.maxLineLength,
                this.castPower,
                this.weightCapacity,
                this.weightMagnitude,
                this.canCast
        );
    }

    public RodConfigurationController(RodConfigurationComponent fromRecord) {
        this.fromRecord = fromRecord;
        this.fromRecord.coreComponent().apply(this);
        this.fromRecord.lineComponent().apply(this);
    }

    void setWeightCapacity(int weightCapacity) {
        if (this.weightCapacity > weightCapacity || weightCapacity == EMPTY.weightCapacity()){
            this.weightCapacity = weightCapacity;
        }
        this.weightMagnitude = getWeightMagnitude();
    }

    public int getWeightMagnitude() {
        if (weightCapacity < 25) {
            return 1;
        }
        if (weightCapacity < 100) {
            return 0;
        }
        if (weightCapacity < 250) {
            return -1;
        }
        return -4;
    }

}
