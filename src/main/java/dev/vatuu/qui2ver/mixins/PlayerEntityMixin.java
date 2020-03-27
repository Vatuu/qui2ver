package dev.vatuu.qui2ver.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow @Final public PlayerInventory inventory;

    @Shadow @Final public PlayerContainer container;

    public PlayerEntityMixin(World worldIn, GameProfile gameProfileIn) {
        super(EntityType.PLAYER, worldIn);
    }

    @Inject(at = @At(value = "INVOKE_ASSIGN", shift = At.Shift.BEFORE, target = "Lnet/minecraft/item/ShootableItem;getAmmoPredicate()Ljava/util/function/Predicate;"), method = "findAmmo", cancellable = true)
    private void findAmmo(ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
        ItemStack arrowSlot = this.inventory.getStackInSlot(42);
        if(!arrowSlot.isEmpty()) {
            info.setReturnValue(arrowSlot);
        }
    }
}
