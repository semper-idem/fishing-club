package net.semperidem.fishing_club.entity.renderer.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class FCFishEntityModel<T extends Entity> extends SinglePartEntityModel<T> {
    private final ModelPart root;
    private final ModelPart tailFin;

    public FCFishEntityModel(ModelPart root) {
        this.root = root;
        this.tailFin = root.hasChild(EntityModelPartNames.TAIL_FIN) ? root.getChild(EntityModelPartNames.TAIL_FIN) : root.getChild(EntityModelPartNames.BODY).getChild(EntityModelPartNames.TAIL_FIN);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float f = 1.0F;
        if (!entity.isTouchingWater()) {
            f = 1.5F;
        }

        this.tailFin.yaw = -f * 0.15F * MathHelper.sin(0.6F * animationProgress);
    }
}