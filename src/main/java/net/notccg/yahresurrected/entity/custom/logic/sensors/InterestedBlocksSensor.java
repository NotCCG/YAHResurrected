package net.notccg.yahresurrected.entity.custom.logic.sensors;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.block.Block;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModSensorTypes;
import net.notccg.yahresurrected.util.ModTags;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InterestedBlocksSensor<E extends PathfinderMob> extends ExtendedSensor<E> {
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get(), ModMemoryTypes.VISITED_BLOCKS.get());
    private static final int RADIUS = 16;
    private static final int Y_RADIUS = 4;
    private static final int SCAN_INTERVAL_TICKS = 20;

    private long nextScanTick = 0;

    public InterestedBlocksSensor() {}

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return ModSensorTypes.INTERESTED_BLOCK_SENSOR.get();
    }

    @Override
    protected void doTick(ServerLevel level, E entity) {
        long gameTime = level.getGameTime();
        if (gameTime < nextScanTick)
            return;

        nextScanTick = gameTime + SCAN_INTERVAL_TICKS;

        var brain = entity.getBrain();

        MemoryModuleType<Set<BlockPos>> visitedType = ModMemoryTypes.VISITED_BLOCKS.get();
        MemoryModuleType<BlockPos> targetType = ModMemoryTypes.INTERESTED_BLOCK_TARGET.get();

        // If already targeting a block, don't pick a new one
        if (brain.hasMemoryValue(targetType))
            return;

        BlockPos origin = entity.blockPosition();
        Set<BlockPos> visited = brain.getMemory(visitedType).orElseGet(HashSet::new);

        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-RADIUS, -Y_RADIUS, -RADIUS),
                origin.offset(RADIUS,  Y_RADIUS,  RADIUS))) {

            BlockPos immutablePos = pos.immutable();

            if (visited.contains(immutablePos))
                continue;

            Block block = level.getBlockState(immutablePos).getBlock();
            if (!SteveLogic.INTERESTED_BLOCKS.contains(block))
                continue;

            brain.setMemory(targetType, immutablePos);
            brain.setMemory(visitedType, new HashSet<>(visited));

            if (level.getBlockState(immutablePos).is(ModTags.Blocks.CONTAINER_BLOCK)) {
                brain.setMemory(ModMemoryTypes.CONTAINER_BLOCK.get(), immutablePos);
                SteveLogic.addCuriosity(brain, gameTime, 0.05);
                return;
            }

            return;
        }

        if (!brain.hasMemoryValue(visitedType))
            brain.setMemory(visitedType, new HashSet<>(visited));
    }
}