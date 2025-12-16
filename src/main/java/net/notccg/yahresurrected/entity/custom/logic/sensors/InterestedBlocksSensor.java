package net.notccg.yahresurrected.entity.custom.logic.sensors;


import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.block.Block;
import net.notccg.yahresurrected.entity.custom.logic.SteveAI.SteveInterests;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModSensorTypes;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InterestedBlocksSensor<E extends Mob> extends ExtendedSensor<E> {
    private static final int RADIUS = 16;
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(ModMemoryTypes.VISITED_BLOCKS.get(), ModMemoryTypes.INTERESTED_BLOCK_TARGET.get());

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    public int scanRate() {
        return 20;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return ModSensorTypes.INTERESTED_BLOCK_SENSOR.get();
    }

    protected void sense(ServerLevel level, E entity) {
        Brain<?> brain = entity.getBrain();

        Set<BlockPos> visited = brain
                .getMemory(ModMemoryTypes.VISITED_BLOCKS.get())
                .orElseGet(HashSet::new);
        BlockPos entityPos = entity.blockPosition();

        for (BlockPos pos : BlockPos.betweenClosed(
                entityPos.offset(-RADIUS, -4, -RADIUS),
                entityPos.offset(RADIUS, 4, RADIUS))) {
            if (visited.contains(pos)) continue;

            Block block = level.getBlockState(pos).getBlock();
            if(!SteveInterests.INTERESTED_BLOCKS.contains(block)) continue;

            brain.setMemory(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get(), pos.immutable());
            brain.setMemory(ModMemoryTypes.VISITED_BLOCKS.get(), visited);
            return;
        }
    }
}
