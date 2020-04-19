package dev.vatuu.qui2ver;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


@Mod(Qui2ver.MODID)
@Mod.EventBusSubscriber(modid = Qui2ver.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Qui2ver {

	public static final String MODID = "qui2ver";

	private static final ResourceLocation EMPTY_SLOT = new ResourceLocation("qui2ver", "textures/gui/empty_slot.png");
	private static final DeferredRegister<Item> REGISTER = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
	public static final RegistryObject<Item> QUIVER_ITEM = REGISTER.register("quiver", () -> new QuiverItem(new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1)));

	public Qui2ver() {
		REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static void drawEmptySlot(int x, int y) {
		Minecraft.getInstance().textureManager.bindTexture(EMPTY_SLOT);
		Screen.blit(x, y, 0, 0, 18, 18, 18, 18);
	}

	@SubscribeEvent
	public static void onPickup(EntityItemPickupEvent e) {
		ItemStack itemstack = e.getItem().getItem();
		if(!itemstack.getItem().isIn(Tags.Items.ARROWS))
			return;

		ItemStack slot = e.getPlayer().inventory.getStackInSlot(42);
		if(slot.isEmpty() || ItemStack.areItemsEqual(itemstack, slot)) {
			int remains = slot.getMaxStackSize() - slot.getCount();
			ItemStack put = itemstack.split(remains);
			slot.grow(put.getCount());
			if(itemstack.isEmpty())
				e.getPlayer().onItemPickup(e.getItem(), put.getCount());
		}
	}
}


