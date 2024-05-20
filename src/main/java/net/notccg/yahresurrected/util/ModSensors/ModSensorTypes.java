package net.notccg.yahresurrected.util.ModSensors;

import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;

public class ModSensorTypes {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES =
        DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, YouAreHerobrineResurrected.MOD_ID);

    public static void register(IEventBus eventBus) {
        SENSOR_TYPES.register(eventBus);
    }
}
