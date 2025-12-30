package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
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
    private final double baseSpeed;
    private final int fleeHorizontal;
    private final int fleeVertical;

    private final double baseFleeRadius;
    private final double fearRadiusScale;

    private final double fearOnSpot;
    private final int fearSpotIntervalTicks;

    private long nextRepathTick = 0;
    private long nextSpotFearTick = 0;

    public FleeOrApproachPlayer(Item cloakingItem,
                                double baseSpeed,
                                int fleeHorizontal,
                                int fleeVertical,
                                double baseFleeRadius,
                                double fearRadiusScale,
                                double fearOnSpot,
                                int fearSpotIntervalTicks) {
        this.cloakingItem = cloakingItem;
        this.baseSpeed = baseSpeed;
        this.fleeHorizontal = fleeHorizontal;
        this.fleeVertical = fleeVertical;
        this.baseFleeRadius = baseFleeRadius;
        this.fearRadiusScale = fearRadiusScale;
        this.fearOnSpot = fearOnSpot;
        this.fearSpotIntervalTicks = fearSpotIntervalTicks;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_PRESENT),
                Pair.of(ModMemoryTypes.FEAR_LEVEL.get(), MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.CURIOSITY_LEVEL.get(), MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.HESITATION_COOLDOWN.get(), MemoryStatus.VALUE_PRESENT)
        );
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        this.nextRepathTick = 0;
        double speed = baseSpeed;
        Brain<?> brain = entity.getBrain();
        Player player = brain.getMemory(ModMemoryTypes.SPOTTED_PLAYER.get()).orElse(null);


        if (player == null)
            return;

        if (player.getInventory().contains(new ItemStack(cloakingItem))) {
            entity.getNavigation().stop();
            brain.eraseMemory(ModMemoryTypes.SPOTTED_PLAYER.get());
            return;
        }

        // Small fear increase from spotting (throttled)
        if (gameTime >= nextSpotFearTick) {
            SteveLogic.addFear(brain, fearOnSpot);
            nextSpotFearTick = gameTime + fearSpotIntervalTicks;
        }

        double fear = brain.getMemory(ModMemoryTypes.FEAR_LEVEL.get()).orElse(0.0);
        fear = SteveLogic.clampEmotion(fear);

        double fear01 = fear / 2.0;
        double fleeRadius = baseFleeRadius + (fear01 * fearRadiusScale);
        double fleeRadiusSqr = fleeRadius * fleeRadius;

        // Steve will only flee when the player is within radius
        if (entity.distanceToSqr(player) > fleeRadiusSqr) {
            entity.getNavigation().stop();
            return;
        }

        // Steve will "hesitate" before fleeing
        if (brain.hasMemoryValue(ModMemoryTypes.HESITATION_COOLDOWN.get())) {
            entity.getNavigation().stop();
            return;
        }

        if (gameTime < nextRepathTick)
            return;

        nextRepathTick = gameTime + 30; // Once every 1.5 seconds

        Vec3 awayPos = DefaultRandomPos.getPosAway(
                entity,
                fleeHorizontal,
                fleeVertical,
                player.position()
        );

        if (SteveLogic.isTerrified(brain)) {
            speed = baseSpeed + 1.0;
        }

        if (awayPos != null) {
            entity.getNavigation().moveTo(awayPos.x, awayPos.y, awayPos.z, speed);
        }
    }

    // If the player has the cloaking item, steve does NOT flee




    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        entity.getNavigation().stop();
    }
}
