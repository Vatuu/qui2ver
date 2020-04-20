package dev.vatuu.qui2ver.network;

import dev.vatuu.qui2ver.capability.IQuiverInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class QuiverListener implements IContainerListener {

    private int quiverIndex, arrowIndex;
    private PlayerEntity p;

    public QuiverListener(int quiver, int slotIndex, PlayerEntity e) {
        this.quiverIndex = quiver;
        this.arrowIndex = slotIndex;
        this.p = e;
    }

    @Override
    public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {
        ItemStack quiver = itemsList.get(quiverIndex);
        ItemStack arrows = itemsList.get(arrowIndex);

        IQuiverInventory cap = IQuiverInventory.get(p);
        cap.setStackInSlot(0, quiver);
        cap.setStackInSlot(1, arrows);
    }

    @Override
    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
        IQuiverInventory cap = IQuiverInventory.get(p);
        if(slotInd == quiverIndex)
            cap.setStackInSlot(0, stack);
        else if(slotInd == arrowIndex)
            cap.setStackInSlot(1, stack);
    }

    @Override
    public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) { }
}
