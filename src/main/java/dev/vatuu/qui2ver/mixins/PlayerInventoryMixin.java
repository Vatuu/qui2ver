package dev.vatuu.qui2ver.mixins;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.INameable;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements IInventory, INameable {

    @Shadow @Mutable
    private List<NonNullList<ItemStack>> allInventories;
    public final NonNullList<ItemStack> quiverInventory = NonNullList.withSize(2, ItemStack.EMPTY);

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(PlayerEntity p, CallbackInfo info) {
        List<NonNullList<ItemStack>> list = new ArrayList<>(allInventories);
        list.add(quiverInventory);
        this.allInventories = ImmutableList.copyOf(list);
    }

    @Inject(at = @At(value = "RETURN"), method = "write", cancellable = true)
    private void write(ListNBT nbt, CallbackInfoReturnable<ListNBT> info) {
        ListNBT ret = info.getReturnValue();

        for(int i = 0; i < this.quiverInventory.size(); ++i)
            if (!this.quiverInventory.get(i).isEmpty()) {
                CompoundNBT tag = new CompoundNBT();
                tag.putByte("Slot", (byte)(i + 160));
                this.quiverInventory.get(i).write(tag);
                ret.add(tag);
            }

        info.setReturnValue(ret);
    }

    @Inject(at = @At("HEAD"), method = "read")
    private void read(ListNBT nbt, CallbackInfo info) {
        quiverInventory.clear();
    }

    @Inject(at = @At(value = "INVOKE_ASSIGN", shift = At.Shift.AFTER, target = "Lnet/minecraft/item/ItemStack;read(Lnet/minecraft/nbt/CompoundNBT;)Lnet/minecraft/item/ItemStack;"), method = "read", locals = LocalCapture.CAPTURE_FAILHARD)
    private void readLater(ListNBT nbt, CallbackInfo info, int index, CompoundNBT compoundnbt, int slot, ItemStack stack) {
        if(!stack.isEmpty() &&  slot >= 160 && slot <= quiverInventory.size() + 160) {
            this.quiverInventory.set(slot - 160, stack);
        }
    }

    @Inject(at = @At("RETURN"), method = "getSizeInventory", cancellable = true)
    private void getSizeInventory(CallbackInfoReturnable<Integer> info) {
        info.setReturnValue(info.getReturnValue() + quiverInventory.size());
    }

    @Inject(at = @At("RETURN"), method = "isEmpty")
    private void isEmpty(CallbackInfoReturnable<Boolean> info) {
        for(ItemStack stack : this.quiverInventory) {
            if (!stack.isEmpty()) {
                info.setReturnValue(false);
            }
        }
    }
}
