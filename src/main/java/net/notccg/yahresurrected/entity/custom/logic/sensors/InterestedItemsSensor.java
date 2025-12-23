package net.notccg.yahresurrected.entity.custom.logic.sensors;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModSensorTypes;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.List;

public class InterestedItemsSensor<E extends PathfinderMob> extends ExtendedSensor<E> {
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(
            ModMemoryTypes.INTERESTED_ITEM_LOCATION.get(),
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.WALK_TARGET
    );

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return ModSensorTypes.INTERESTED_ITEMS_SENSOR.get();
    }

    @Override
    protected void doTick(ServerLevel level, E entity) {
        super.doTick(level, entity);
    }
}