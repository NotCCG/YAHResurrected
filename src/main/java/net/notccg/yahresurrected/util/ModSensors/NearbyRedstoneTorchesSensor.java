package net.notccg.yahresurrected.util.ModSensors;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.List;

public class NearbyRedstoneTorchesSensor<E extends LivingEntity> extends ExtendedSensor {
    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return List.of();
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return null;
    }
}
