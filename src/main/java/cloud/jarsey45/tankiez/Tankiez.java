package cloud.jarsey45.tankiez;

import cloud.jarsey45.tankiez.blocks.ModBlocks;
import cloud.jarsey45.tankiez.items.ModCreativeModeTabs;
import cloud.jarsey45.tankiez.items.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
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
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreativeTab);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreativeTab(CreativeModeTabEvent.BuildContents event) {
        if(event.getTab() == ModCreativeModeTabs.TANKIEZ_TAB) {
            event.accept(ModItems.WRENCH);
            event.accept(ModBlocks.DIAZULI_GLASS);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}
