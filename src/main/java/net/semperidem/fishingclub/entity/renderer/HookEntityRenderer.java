package net.semperidem.fishingclub.entity.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.FishingBobberEntityState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.HookEntity;
import net.semperidem.fishingclub.registry.Tags;

public class HookEntityRenderer extends EntityRenderer<HookEntity, FishingBobberEntityState> {
    public static final Identifier DEFAULT = FishingClub.identifier("textures/entity/bobber.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityCutout(DEFAULT);

    public HookEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public void render(FishingBobberEntityState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
            matrixStack.push();
            matrixStack.push();
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            matrixStack.multiply(this.dispatcher.getRotation());
            MatrixStack.Entry entry = matrixStack.peek();
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
            vertex(vertexConsumer, entry, i, 0.0F, 0, 0, 1);
            vertex(vertexConsumer, entry, i, 1.0F, 0, 1, 1);
            vertex(vertexConsumer, entry, i, 1.0F, 1, 1, 0);
            vertex(vertexConsumer, entry, i, 0.0F, 1, 0, 0);
            matrixStack.pop();
		float f = (float)state.pos.x;
		float g = (float)state.pos.y;
		float h = (float)state.pos.z;
		VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getLineStrip());
		MatrixStack.Entry entry2 = matrixStack.peek();
		int j = 16;

		for (int k = 0; k <= 16; k++) {
			renderFishingLine(f, g, h, vertexConsumer2, entry2, percentage(k, 16), percentage(k + 1, 16));
		}

		matrixStack.pop();
		super.render(state, matrixStack, vertexConsumerProvider, i);
	  }

    public static Arm getArmHoldingRod(PlayerEntity player) {
        return player.getMainHandStack().isIn(Tags.CORE) ? player.getMainArm() : player.getMainArm().getOpposite();
    }

    private Vec3d getHandPos(PlayerEntity player, float f, float tickProgress) {
        int i = getArmHoldingRod(player) == Arm.RIGHT ? 1 : -1;
        if (this.dispatcher.gameOptions.getPerspective().isFirstPerson() && player == MinecraftClient.getInstance().player) {
            double m = 960.0 / this.dispatcher.gameOptions.getFov().getValue().intValue();
            Vec3d vec3d = this.dispatcher.camera.getProjection().getPosition(i * 0.525F, -0.1F).multiply(m).rotateY(f * 0.5F).rotateX(-f * 0.7F);
            return player.getCameraPosVec(tickProgress).add(vec3d);
        } else {
            float g = MathHelper.lerp(tickProgress, player.lastBodyYaw, player.bodyYaw) * (float) (Math.PI / 180.0);
            double d = MathHelper.sin(g);
            double e = MathHelper.cos(g);
            float h = player.getScale();
            double j = i * 0.35 * h;
            double k = 0.8 * h;
            float l = player.isInSneakingPose() ? -0.1875F : 0.0F;
            return player.getCameraPosVec(tickProgress).add(-e * j - d * k, l - 0.45 * h, -d * j + e * k);
        }
    }

    private static float percentage(int value, int denominator) {
        return (float)value / denominator;
    }

    private static void vertex(VertexConsumer buffer, MatrixStack.Entry matrix, int light, float x, int y, int u, int v) {
        buffer.vertex(matrix, x - 0.5F, y - 0.5F, 0.0F)
                .color(Colors.WHITE)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(matrix, 0.0F, 1.0F, 0.0F);
    }

    private static void renderFishingLine(float x, float y, float z, VertexConsumer buffer, MatrixStack.Entry matrices, float segmentStart, float segmentEnd) {
        float f = x * segmentStart;
        float g = y * (segmentStart * segmentStart + segmentStart) * 0.5F + 0.25F;
        float h = z * segmentStart;
        float i = x * segmentEnd - f;
        float j = y * (segmentEnd * segmentEnd + segmentEnd) * 0.5F + 0.25F - g;
        float k = z * segmentEnd - h;
        float l = MathHelper.sqrt(i * i + j * j + k * k);
        i /= l;
        j /= l;
        k /= l;
        buffer.vertex(matrices, f, g, h).color(Colors.BLACK).normal(matrices, i, j, k);
    }

    public FishingBobberEntityState createRenderState() {
        return new FishingBobberEntityState();
    }

    public void updateRenderState(HookEntity hookEntity, FishingBobberEntityState state, float f) {
        super.updateRenderState(hookEntity, state, f);
        PlayerEntity playerEntity = hookEntity.getPlayerOwner();
        if (playerEntity == null) {
            state.pos = Vec3d.ZERO;
        } else {
            float g = playerEntity.getHandSwingProgress(f);
            float h = MathHelper.sin(MathHelper.sqrt(g) * (float) Math.PI);
            Vec3d vec3d = this.getHandPos(playerEntity, h, f);
            Vec3d vec3d2 = hookEntity.getLerpedPos(f).add(0.0, 0.25, 0.0);
            state.pos = vec3d.subtract(vec3d2);
        }
    }

    protected boolean canBeCulled(HookEntity hookEntity) {
        return false;
    }

}
