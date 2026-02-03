package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.*;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetInterestedBlockTarget<E extends Mob> extends ExtendedBehaviour<E> {
    private final float speed;
    private final int arriveDistance;
    private final int repathInterval;

    private long nextSetTick = 0;

    public SetInterestedBlockTarget(float speed, int arriveDistance, int repathInterval) {
        this.speed = speed;
        this.arriveDistance = arriveDistance;
        this.repathInterval = repathInterval;
    }

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS =
            ObjectArrayList.of(
                    Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_ABSENT),
                    Pair.of(ModMemoryTypes.PLAYER_IS_SPOTTED.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.PLAYER_HURT.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get(), MemoryStatus.VALUE_PRESENT),
                    Pair.of(ModMemoryTypes.VISITED_BLOCKS.get(), MemoryStatus.REGISTERED)
            );

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    private static BlockPos getWalkablePos(ServerLevel level, BlockPos target) {
        if (!level.getBlockState(target).getCollisionShape(level, target).isEmpty()) {
            return target.above();
        }
        return target;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        var brain = entity.getBrain();
        if (gameTime < nextSetTick) return;

        nextSetTick = gameTime + repathInterval;

        MemoryModuleType<BlockPos> interestedType = ModMemoryTypes.INTERESTED_BLOCK_TARGET.get();


        BlockPos target = brain.getMemory(interestedType).orElse(null);
        BlockPos walkPos = getWalkablePos(level, target);
        if (target == null) return;

        if (entity.blockPosition().closerThan(target, arriveDistance)) {
            Set<BlockPos> visited = brain.getMemory(ModMemoryTypes.VISITED_BLOCKS.get()).orElseGet(HashSet::new);

            visited.add(target.immutable());
            brain.setMemory(ModMemoryTypes.VISITED_BLOCKS.get(), new HashSet<>(visited));

            if (visited.size() > 1024) {
                int excess = visited.size() - 1024;
                var it = visited.iterator();
                while (excess-- > 0 && it.hasNext()) {
                    it.next();
                    it.remove();
                }
            }

            brain.eraseMemory(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get());
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
            brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
            return;
        }
        boolean playerHasBeenSeen = brain.hasMemoryValue(ModMemoryTypes.PLAYER_IS_SPOTTED.get());
        boolean hasBeenHurtByPlayer = brain.hasMemoryValue(ModMemoryTypes.PLAYER_HURT.get());

        if ((playerHasBeenSeen && !hasBeenHurtByPlayer) || (!playerHasBeenSeen && !hasBeenHurtByPlayer)) {
            SteveLogic.addCuriosity(brain, gameTime, 0.05);
        }
        if (!playerHasBeenSeen && hasBeenHurtByPlayer) {
            SteveLogic.addParanoia(brain, gameTime, 0.1);
            SteveLogic.addFear(brain, gameTime, 0.01);
        }
        if (playerHasBeenSeen && hasBeenHurtByPlayer) {
            SteveLogic.addFear(brain, gameTime, 0.1);
        }

        brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(walkPos, speed, arriveDistance));
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(walkPos));
        brain.eraseMemory(ModMemoryTypes.INTERESTED_BLOCK_TARGET.get());
    }

}
