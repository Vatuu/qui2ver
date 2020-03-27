package dev.vatuu.qui2ver.mixins;

import dev.vatuu.qui2ver.Qui2ver;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends DisplayEffectsScreen<PlayerContainer> implements IRecipeShownListener {

    @Shadow @Final private RecipeBookGui recipeBookGui;

    public InventoryScreenMixin(PlayerEntity p) { super(p.container, p.inventory, new TranslationTextComponent("container.crafting")); }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/inventory/InventoryScreen;blit(IIIIII)V", shift = At.Shift.AFTER), method = "drawGuiContainerBackgroundLayer")
    private void drawBackgroundLayer(float ticks, int mouseX, int mouseY, CallbackInfo info) {
        int addition = this.recipeBookGui.isVisible() ? 77 : 0;
        int x = this.width / 2 - 12 + addition;
        int y = this.height / 2 - 76;
        Qui2ver.drawEmptySlot(x, y);
        if(!this.playerInventory.getStackInSlot(41).isEmpty()) {
            int xs = this.width / 2 - 12 + addition;
            int ys = this.height / 2 - 58;
            Qui2ver.drawEmptySlot(xs, ys);
        }
    }
}
