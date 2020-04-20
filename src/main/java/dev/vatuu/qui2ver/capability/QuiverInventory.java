package dev.vatuu.qui2ver.capability;

import dev.vatuu.qui2ver.Qui2ver;
import dev.vatuu.qui2ver.network.SSyncQuiverInvPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class QuiverInventory extends ItemStackHandler implements IQuiverInventory {

    private PlayerEntity entity;

    public QuiverInventory(PlayerEntity p) {
        super(2);
        this.entity = p;
    }

    public QuiverInventory() {
        super(2);
    }

    @Override
    protected void onContentsChanged(int slot) {
        if(!entity.getEntityWorld().isRemote) {
            if(getStackInSlot(0).isEmpty() && !getStackInSlot(1).isEmpty()) {
                entity.dropItem(getStackInSlot(1).copy(), false, true);
                this.setStackInSlot(1, ItemStack.EMPTY);
            }
            entity.getServer().getPlayerList().getPlayers().forEach((p) -> {
                Qui2ver.SYNC_CHANNEL.sendTo(new SSyncQuiverInvPacket(entity), p.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
            });
        }
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        switch(slot) {
            case 0:
                return stack.getItem() == Qui2ver.QUIVER_ITEM.get();
            case 1:
                return stack.getItem().isIn(Tags.Items.ARROWS) && !getStackInSlot(0).isEmpty();
            default:
                return false;
        }
    }
}
