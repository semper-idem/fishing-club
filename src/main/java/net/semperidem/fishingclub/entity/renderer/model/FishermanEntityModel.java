package net.semperidem.fishingclub.entity.renderer.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.entity.state.VillagerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.entity.renderer.FishermanEntityRenderState;

public class FishermanEntityModel extends EntityModel<FishermanEntityRenderState> implements ModelWithHead, ModelWithHat {
        public static final ModelTransformer BABY_TRANSFORMER = ModelTransformer.scaling(0.5F);
        private final ModelPart head;
        private final ModelPart hat;
        private final ModelPart hatRim;
        private final ModelPart rightLeg;
        private final ModelPart leftLeg;
        private final ModelPart arms;



    public FishermanEntityModel(ModelPart root) {
        super(root);
        this.head = root.getChild(EntityModelPartNames.HEAD);
        this.hat = this.head.getChild(EntityModelPartNames.HAT);
        this.hatRim = this.hat.getChild(EntityModelPartNames.HAT_RIM);
        this.rightLeg = root.getChild(EntityModelPartNames.RIGHT_LEG);
        this.leftLeg = root.getChild(EntityModelPartNames.LEFT_LEG);
        this.arms = root.getChild(EntityModelPartNames.ARMS);
    }

    public static ModelData getModelData() {
            ModelData modelData = new ModelData();
            ModelPartData modelPartData = modelData.getRoot();
            float f = 0.5F;
            ModelPartData modelPartData2 = modelPartData.addChild(
                    EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), ModelTransform.NONE
            );
            ModelPartData modelPartData3 = modelPartData2.addChild(
                    EntityModelPartNames.HAT, ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new Dilation(0.51F)), ModelTransform.NONE
            );
            modelPartData3.addChild(
                    EntityModelPartNames.HAT_RIM,
                    ModelPartBuilder.create().uv(30, 47).cuboid(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 1.0F),
                    ModelTransform.rotation((float) (-Math.PI / 2), 0.0F, 0.0F)
            );
            modelPartData2.addChild(
                    EntityModelPartNames.NOSE, ModelPartBuilder.create().uv(24, 0).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F), ModelTransform.origin(0.0F, -2.0F, 0.0F)
            );
            ModelPartData modelPartData4 = modelPartData.addChild(
                    EntityModelPartNames.BODY, ModelPartBuilder.create().uv(16, 20).cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F), ModelTransform.NONE
            );
            modelPartData4.addChild(
                    EntityModelPartNames.JACKET, ModelPartBuilder.create().uv(0, 38).cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, new Dilation(0.5F)), ModelTransform.NONE
            );
            modelPartData.addChild(
                    EntityModelPartNames.ARMS,
                    ModelPartBuilder.create()
                            .uv(44, 22)
                            .cuboid(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F)
                            .uv(44, 22)
                            .cuboid(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, true)
                            .uv(40, 38)
                            .cuboid(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F),
                    ModelTransform.of(0.0F, 3.0F, -1.0F, -0.75F, 0.0F, 0.0F)
            );
            modelPartData.addChild(
                    EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(0, 22).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), ModelTransform.origin(-2.0F, 12.0F, 0.0F)
            );
            modelPartData.addChild(
                    EntityModelPartNames.LEFT_LEG,
                    ModelPartBuilder.create().uv(0, 22).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                    ModelTransform.origin(2.0F, 12.0F, 0.0F)
            );
            return modelData;
        }

        public void setAngles(FishermanEntityRenderState state) {
            super.setAngles(state);
            this.head.yaw = state.relativeHeadYaw * (float) (Math.PI / 180.0);
            this.head.pitch = state.pitch * (float) (Math.PI / 180.0);
            if (state.headRolling) {
                this.head.roll = 0.3F * MathHelper.sin(0.45F * state.age);
                this.head.pitch = 0.4F;
            } else {
                this.head.roll = 0.0F;
            }

            this.rightLeg.pitch = MathHelper.cos(state.limbSwingAnimationProgress * 0.6662F)
                    * 1.4F
                    * state.limbSwingAmplitude
                    * 0.5F;
            this.leftLeg.pitch = MathHelper.cos(state.limbSwingAnimationProgress * 0.6662F + (float) Math.PI)
                    * 1.4F
                    * state.limbSwingAmplitude
                    * 0.5F;
            this.rightLeg.yaw = 0.0F;
            this.leftLeg.yaw = 0.0F;
        }

        @Override
        public ModelPart getHead() {
            return this.head;
        }

        @Override
        public void setHatVisible(boolean visible) {
            this.head.visible = visible;
            this.hat.visible = visible;
            this.hatRim.visible = visible;
        }

        @Override
        public void rotateArms(MatrixStack stack) {
            this.root.applyTransform(stack);
            this.arms.applyTransform(stack);
        }

    public static TexturedModelData getTextureModelData() {
        return TexturedModelData.of(FishermanEntityModel.getModelData(), 64, 64);
    }
}
