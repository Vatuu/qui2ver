package dev.vatuu.qui2ver.mixins;

import dev.vatuu.qui2ver.QuiverSlot;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.AtlasTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(at = @At(value = "RETURN"), method = "lambda$static$2")
    private static void iHateForge(HashSet<Material> set, CallbackInfo info) {
        set.add(new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, QuiverSlot.QUIVER_SLOT_EMPTY));
        set.add(new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, QuiverSlot.ArrowSlot.ARROW_SLOT_EMPTY));
    }
}
