package net.notccg.yahresurrected.util;

import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.custom.logic.sensors.*;

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

    public static final RegistryObject<SensorType<SpotPlayerSensor<?>>> SPOT_PLAYER_SENSOR =
            SENSOR_TYPES.register("spotplayersensor", sensorType(SpotPlayerSensor::new));

    public static final RegistryObject<SensorType<PlayerSoundsSensor<?>>> PLAYER_SOUND_SENSOR =
            SENSOR_TYPES.register("playersoundssensor", sensorType(PlayerSoundsSensor::new));

    public static final RegistryObject<SensorType<InterestedItemsSensor<?>>> INTERESTED_ITEMS_SENSOR =
            SENSOR_TYPES.register("interesteditemssensor", sensorType(InterestedItemsSensor::new));

    public static final RegistryObject<SensorType<NearestUnoccupiedBedSensor<?>>> UNOCCUPIED_BED_SENSOR =
            SENSOR_TYPES.register("unoccupiedbedsensor", sensorType(NearestUnoccupiedBedSensor::new));
}
