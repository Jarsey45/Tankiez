package cloud.jarsey45.tankiez;

import cloud.jarsey45.tankiez.blocks.ModBlocks;
import cloud.jarsey45.tankiez.blocks.entity.ModBlockEntities;
import cloud.jarsey45.tankiez.items.ModCreativeModeTabs;
import cloud.jarsey45.tankiez.items.ModItems;
import cloud.jarsey45.tankiez.networking.ModNetworking;
import cloud.jarsey45.tankiez.screen.BasicTankScreen;
import cloud.jarsey45.tankiez.screen.ModMenuTypes;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Tankiez.MOD_ID)
public class Tankiez {
	public static final String MOD_ID = "tankiez";
	private static final Logger LOGGER = LogUtils.getLogger();

	public Tankiez() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		//registers
		ModCreativeModeTabs.register(modEventBus);

		ModItems.register(modEventBus);
		ModBlocks.register(modEventBus);
		ModBlockEntities.register(modEventBus);
		ModMenuTypes.register(modEventBus);

		modEventBus.addListener(this::commonSetup);

		MinecraftForge.EVENT_BUS.register(this);

		modEventBus.addListener(this::addCreativeTab);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {

		ModNetworking.register();
	}

	private void addCreativeTab(BuildCreativeModeTabContentsEvent event) {
		if (event.getTab() == ModCreativeModeTabs.TANKIEZ_TAB.get()) {
			event.accept(ModItems.DIAZULI_WRENCH);
			event.accept(ModBlocks.DIAZULI_GLASS);
			event.accept(ModBlocks.BASIC_TANK);
		}
	}

	@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			MenuScreens.register(ModMenuTypes.BASIC_TANK_MENU.get(), BasicTankScreen::new);
		}
	}
}
