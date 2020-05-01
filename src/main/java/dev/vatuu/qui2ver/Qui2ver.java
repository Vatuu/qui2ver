package dev.vatuu.qui2ver;

import dev.vatuu.qui2ver.capability.IQuiverInventory;
import dev.vatuu.qui2ver.capability.QuiverInventory;
import dev.vatuu.qui2ver.events.ClientEvents;
import dev.vatuu.qui2ver.extra.QuiverLayer;
import dev.vatuu.qui2ver.network.SSyncQuiverInvPacket;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Qui2ver.MODID)
@Mod.EventBusSubscriber(modid = Qui2ver.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Qui2ver {

	/*
	TODO: Locked Creative Slots
	 */

	public static final String MODID = "qui2ver";

	public static final ResourceLocation QUIVER_SLOT_EMPTY = new ResourceLocation("qui2ver", "item/empty_quiver_slot");
	public static final ResourceLocation ARROW_SLOT_EMPTY = new ResourceLocation("qui2ver", "item/empty_arrow_slot");

	public static final String CHANNEL_VERSION = "1.0";
	public static final SimpleChannel SYNC_CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "sync"))
			.networkProtocolVersion(() -> CHANNEL_VERSION)
			.clientAcceptedVersions(CHANNEL_VERSION::equals)
			.serverAcceptedVersions(CHANNEL_VERSION::equals)
			.simpleChannel();

	private static final DeferredRegister<Item> REGISTER = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
	public static final RegistryObject<Item> QUIVER_ITEM = REGISTER.register("quiver", () -> new QuiverItem(new Item.Properties().group(ItemGroup.COMBAT).maxStackSize(1)));

	public Qui2ver() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		REGISTER.register(bus);
		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);
		bus.addListener(ClientEvents::onTextureStitch);
	}

	public void commonSetup(FMLCommonSetupEvent e) {
		CapabilityManager.INSTANCE.register(IQuiverInventory.class, new QuiverInventoryStorage(), QuiverInventory::new);

		SYNC_CHANNEL.messageBuilder(SSyncQuiverInvPacket.class, 0)
				.encoder(SSyncQuiverInvPacket::encode)
				.decoder(SSyncQuiverInvPacket::new)
				.consumer(SSyncQuiverInvPacket::handle)
				.add();
	}

	public void clientSetup(FMLClientSetupEvent e) {
		e.getMinecraftSupplier().get().getRenderManager().getSkinMap().forEach(($, r) -> r.addLayer(new QuiverLayer(r)));
	}

	static class QuiverInventoryStorage implements Capability.IStorage<IQuiverInventory> {

		public INBT writeNBT(Capability<IQuiverInventory> capability, IQuiverInventory instance, Direction side) {
			return ((ItemStackHandler)instance).serializeNBT();
		}

		public void readNBT(Capability<IQuiverInventory> capability, IQuiverInventory instance, Direction side, INBT nbt) {
			((ItemStackHandler) instance).deserializeNBT((CompoundNBT) nbt);
		}
	}
}


