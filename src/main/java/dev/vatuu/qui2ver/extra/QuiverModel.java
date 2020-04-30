package dev.vatuu.qui2ver.extra;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class QuiverModel extends AgeableModel<AbstractClientPlayerEntity> {

    private final ModelRenderer element;

    public QuiverModel() {
        textureWidth = 16;
        textureHeight = 16;

        element = new ModelRenderer(this);
        element.setRotationPoint(0.0F, 24.0F, 0.0F);
        element.addBox(0, 0, 0, 16, 16, 0, 0);
    }

    protected Iterable<ModelRenderer> getHeadParts() {
        return ImmutableList.of();
    }

    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.element);
    }

    @Override
    public void render(AbstractClientPlayerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityIn.isCrouching()) {
            this.element.rotateAngleX = 0.65F;
        } else {
            this.element.rotateAngleX = 0;
        }
    }
}