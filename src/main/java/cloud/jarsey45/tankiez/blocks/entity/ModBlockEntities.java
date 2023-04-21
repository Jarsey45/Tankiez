package cloud.jarsey45.tankiez.blocks.entity;

import cloud.jarsey45.tankiez.Tankiez;
import cloud.jarsey45.tankiez.blocks.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Tankiez.MOD_ID);

    public static final RegistryObject<BlockEntityType<BasicTankEntity>> BASIC_TANK =
            BLOCK_ENTITIES.register("basic_tank", () ->
                    BlockEntityType.Builder.of(BasicTankEntity::new,
                            ModBlocks.BASIC_TANK.get()).build(null)
            );
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
