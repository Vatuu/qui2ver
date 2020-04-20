package dev.vatuu.qui2ver.capability;

import dev.vatuu.qui2ver.Qui2ver;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public final class Capabilities {

    @CapabilityInject(IQuiverInventory.class)
    public static final Capability<IQuiverInventory> QUIVER_INVENTORY_CAPABILITY = null;
    public static final ResourceLocation QUIVER_INVENTORY_ID = new ResourceLocation(Qui2ver.MODID, "quiver_inventory");
}
