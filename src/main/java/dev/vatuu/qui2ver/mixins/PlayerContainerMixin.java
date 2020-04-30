package dev.vatuu.qui2ver.mixins;

import dev.vatuu.qui2ver.Qui2ver;
import dev.vatuu.qui2ver.capability.QuiverSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerContainer.class)
public abstract class PlayerContainerMixin extends RecipeBookContainer<CraftingInventory> {

    @Unique public int quiverIndex, arrowIndex;

    public PlayerContainerMixin() {
        super(null, 0);
    }

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(PlayerInventory inv, boolean local, PlayerEntity player, CallbackInfo info) {
        quiverIndex = this.addSlot(new QuiverSlot(player, 0, 77, 8, Qui2ver.QUIVER_SLOT_EMPTY, true)).slotNumber;
        arrowIndex = this.addSlot(new QuiverSlot(player, 1, 77, 26, Qui2ver.ARROW_SLOT_EMPTY, false, (i) -> !i.getStackInSlot(0).isEmpty())).slotNumber;
    }

    @Inject(at = @At(value = "CONSTANT", args = {"intValue=36"}, ordinal = 0), method = "transferStackInSlot", cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void onShift(PlayerEntity p, int index, CallbackInfoReturnable<ItemStack> info, ItemStack stack, Slot s, ItemStack stack1) {
        if(index >= 9 && index < 45) {
            boolean flag = this.mergeItemStack(stack1, quiverIndex, quiverIndex + 1, false);
            if (!flag && !this.getSlot(quiverIndex).getStack().isEmpty())
                flag = this.mergeItemStack(stack1, arrowIndex, arrowIndex + 1, false);

            if (flag) {
                if (stack1.isEmpty())
                    s.putStack(ItemStack.EMPTY);
                else
                    s.onSlotChanged();

                if (stack1.getCount() != stack.getCount())
                    s.onTake(p, stack1);

                info.setReturnValue(ItemStack.EMPTY);
            }
        }
    }
}
