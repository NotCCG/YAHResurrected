package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class FleeOrApproachPlayer<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private final Item cloakingItem;
    private final float baseSpeed;
    private final int fleeHorizontal;
    private final int fleeVertical;

    private final double baseFleeRadius;
    private final double fearRadiusScale;

    private long nextRepathTick = 0;

    public FleeOrApproachPlayer(Item cloakingItem,
                                float baseSpeed,
                                int fleeHorizontal,
                                int fleeVertical,
                                double baseFleeRadius,
                                double fearRadiusScale) {
        this.cloakingItem = cloakingItem;
        this.baseSpeed = baseSpeed;
        this.fleeHorizontal = fleeHorizontal;
        this.fleeVertical = fleeVertical;
        this.baseFleeRadius = baseFleeRadius;
        this.fearRadiusScale = fearRadiusScale;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
                Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.FEAR_LEVEL.get(), MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.CURIOSITY_LEVEL.get(), MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.HESITATION_COOLDOWN.get(), MemoryStatus.REGISTERED)
        );
    }

    private static BlockPos getWalkablePos(ServerLevel level, BlockPos target) {
        if (!level.getBlockState(target).getCollisionShape(level, target).isEmpty()) {
            return target.above();
        }
        return target;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        double speed = baseSpeed;
        Brain<?> brain = entity.getBrain();
        if (!brain.hasMemoryValue(ModMemoryTypes.SPOTTED_PLAYER.get())) return;
        Player player = brain.getMemory(ModMemoryTypes.SPOTTED_PLAYER.get()).orElse(null);

        if (player == null)
            return;

        // If the player has the cloaking item, steve does NOT flee
        if (player.getInventory().contains(new ItemStack(cloakingItem))) {
            entity.getNavigation().stop();
            brain.eraseMemory(ModMemoryTypes.SPOTTED_PLAYER.get());
            return;
        }

        boolean hasBeenHurtByPlayer = brain.hasMemoryValue(ModMemoryTypes.PLAYER_HURT.get());
        double fear = brain.getMemory(ModMemoryTypes.FEAR_LEVEL.get()).orElse(0.0);
        fear = SteveLogic.clampEmotion(fear);

        if (fear > 0.0 || hasBeenHurtByPlayer) {
            WalkTarget walkPos = brain.getMemory(MemoryModuleType.WALK_TARGET).orElse(null);
            if (walkPos != null) {
                BlockPos walkPosTarget = walkPos.getTarget().currentBlockPosition();
                int closeEnough = walkPos.getCloseEnoughDist();
                if (entity.blockPosition().closerThan(walkPosTarget, closeEnough)) {
                    nextRepathTick = gameTime;
                }
            }

            if (gameTime < nextRepathTick)
                return;

            nextRepathTick = gameTime + 60; // Once every 3 seconds

            Vec3 awayPos = DefaultRandomPos.getPosAway(
                    entity,
                    fleeHorizontal,
                    fleeVertical,
                    player.position()
            );
            if (awayPos == null) return;

            if (SteveLogic.isTerrified(brain) || hasBeenHurtByPlayer) {
                speed = baseSpeed * 1.3F;
            } else {
                speed = baseSpeed;
            }
            BlockPos pos = BlockPos.containing(awayPos.x, awayPos.y, awayPos.z);
            BlockPos reachablePos = getWalkablePos(level, pos);
            WalkTarget fleeTarget = new WalkTarget(reachablePos, (float) speed, 0);
            BlockPos lookPos = pos.above();

            brain.setMemory(MemoryModuleType.WALK_TARGET, fleeTarget);
            brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(lookPos));
        }
    }
}
