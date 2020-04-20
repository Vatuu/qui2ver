package dev.vatuu.qui2ver.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerProvider<V> implements ICapabilityProvider {

    private final Capability<V> capability;
    private final V self;

    public PlayerProvider(Capability<V> capability, V self) {
        this.capability = capability;
        this.self = self;
    }

    public static <V> PlayerProvider<V> provide(Capability<V> capability, Object self) {
        return new PlayerProvider(capability, self);
    }

    public static <V, C extends ICapabilityProvider> void attach(ResourceLocation location, Capability<V> capability, AttachCapabilitiesEvent<C> e) {
        e.addCapability(location, provide(capability, new QuiverInventory((PlayerEntity) e.getObject())));
    }

    @Nullable
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return capability == this.capability ? (LazyOptional<T>)LazyOptional.of(() -> this.self) : LazyOptional.empty();
    }
}