package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class SteveRandomLook<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final int MIN_TICKS = 10;
    private static final int MAX_TICKS = 1200;

    private long nextLookAroundTick = 0;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT),
                Pair.of(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get(), MemoryStatus.VALUE_ABSENT),
                Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_ABSENT)
        );
    }

    public static BlockPos randomBlockPosInFov(Mob mob, int minDist, int maxDist, float yawFovDeg, float pitchFovDeg) {
        // Random distance
        int dist = Mth.nextInt(mob.getRandom(), Math.max(1, minDist), Math.max(minDist, maxDist));
        float yawOffset = (mob.getRandom().nextFloat() - 0.5f) * yawFovDeg;       // left/right
        float pitchOffset = (mob.getRandom().nextFloat() - 0.5f) * pitchFovDeg;   // up/down
        float yaw = mob.getYHeadRot() + yawOffset;
        float pitch = Mth.clamp(mob.getXRot() + pitchOffset, -90.0f, 90.0f);

        // Convert yaw/pitch to a direction vector
        float yawRad = yaw * ((float)Math.PI / 180.0f);
        float pitchRad = pitch * ((float)Math.PI / 180.0f);

        double x = -Mth.sin(yawRad) * Mth.cos(pitchRad);
        double y = -Mth.sin(pitchRad);
        double z =  Mth.cos(yawRad) * Mth.cos(pitchRad);

        Vec3 start = mob.getEyePosition();
        Vec3 end = start.add(x * dist, y * dist, z * dist);

        return BlockPos.containing(end);
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        var brain = entity.getBrain();

        if (gameTime < nextLookAroundTick) return;
        int nextInterval = entity.getRandom().nextInt(
                MAX_TICKS - MIN_TICKS + 1
        ) + MIN_TICKS;

        BlockPos newLookPos =  randomBlockPosInFov(entity, 1, 32, 60, 85);
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(newLookPos));
    }

    @Override
    protected void stop(E entity) {
        entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }
}
