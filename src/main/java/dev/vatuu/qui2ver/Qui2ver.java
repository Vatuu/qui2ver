package dev.vatuu.qui2ver;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


@Mod(Qui2ver.MODID)
public class Qui2ver {

	public static final String MODID = "qui2ver";

	private static final ResourceLocation EMPTY_SLOT = new ResourceLocation("qui2ver", "textures/gui/empty_slot.png");
	private static final DeferredRegister<Item> register = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
	public static final RegistryObject<Item> quiverItem = register.register("quiver", () -> new Item(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1)));

	public Qui2ver() {
		register.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static void drawEmptySlot(int x, int y) {
		Minecraft.getInstance().textureManager.bindTexture(EMPTY_SLOT);
		Screen.blit(x, y, 0, 0, 18, 18, 18, 18);
	}
}


