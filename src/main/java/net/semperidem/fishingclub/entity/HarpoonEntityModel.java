package net.semperidem.fishingclub.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class HarpoonEntityModel extends Model{
    private final ModelPart root;

    public HarpoonEntityModel(ModelPart root) {
        super(RenderLayer::getEntitySolid);
        this.root = root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData modelPartData2 = modelPartData.addChild("pole", ModelPartBuilder.create().uv(0, 6).cuboid(-0.5f, -4.0f, -0.5f, 1.0f, 36.0f, 1.0f), ModelTransform.NONE);
         modelPartData2.addChild("hook_0", ModelPartBuilder.create().uv(4, 0).cuboid(-1.5f, -2.0f, -0.5f, 1.0f, 4.0f, 1.0f), ModelTransform.NONE);
         modelPartData2.addChild("hook_1", ModelPartBuilder.create().uv(4, 0).cuboid(-2.5f, -0.0f, -0.5f, 1.0f, 4.0f, 1.0f), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
