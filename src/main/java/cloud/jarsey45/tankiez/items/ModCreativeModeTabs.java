package cloud.jarsey45.tankiez.items;

import cloud.jarsey45.tankiez.Tankiez;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Tankiez.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {
    public static CreativeModeTab TANKIEZ_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {
        TANKIEZ_TAB = event.registerCreativeModeTab(
                new ResourceLocation(Tankiez.MOD_ID, "tankiez_tab"),
                builder -> builder.icon(() -> new ItemStack(ModItems.WRENCH.get()))
                        .title(Component.translatable("creativemodetab.tankiez_tab"))
        );
    }
}
