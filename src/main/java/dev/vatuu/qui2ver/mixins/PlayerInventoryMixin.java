package dev.vatuu.qui2ver.mixins;

import com.google.common.collect.ImmutableList;
import dev.vatuu.qui2ver.capability.IQuiverInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
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

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements IInventory, INameable {

    @Shadow @Mutable
    private List<NonNullList<ItemStack>> allInventories;
    public final NonNullList<ItemStack> quiverInventory = NonNullList.withSize(2, ItemStack.EMPTY);
    private IQuiverInventory capability;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(PlayerEntity p, CallbackInfo info) {
        List<NonNullList<ItemStack>> list = new ArrayList<>(allInventories);
        list.add(quiverInventory);
        this.allInventories = ImmutableList.copyOf(list);
        this.capability = IQuiverInventory.get(p);
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
