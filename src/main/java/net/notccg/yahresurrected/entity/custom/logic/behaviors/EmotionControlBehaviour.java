package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.HeardSoundType;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.Comparator;
import java.util.List;

public class EmotionControlBehaviour<E extends PathfinderMob> extends ExtendedBehaviour<E> {
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
                    Pair.of(ModMemoryTypes.LAST_SPOTTED_PLAYER_TIME.get(), MemoryStatus.REGISTERED)
            );

    private final long updateInterval;

    private final double baseFearIncreaseRadius;
    private final double baseCuriosityIncrease;

    private long nextUpdateTick = 0;
    private long previousSpottedPlayerTime = 0;
    private long previousHeardSoundTime = 0;

    private double baseParanoiaScale = 1;

    EmotionControlBehaviour(long updateInterval,
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
                            SteveLogic.addFear(brain, gameTime,paranoiaSqr);
                        } else {
                        SteveLogic.addCuriosity(brain, gameTime, baseCuriosityIncrease);
                        }
                    }
                }
            }
            nextUpdateTick = gameTime + updateInterval;
        }

        HeardSoundType heardSoundType = brain.getMemory(ModMemoryTypes.HEARD_SOUND_TYPE.get()).orElse(null);
        if (heardSoundType != null) {
            long heardSoundTime = brain.getMemory(ModMemoryTypes.LAST_HEARD_TIME.get()).orElse(0L);
            long heardSoundTimeDelta = heardSoundTime - previousHeardSoundTime;
            if (heardSoundTimeDelta > 0) {
                previousHeardSoundTime = heardSoundTime;

                if (hasBeenHurtByPlayer || SteveLogic.isParanoid(brain, gameTime) || SteveLogic.isScared(brain, gameTime)) {
                    SteveLogic.addFear(brain, gameTime, paranoiaSqr);
                } else {
                    SteveLogic.addCuriosity(brain, gameTime, baseCuriosityIncrease);
                }
            }
            nextUpdateTick = gameTime + updateInterval;
        }
    }
}
