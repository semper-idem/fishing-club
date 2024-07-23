package net.semperidem.fishing_club.entity.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.semperidem.fishing_club.entity.TackleBoxBlockEntity;

public class TackleBoxBlockEntityRenderer implements BlockEntityRenderer<TackleBoxBlockEntity> {


    public TackleBoxBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }
    @Override
    public void render(TackleBoxBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
    }

}
