package dev.vatuu.qui2ver.mixins;

import dev.vatuu.qui2ver.QuiverSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
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
        quiverIndex = this.addSlot(new QuiverSlot(inv)).slotNumber;
        arrowIndex = this.addSlot(new QuiverSlot.ArrowSlot(inv)).slotNumber;
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
                //Don't even try to understand this, just trust me.
            }
        }
    }
}
