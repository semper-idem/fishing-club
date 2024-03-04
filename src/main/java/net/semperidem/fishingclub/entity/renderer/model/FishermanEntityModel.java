package net.semperidem.fishingclub.entity.renderer.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.entity.Entity;

public class FishermanEntityModel<T extends Entity> extends VillagerResemblingModel<T> {
    public FishermanEntityModel(ModelPart root) {
        super(root);
    }


    public static TexturedModelData getTextureModelData() {
        return TexturedModelData.of(FishermanEntityModel.getModelData(), 64, 64);
    }
}
