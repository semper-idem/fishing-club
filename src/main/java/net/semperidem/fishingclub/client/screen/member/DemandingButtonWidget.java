package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.text.Text;

import java.util.function.Supplier;

public class DemandingButtonWidget extends MemberButton {
    Supplier<Boolean> demand;
    public DemandingButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, Supplier<Boolean> demand) {
        super(x, y, width, height, message, onPress);
        this.demand = demand;
        this.active = demand.get();
    }

    public void tick() {
        this.active = this.demand.get();
    }
}
