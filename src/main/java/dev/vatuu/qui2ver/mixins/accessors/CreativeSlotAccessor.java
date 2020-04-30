package dev.vatuu.qui2ver.mixins.accessors;

import net.minecraft.inventory.container.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net/minecraft/client/gui/screen/inventory/CreativeScreen$CreativeSlot")
public interface CreativeSlotAccessor {

    @Accessor Slot getSlot();
}
