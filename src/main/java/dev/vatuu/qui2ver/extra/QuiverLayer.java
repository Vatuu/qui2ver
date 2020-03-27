package dev.vatuu.qui2ver.extra;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.vatuu.qui2ver.Qui2ver;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class QuiverLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

    private static final ResourceLocation TEXTURE_QUIVER = new ResourceLocation(Qui2ver.MODID, "textures/entity/quiver.png");
    private final QuiverModel modelQuiver = new QuiverModel();

    public QuiverLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> playerModelIn) {
        super(playerModelIn);
    }

    @Override
    public void render(MatrixStack mat, IRenderTypeBuffer buffer, int lights, AbstractClientPlayerEntity p, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = p.inventory.getStackInSlot(41);
        if(!stack.isEmpty() && p.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() != Items.ELYTRA && !p.isInvisible() && !p.isWearing(PlayerModelPart.CAPE)) {
            mat.push();
            this.getEntityModel().setModelAttributes(modelQuiver);
            mat.translate(-0.55D, -1.9D, 0.195D);
            if(p.isCrouching()) {
                mat.translate(0, 0.3, -0.2);
            }
            this.modelQuiver.render(p, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            IVertexBuilder consumer = buffer.getBuffer(RenderType.entityCutout(TEXTURE_QUIVER));
            this.modelQuiver.render(mat, consumer, lights, OverlayTexture.DEFAULT_LIGHT, 1.0F, 1.0F, 1.0F, 1.0F);
            mat.pop();
        }
    }
}