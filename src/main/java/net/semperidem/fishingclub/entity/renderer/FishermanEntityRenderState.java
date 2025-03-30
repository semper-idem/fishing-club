package net.semperidem.fishingclub.entity.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.village.VillagerData;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class FishermanEntityRenderState extends LivingEntityRenderState {
    public boolean headRolling;
    public float yaw;
    public boolean submergedInWater;
    public float leftPaddleAngle;
    public float rightPaddleAngle;
    public int outOfWaterTicks;
}
