package dev.vatuu.qui2ver.capability;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.function.Function;

public class QuiverSlot extends SlotItemHandler {

    private final ResourceLocation icon;
    private int limit = 64;
    private Function<IItemHandler, Boolean> enabledSupplier = (i) -> true;

    public QuiverSlot(PlayerEntity p, int index, int x, int y, ResourceLocation icon, boolean oneOnly) {
        super(IQuiverInventory.get(p), index, x, y);
        this.icon = icon;
        if(oneOnly)
            limit = 1;
    }

    public QuiverSlot(PlayerEntity p, int index, int x, int y, ResourceLocation icon, boolean oneOnly, Function<IItemHandler, Boolean> enabled) {
        super(IQuiverInventory.get(p), index, x, y);
        this.icon = icon;
        if(oneOnly)
            limit = 1;
        this.enabledSupplier = enabled;
    }


    @OnlyIn(Dist.CLIENT)
    public Pair<ResourceLocation, ResourceLocation> func_225517_c_() {
        return Pair.of(PlayerContainer.LOCATION_BLOCKS_TEXTURE, icon);
    }

    public boolean isEnabled() {
        return enabledSupplier.apply(getItemHandler());
    }

    public int getSlotStackLimit() {
        return limit;
    }
}
