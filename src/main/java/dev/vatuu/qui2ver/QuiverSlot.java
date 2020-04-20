package dev.vatuu.qui2ver;

import com.mojang.datafixers.util.Pair;
import dev.vatuu.qui2ver.capability.IQuiverInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class QuiverSlot extends SlotItemHandler {

    public static final ResourceLocation QUIVER_SLOT_EMPTY = new ResourceLocation("qui2ver", "item/empty_quiver_slot");

    public QuiverSlot(PlayerEntity p) {
        super(IQuiverInventory.get(p), 0, 77, 8);
    }

    @OnlyIn(Dist.CLIENT)
    public Pair<ResourceLocation, ResourceLocation> func_225517_c_() {
        return Pair.of(PlayerContainer.LOCATION_BLOCKS_TEXTURE, QUIVER_SLOT_EMPTY);
    }

    public int getSlotStackLimit() {
        return 1;
    }

    public boolean isItemValid(ItemStack stack) {
        return stack.getItem().equals(Qui2ver.QUIVER_ITEM.get());
    }

    public static class ArrowSlot extends SlotItemHandler {

        public static final ResourceLocation ARROW_SLOT_EMPTY = new ResourceLocation("qui2ver", "item/empty_arrow_slot");

        public ArrowSlot(PlayerEntity p) {
            super(IQuiverInventory.get(p), 1, 77, 26);
        }

        @Nullable
        @OnlyIn(Dist.CLIENT)
        public Pair<ResourceLocation, ResourceLocation> func_225517_c_() {
            return Pair.of(PlayerContainer.LOCATION_BLOCKS_TEXTURE, ARROW_SLOT_EMPTY);
        }

        public boolean isEnabled() {
            return !getItemHandler().getStackInSlot(0).isEmpty();
        }
    }
}
