package net.semperidem.fishingclub.mixin.client.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BoatEntityModel.class)
public interface BoatEntityModelAccessor {

    @Accessor("leftPaddle")
    ModelPart getLeftPaddle();
    @Accessor("rightPaddle")
    ModelPart getRightPaddle();
}
