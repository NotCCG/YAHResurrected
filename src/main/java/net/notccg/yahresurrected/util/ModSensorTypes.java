package net.notccg.yahresurrected.util;

import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.custom.logic.sensors.ClosestBlockOfInterestSensor;

public class ModSensorTypes {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.Keys.SENSOR_TYPES, YouAreHerobrineResurrected.MOD_ID);

    public static final RegistryObject<SensorType<ClosestBlockOfInterestSensor<?>>> CLOSEST_BLOCK_OF_INTEREST = SENSOR_TYPES.register("interested_block", () -> new SensorType<>(ClosestBlockOfInterestSensor::new));
}
