package dev.vatuu.qui2ver.mixins;

import dev.vatuu.qui2ver.extra.QuiverLayer;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

    public PlayerRendererMixin(EntityRendererManager man) {
        super(man, new PlayerModel<>(0.0F, false), 0.5F);
    }

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererManager;Z)V")
    public void init(EntityRendererManager manager, boolean smallArms, CallbackInfo info) {
        this.addLayer(new QuiverLayer(this));
    }
}
