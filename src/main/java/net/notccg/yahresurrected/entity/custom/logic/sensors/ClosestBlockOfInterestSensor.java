package net.notccg.yahresurrected.entity.custom.logic.sensors;


import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.block.state.BlockState;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.PredicateSensor;

import java.util.List;
import java.util.function.Predicate;

public class ClosestBlockOfInterestSensor<E extends LivingEntity> extends PredicateSensor<BlockState, E> {
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(ModMemoryTypes.NEAREST_BLOCK_OF_INTEREST.get());
    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return null;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return null;
    }
}
