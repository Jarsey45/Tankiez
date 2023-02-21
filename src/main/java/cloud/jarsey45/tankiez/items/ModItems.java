package cloud.jarsey45.tankiez.items;

import cloud.jarsey45.tankiez.Tankiez;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Tankiez.MOD_ID);

    public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", WrenchItem::new);
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
