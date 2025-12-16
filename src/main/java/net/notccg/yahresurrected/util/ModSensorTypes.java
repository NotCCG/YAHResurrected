package net.notccg.yahresurrected.util;

import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.custom.logic.sensors.InterestedBlocksSensor;

import java.util.function.Supplier;

public class ModSensorTypes {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES =
            DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, YouAreHerobrineResurrected.MOD_ID);

    private static <T extends Sensor<?>> Supplier<SensorType<T>> sensorType(Supplier<T> factory) {
        return () -> new SensorType<>(factory);
    }

    public static final RegistryObject<SensorType<InterestedBlocksSensor<?>>> INTERESTED_BLOCK_SENSOR =
            SENSOR_TYPES.register("interestedblocksensor", sensorType(InterestedBlocksSensor::new)
        );
}
