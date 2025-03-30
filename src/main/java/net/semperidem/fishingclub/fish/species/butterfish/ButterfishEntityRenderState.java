package net.semperidem.fishingclub.fish.species.butterfish;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;

@Environment(EnvType.CLIENT)
public class ButterfishEntityRenderState extends LivingEntityRenderState {
    public ButterfishVariant variant;

    public ButterfishEntityRenderState(){}
}
