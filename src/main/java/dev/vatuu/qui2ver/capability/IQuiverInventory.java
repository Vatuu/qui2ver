package dev.vatuu.qui2ver.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IQuiverInventory extends IItemHandlerModifiable {

    static IQuiverInventory get(PlayerEntity p) {
        return p.getCapability(Capabilities.QUIVER_INVENTORY_CAPABILITY).orElseThrow(() -> new NullPointerException("Capability"));
    }
}
