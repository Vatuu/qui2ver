package dev.vatuu.qui2ver.mixins;

import dev.vatuu.qui2ver.Qui2ver;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Collectors;

@Mixin(CreativeScreen.class)
public abstract class CreativeScreenMixin extends DisplayEffectsScreen<CreativeScreen.CreativeContainer> {

    @Shadow public abstract int getSelectedTabIndex();

    public CreativeScreenMixin() {
        super(null, null, null);
    }

    @Inject(at = @At("RETURN"), method = "drawGuiContainerBackgroundLayer")
    private void onDraw(float ticks, int mouseX, int mouseY, CallbackInfo info) {
        if(ItemGroup.GROUPS[this.getSelectedTabIndex()] == ItemGroup.INVENTORY) {
            int x = this.width / 2 + 29;
            int y = this.height / 2 - 49;
            Qui2ver.drawEmptySlot(x, y);
            if (!this.playerInventory.getStackInSlot(41).isEmpty()) {
                int xs = this.width / 2 + 47;
                int ys = this.height / 2 - 49;
                Qui2ver.drawEmptySlot(xs, ys);
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "setCurrentCreativeTab")
    private void onSetTab(ItemGroup group, CallbackInfo info) {
        if(group == ItemGroup.INVENTORY) {
            this.container.inventorySlots.stream()
                    .filter(slot -> slot.getSlotIndex() == 41 || slot.getSlotIndex() == 42).forEach(s -> {
                        if(s.getSlotIndex() == 41) {
                            ((SlotAccessor)s).setXPos(127);
                            ((SlotAccessor)s).setYPos(20);
                        } else {
                            ((SlotAccessor)s).setXPos(145);
                            ((SlotAccessor)s).setYPos(20);
                        }
            });
        }
    }
}
