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
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class FleeOrApproachPlayer<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private final Item cloakingItem;
    private final double speed;
    private final int fleeHorizontal;
    private final int fleeVertical;

    private final double fleeRadius;
    private final double fleeRadiusSqr;

    private long nextRepathTick = 0;

    public FleeOrApproachPlayer(Item cloakingItem, double speed, int fleeHorizontal, int fleeVertical, double fleeRadius) {
        this.cloakingItem = cloakingItem;
        this.speed = speed;
        this.fleeHorizontal = fleeHorizontal;
        this.fleeVertical = fleeVertical;

        this.fleeRadius = fleeRadius;
        this.fleeRadiusSqr = fleeRadius * fleeRadius;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_PRESENT)
        );
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        this.nextRepathTick = 0;
    }

    // If the player has the cloaking item, steve does NOT flee
    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        Brain<?> brain = entity.getBrain();
        Player player = brain.getMemory(ModMemoryTypes.SPOTTED_PLAYER.get()).orElse(null);
        if (player == null)
            return;

        if (player.getInventory().contains(new ItemStack(cloakingItem))) {
            entity.getNavigation().stop();
            brain.eraseMemory(ModMemoryTypes.SPOTTED_PLAYER.get());
            return;
        }

        if (entity.distanceToSqr(player) > fleeRadiusSqr) {
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

        if (awayPos != null) {
            entity.getNavigation().moveTo(awayPos.x, awayPos.y, awayPos.z, speed);
        }
    }
}
