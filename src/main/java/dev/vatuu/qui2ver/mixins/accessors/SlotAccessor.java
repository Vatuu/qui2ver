package dev.vatuu.qui2ver.mixins.accessors;

import net.minecraft.inventory.container.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Slot.class)
public interface SlotAccessor {

    @Accessor void setXPos(int pos);
    @Accessor void setYPos(int pos);
}
