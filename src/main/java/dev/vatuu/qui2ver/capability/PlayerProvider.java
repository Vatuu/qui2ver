package dev.vatuu.qui2ver.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerProvider implements ICapabilitySerializable<INBT> {

    private final Capability<IQuiverInventory> capability;
    private final IQuiverInventory self;

    public PlayerProvider(Capability<IQuiverInventory> capability, IQuiverInventory self) {
        this.capability = capability;
        this.self = self;
    }

    public static PlayerProvider provide(Capability<IQuiverInventory> capability, IQuiverInventory self) {
        return new PlayerProvider(capability, self);
    }

    public static <C extends ICapabilityProvider> void attach(ResourceLocation location, Capability<IQuiverInventory> capability, AttachCapabilitiesEvent<C> e) {
        e.addCapability(location, provide(capability, new QuiverInventory((PlayerEntity) e.getObject())));
    }

    @Nullable
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return capability == this.capability ? (LazyOptional<T>)LazyOptional.of(() -> this.self) : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return capability.writeNBT(self, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        this.capability.readNBT(self, null, nbt);
    }
}