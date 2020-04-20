package dev.vatuu.qui2ver.events;

import dev.vatuu.qui2ver.Qui2ver;
import dev.vatuu.qui2ver.capability.Capabilities;
import dev.vatuu.qui2ver.capability.IQuiverInventory;
import dev.vatuu.qui2ver.capability.PlayerProvider;
import dev.vatuu.qui2ver.network.SSyncQuiverInvPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = Qui2ver.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CommonEvents {

    @SubscribeEvent
    public static void onCapabilityAttach(AttachCapabilitiesEvent<Entity> e) {
        if(e.getObject() instanceof PlayerEntity)
            PlayerProvider.attach(Capabilities.QUIVER_INVENTORY_ID, Capabilities.QUIVER_INVENTORY_CAPABILITY, e);
    }

    @SubscribeEvent
    public static void onPickup(EntityItemPickupEvent e) {
        ItemStack itemstack = e.getItem().getItem();
        IQuiverInventory inv = IQuiverInventory.get(e.getPlayer());
        ItemStack slot = inv.insertItem(1, itemstack, false);
        itemstack.setCount(slot.getCount());
        if(slot.isEmpty() || !ItemStack.areItemStacksEqual(itemstack, slot)) {
            if(slot.isEmpty())
                e.getPlayer().onItemPickup(e.getItem(), slot.getCount());
        }
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        if(!e.getPlayer().world.isRemote())
            Qui2ver.SYNC_CHANNEL.sendTo(new SSyncQuiverInvPacket(e.getPlayer()), ((ServerPlayerEntity)e.getPlayer()).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone e) {
        e.getOriginal().revive(); // ¯\_(ツ)_/¯
        IQuiverInventory orCap = IQuiverInventory.get(e.getOriginal());
        IQuiverInventory newCap = IQuiverInventory.get(e.getPlayer());
        for (int i = 0; i < orCap.getSlots(); i++)
            newCap.setStackInSlot(i, orCap.getStackInSlot(i).copy());
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent e) {
        if(!(e.getEntity() instanceof PlayerEntity) || e.getEntity().world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY) || e.getEntity().isSpectator())
            return;
        IQuiverInventory cap = IQuiverInventory.get((PlayerEntity)e.getEntity());
        Collection<ItemEntity> old = e.getEntity().captureDrops(e.getDrops());
        for (int i = 0; i < cap.getSlots(); i++) {
            ((PlayerEntity) e.getEntity()).dropItem(cap.getStackInSlot(i), true, true);
            cap.setStackInSlot(i, ItemStack.EMPTY);
        }
        e.getEntity().captureDrops(old);
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking e) {
        if(!(e.getTarget() instanceof PlayerEntity) || e.getPlayer().world.isRemote)
            return;

        Qui2ver.SYNC_CHANNEL.sendTo(new SSyncQuiverInvPacket((PlayerEntity)e.getTarget()), ((ServerPlayerEntity)e.getPlayer()).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }
}
