package dev.vatuu.qui2ver;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class QuiverSlot extends Slot {

    public static final ResourceLocation QUIVER_SLOT_EMPTY = new ResourceLocation("qui2ver", "item/empty_quiver_slot");

    public QuiverSlot(IInventory inv) {
        super(inv, 41, 77, 8);
    }

    @OnlyIn(Dist.CLIENT)
    public Pair<ResourceLocation, ResourceLocation> func_225517_c_() {
        return Pair.of(PlayerContainer.LOCATION_BLOCKS_TEXTURE, QUIVER_SLOT_EMPTY);
    }

    public int getSlotStackLimit() {
        return 1;
    }

    public boolean isItemValid(ItemStack stack) {
        return stack.getItem().equals(Qui2ver.quiverItem.get());
    }

    public boolean canTakeStack(PlayerEntity p) {
        p.dropItem(this.inventory.removeStackFromSlot(42), false, true);
        return true;
    }

    public static class ArrowSlot extends Slot {

        public static final ResourceLocation ARROW_SLOT_EMPTY = new ResourceLocation("qui2ver", "item/empty_arrow_slot");

        public ArrowSlot(IInventory inv) {
            super(inv, 42, 77, 26);
        }

        @Nullable
        @OnlyIn(Dist.CLIENT)
        public Pair<ResourceLocation, ResourceLocation> func_225517_c_() {
            return Pair.of(PlayerContainer.LOCATION_BLOCKS_TEXTURE, ARROW_SLOT_EMPTY);
        }

        public boolean isEnabled() {
            return !inventory.getStackInSlot(41).isEmpty();
        }

        public boolean isItemValid(ItemStack stack) {
            return stack.getItem() instanceof ArrowItem;
        }
    }
}
