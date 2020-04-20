package dev.vatuu.qui2ver.network;

import dev.vatuu.qui2ver.capability.IQuiverInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SSyncQuiverInvPacket  {

    private final List<ItemStack> stacks = new ArrayList<>();;
    private int entityId;

    public SSyncQuiverInvPacket(PlayerEntity e) {
        IQuiverInventory inv = IQuiverInventory.get(e);

        for (int i = 0; i < inv.getSlots(); i++)
            stacks.add(inv.getStackInSlot(i));

        this.entityId = e.getEntityId();
    }

    public SSyncQuiverInvPacket(PacketBuffer buf) {
        this.entityId = buf.readVarInt();
        int size = buf.readVarInt();
        for (int i = 0; i < size; i++) {
            stacks.add(buf.readItemStack());
        }
    }

    public void encode(PacketBuffer buf) {
        buf.writeVarInt(entityId);
        buf.writeVarInt(stacks.size());
        for (int i = 0; i < stacks.size(); i++)
            buf.writeItemStack(stacks.get(i));
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity e = Minecraft.getInstance().world.getEntityByID(entityId);
            if(!(e instanceof PlayerEntity))
                return;
            IQuiverInventory inv = IQuiverInventory.get((PlayerEntity)e);
            int size = Math.min(inv.getSlots(), stacks.size());
            for (int i = 0; i < size; i++)
                inv.setStackInSlot(i, stacks.get(i));
        });
        ctx.get().setPacketHandled(true);
    }
}
