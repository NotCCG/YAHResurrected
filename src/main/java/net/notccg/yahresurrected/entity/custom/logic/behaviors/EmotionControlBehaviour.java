package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.slf4j.Logger;

import java.util.Comparator;
import java.util.List;

public class EmotionControlBehaviour<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES =
            ObjectArrayList.of(
                    Pair.of(ModMemoryTypes.CURIOSITY_LEVEL.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.CURIOSITY_ANCHOR.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.CURIOSITY_CHANGE.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.FEAR_LEVEL.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.FEAR_ANCHOR.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.FEAR_CHANGE.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.PARANOIA_LEVEL.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.PARANOIA_ANCHOR.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.PARANOIA_CHANGE.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.HEARD_SOUND_TYPE.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.LAST_HEARD_TIME.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.PLAYER_IS_SPOTTED.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.LAST_SPOTTED_PLAYER_TIME.get(), MemoryStatus.REGISTERED),
                    Pair.of(ModMemoryTypes.PICKED_UP_LIKED_ITEM.get(), MemoryStatus.REGISTERED)
            );

    private final long updateInterval;

    private final double baseFearIncreaseRadius;
    private final double baseCuriosityIncrease;

    private long nextUpdateTick = 0;
    private long previousSpottedPlayerTime = 0;
    private long previousHeardSoundTime = 0;

    private double baseParanoiaScale = 0.1;

    public EmotionControlBehaviour(long updateInterval,
                            double baseFearIncreaseRadius,
                            double baseCuriosityIncrease) {
        this.updateInterval = updateInterval;
        this.baseFearIncreaseRadius = baseFearIncreaseRadius;
        this.baseCuriosityIncrease = baseCuriosityIncrease;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
        return true;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        this.nextUpdateTick = gameTime;

        var brain = entity.getBrain();
        this.previousSpottedPlayerTime = brain.getMemory(ModMemoryTypes.LAST_SPOTTED_PLAYER_TIME.get()).orElse(0L);
        this.previousHeardSoundTime = brain.getMemory(ModMemoryTypes.LAST_HEARD_TIME.get()).orElse(0L);
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        if (gameTime < nextUpdateTick) return;
        nextUpdateTick = gameTime + updateInterval;

        var brain = entity.getBrain();
        double paranoia = SteveLogic.getParanoia(brain, gameTime);
        double paranoiaSqr = paranoia*paranoia;
        boolean hasBeenHurtByPlayer = brain.hasMemoryValue(ModMemoryTypes.PLAYER_HURT.get());

        if (brain.hasMemoryValue(ModMemoryTypes.PLAYER_IS_SPOTTED.get())) {
            long spottedPlayerTime = brain.getMemory(ModMemoryTypes.LAST_SPOTTED_PLAYER_TIME.get()).orElse(0L);
            long spottedPlayerTimeDelta = spottedPlayerTime - previousSpottedPlayerTime;

            if (spottedPlayerTimeDelta > 0) {
                previousSpottedPlayerTime = spottedPlayerTime;
                Player player = brain.getMemory(ModMemoryTypes.SPOTTED_PLAYER.get()).orElse(null);
                if (player != null) {
                    double paranoiaScale = paranoia + baseParanoiaScale;
                    double fearIncreaseRadius = baseFearIncreaseRadius * paranoiaScale;
                    double fearIncreaseRadiusSqr = fearIncreaseRadius * fearIncreaseRadius;

                    AABB fearIncreaseRange = entity.getBoundingBox().inflate(fearIncreaseRadius);

                    Player overThreshold = level.getEntitiesOfClass(Player.class, fearIncreaseRange, p ->
                                    p.isAlive() &&
                                            !p.isSpectator() &&
                                            !p.isCreative() &&
                                            entity.distanceToSqr(p) <= fearIncreaseRadiusSqr
                            ).stream()
                            .min(Comparator.comparingDouble(entity::distanceToSqr))
                            .orElse(null);
                    if (overThreshold != null) {
                        if (SteveLogic.isTerrified(brain, gameTime) || hasBeenHurtByPlayer) {
                            SteveLogic.addFear(brain, gameTime, paranoiaSqr);
                            System.out.println("[YAH:R STEVE-DEBUG] Steve saw the player and is scared");
                        } else {
                            SteveLogic.addCuriosity(brain, gameTime, baseCuriosityIncrease);
                            System.out.println("[YAH:R STEVE-DEBUG] Steve saw the player and is curious");
                        }
                    }
                }
            }
            nextUpdateTick = gameTime + updateInterval;
        }

        if (brain.hasMemoryValue(ModMemoryTypes.PICKED_UP_LIKED_ITEM.get())) {
            SteveLogic.reduceFear(brain, gameTime, 0.25);
        }
    }
}
