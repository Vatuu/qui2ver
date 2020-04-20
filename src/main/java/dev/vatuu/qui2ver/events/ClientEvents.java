package dev.vatuu.qui2ver.events;

import dev.vatuu.qui2ver.Qui2ver;
import dev.vatuu.qui2ver.QuiverSlot;
import dev.vatuu.qui2ver.capability.IQuiverInventory;
import dev.vatuu.qui2ver.mixins.CreativeSlotAccessor;
import dev.vatuu.qui2ver.mixins.SlotAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Qui2ver.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ClientEvents {

    private static final ResourceLocation EMPTY_SLOT = new ResourceLocation("qui2ver", "textures/gui/empty_slot.png");

    @SubscribeEvent
    public static void onScreenDraw(GuiContainerEvent.DrawBackground e) {
        Screen gui = e.getGuiContainer();
        if(gui instanceof InventoryScreen) {
            int addition = ((InventoryScreen) gui).getRecipeGui().isVisible() ? 77 : 0;
            int x = gui.width / 2 - 12 + addition;
            int y = gui.height / 2 - 76;
            drawEmptySlot(x, y);
            if(!IQuiverInventory.get(gui.getMinecraft().player).getStackInSlot(0).isEmpty()) {
                int xs = gui.width / 2 - 12 + addition;
                int ys = gui.height / 2 - 58;
                drawEmptySlot(xs, ys);
            }
        } else if(gui instanceof CreativeScreen) {
            ItemGroup g = ItemGroup.GROUPS[((CreativeScreen)gui).getSelectedTabIndex()];
            if(g == ItemGroup.INVENTORY) {
                ((CreativeScreen) gui).getContainer().inventorySlots.stream()
                    .filter(slot -> slot instanceof CreativeSlotAccessor)
                    .map(CreativeSlotAccessor.class::cast)
                    .filter(slot -> slot.getSlot() instanceof QuiverSlot || slot.getSlot() instanceof QuiverSlot.ArrowSlot).forEach(s -> {
                        if(s.getSlot().getSlotIndex() == 0)
                            ((SlotAccessor)s).setXPos(127);
                        else
                            ((SlotAccessor)s).setXPos(145);
                    ((SlotAccessor)s).setYPos(20);
                });
                int x = gui.width / 2 + 29;
                int y = gui.height / 2 - 49;
                drawEmptySlot(x, y);
                if (!IQuiverInventory.get(gui.getMinecraft().player).getStackInSlot(0).isEmpty()) {
                    int xs = gui.width / 2 + 47;
                    int ys = gui.height / 2 - 49;
                    drawEmptySlot(xs, ys);
                }
            }
        }
    }

    public static void onTextureStitch(TextureStitchEvent.Pre e) {
        if(e.getMap().getBasePath() == AtlasTexture.LOCATION_BLOCKS_TEXTURE) {
            e.addSprite(QuiverSlot.QUIVER_SLOT_EMPTY);
            e.addSprite(QuiverSlot.ArrowSlot.ARROW_SLOT_EMPTY);
        }
    }

    public static void drawEmptySlot(int x, int y) {
        Minecraft.getInstance().textureManager.bindTexture(EMPTY_SLOT);
        Screen.blit(x, y, 0, 0, 18, 18, 18, 18);
    }
}
