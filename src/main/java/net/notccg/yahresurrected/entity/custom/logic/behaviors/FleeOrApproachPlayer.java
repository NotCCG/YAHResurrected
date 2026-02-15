package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.FleeOrApproach;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.slf4j.Logger;

import java.util.List;
import java.util.UUID;

public class FleeOrApproachPlayer<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final float baseSpeed;
    private final int baseFleeDist;
    private final int maxFleeDist;
    private final int baseApproachDist;
    private final int maxApproachDist;
    private final long decisionCoolDown;

    private long nextDecisionTick = 0;

    public FleeOrApproachPlayer(float baseSpeed,
                                int baseFleeDist,
                                int maxFleeDist,
                                int baseApproachDist,
                                int maxApproachDist,
                                long decisionCoolDown) {
        this.baseSpeed = baseSpeed;
        this.baseFleeDist = baseFleeDist;
        this.maxFleeDist = maxFleeDist;
        this.baseApproachDist = baseApproachDist;
        this.maxApproachDist = maxApproachDist;
        this.decisionCoolDown = decisionCoolDown;

    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
                Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_PRESENT),
                Pair.of(ModMemoryTypes.FEAR_LEVEL.get(), MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.CURIOSITY_LEVEL.get(), MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.FLEE_OR_APPROACH.get(), MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.LAST_FLEE_POS.get(), MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.LOOK_BACK_UNTIL.get(), MemoryStatus.REGISTERED)
        );
    }


    @Override
    protected boolean shouldKeepRunning(E entity) {
        return entity.getBrain().hasMemoryValue(ModMemoryTypes.SPOTTED_PLAYER.get()) ||
                entity.getBrain().hasMemoryValue(ModMemoryTypes.LOOK_BACK_UNTIL.get()) ||
                entity.getBrain().hasMemoryValue(ModMemoryTypes.LAST_PLAYER_SEEN.get());
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] start", this.getClass().getSimpleName(), entity.getUUID());
        this.nextDecisionTick = gameTime;
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        Player player = entity.getBrain().getMemory(ModMemoryTypes.SPOTTED_PLAYER.get()).orElse(null);
        if (player == null) {
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] memory SPOTTED_PLAYER returns null, checking memory LAST_PLAYER_SEEN",
                    this.getClass().getSimpleName(), entity.getUUID());

            UUID uuid = entity.getBrain().getMemory(ModMemoryTypes.LAST_PLAYER_SEEN.get()).orElse(null);
            if (uuid != null) {
                player = level.getPlayerByUUID(uuid);
            }
        }
        if (player == null) {
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] variable \"player\" is null, return",
                    this.getClass().getSimpleName(), entity.getUUID());

            return;
        }

        var brain = entity.getBrain();

        Long lookBackUntil = brain.getMemory(ModMemoryTypes.LOOK_BACK_UNTIL.get()).orElse(null);
        if (lookBackUntil != null) {
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] memory LOOK_BACK_UNTIL is present and has a value, performing additional tasks", this.getClass().getSimpleName(), entity.getUUID());
            if (gameTime <= lookBackUntil) {
                brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] erased WALK_TARGET", this.getClass().getSimpleName(), entity.getUUID());
                brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(player.getEyePosition()));
                LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set LOOK_TARGET -> {}", this.getClass().getSimpleName(), entity.getUUID(), player.getEyePosition());
                return;
            }
            brain.eraseMemory(ModMemoryTypes.LOOK_BACK_UNTIL.get());
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] erased LOOK_BACK_UNTIL", this.getClass().getSimpleName(), entity.getUUID());
        }

        double fear01 = SteveLogic.getFear(brain, gameTime) / 2.0;
        double curiosity01 = SteveLogic.getCuriosity(brain, gameTime) / 2.0;
        double paranoia01 = SteveLogic.getParanoia(brain, gameTime) / 2.0;

        double fear = Mth.clamp(fear01, 0.0, 1.0);
        double curiosity = Mth.clamp(curiosity01, 0.0, 1.0);
        double paranoia = Mth.clamp(paranoia01, 0.0, 1.0);

        double fearParanoiaMean = (fear + paranoia) * 0.5;
        double approachDrive = Mth.clamp((curiosity - paranoia), 0.0, 1.0);
        double maxApproachChance = 0.75;
        double minApproachChance = 0.05;
        double approachChance = minApproachChance + (maxApproachChance - minApproachChance) * approachDrive;
        boolean sneakApproach = SteveLogic.getCuriosity(brain, gameTime) >= 1.4;

        int dynamicFleeDist = baseFleeDist + (int) Math.round(maxFleeDist * fearParanoiaMean);
        int dynamicApproachDist = baseApproachDist + (int) Math.round(maxApproachDist * approachDrive);

        if (gameTime > nextDecisionTick || !brain.hasMemoryValue(ModMemoryTypes.FLEE_OR_APPROACH.get())) {
            double playerDistSqr = entity.distanceToSqr(player);
            int fleeDistSqr = dynamicFleeDist * dynamicFleeDist;
            int maxApproachDistSqr = dynamicApproachDist * dynamicApproachDist;
            int closeEnough = 3;

            FleeOrApproach fleeOrApproach;

            boolean approach = entity.getRandom().nextDouble() < approachChance;

            if (playerDistSqr <= fleeDistSqr) {
                fleeOrApproach = FleeOrApproach.FLEE;
                LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set variable \"fleeOrApproach\" to {} as conditions were met", this.getClass().getSimpleName(), entity.getUUID(), fleeOrApproach);
            } else if ((playerDistSqr >= maxApproachDistSqr) && (playerDistSqr > closeEnough) && approach) {
                fleeOrApproach = FleeOrApproach.APPROACH;
                LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set variable \"fleeOrApproach\" to {} as conditions were met", this.getClass().getSimpleName(), entity.getUUID(), fleeOrApproach);
            } else {
                fleeOrApproach = FleeOrApproach.FREEZE;
                LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set variable \"fleeOrApproach\" to {} as conditions were met", this.getClass().getSimpleName(), entity.getUUID(), fleeOrApproach);
            }

            long cooldown = decisionCoolDown;
            if (fleeOrApproach == FleeOrApproach.APPROACH || fleeOrApproach == FleeOrApproach.FLEE) {
                cooldown = decisionCoolDown * 2;
                LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] variable \"fleeOrApproach\" is {}, \"decisionCoolDown\" is now doubled", this.getClass().getSimpleName(), entity.getUUID(), fleeOrApproach);
            }

            brain.setMemory(ModMemoryTypes.FLEE_OR_APPROACH.get(), fleeOrApproach);
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set FLEE_OR_APPROACH -> {}", this.getClass().getSimpleName(), entity.getUUID(), fleeOrApproach);
            nextDecisionTick = gameTime + cooldown;
        }

        boolean hasBeenHurtByPlayer = brain.hasMemoryValue(ModMemoryTypes.PLAYER_HURT.get());
        double hurtMultiplier = hasBeenHurtByPlayer ? 1.5 : 1.0;



        double fleeToDist =
                (4 + 32 * fearParanoiaMean) * hurtMultiplier;

        FleeOrApproach fleeOrApproach = brain.getMemory(ModMemoryTypes.FLEE_OR_APPROACH.get()).orElse(FleeOrApproach.FREEZE);
        LOGGER.debug("[YAH:R] [TICK:{}][BEHAVIOR:{}][{}] memory FLEE_OR_APPROACH has value -> {}",
                gameTime, this.getClass().getSimpleName(), entity.getUUID(), fleeOrApproach);
        if (fleeOrApproach == FleeOrApproach.FREEZE) {
            entity.setShiftKeyDown(false);
            Vec3 playerEyePos = player.getEyePosition();
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] erased WALK_TARGET", this.getClass().getSimpleName(), entity.getUUID());
            brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(playerEyePos));
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set LOOK_TARGET -> {}", this.getClass().getSimpleName(), entity.getUUID(), playerEyePos);
        }
        if (fleeOrApproach == FleeOrApproach.APPROACH) {
            entity.setShiftKeyDown(sneakApproach);

            float speed = sneakApproach ? (baseSpeed * 0.3f) : baseSpeed;
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] boolean \"sneakOrApproach\" has value of {}, baseSpeed -> {}",
                    this.getClass().getSimpleName(), entity.getUUID(), sneakApproach, speed);
            brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(player, speed, 1));
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set WALK_TARGET -> {}", this.getClass().getSimpleName(), entity.getUUID(), player);

            Vec3 playerEyePos = player.getEyePosition();
            brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(playerEyePos));
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set LOOK_TARGET -> {}", this.getClass().getSimpleName(), entity.getUUID(), playerEyePos);
        }
        if (fleeOrApproach == FleeOrApproach.FLEE) {
            entity.setShiftKeyDown(false);
            Vec3 lastFleePos = brain.getMemory(ModMemoryTypes.LAST_FLEE_POS.get()).orElse(null);
            if (lastFleePos != null) {
                LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] variable \"lastFleePos\" is not null, checking extra conditions", this.getClass().getSimpleName(), entity.getUUID());
                double arriveDist = 1.5;
                double arriveDistSqr = arriveDist * arriveDist;
                if (entity.position().distanceToSqr(lastFleePos) <= arriveDistSqr) {
                    LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] extra conditions met, performing additional tasks", this.getClass().getSimpleName(), entity.getUUID());
                    brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                    LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] erased WALK_TARGET", this.getClass().getSimpleName(), entity.getUUID());
                    brain.setMemory(ModMemoryTypes.LOOK_BACK_UNTIL.get(), 40L);
                    LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set LOOK_BACK_UNTIL -> 40L", this.getClass().getSimpleName(), entity.getUUID());

                    brain.eraseMemory(ModMemoryTypes.FLEE_OR_APPROACH.get());
                    LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] erased FLEE_OR_APPROACH", this.getClass().getSimpleName(), entity.getUUID());
                }
                LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] extra conditions NOT met", this.getClass().getSimpleName(), entity.getUUID());
            }

            Vec3 away = entity.position().subtract(player.position()).normalize();
            Vec3 fleePos = entity.position().add(away.scale(fleeToDist));
            BlockPos lookPos = BlockPos.containing(fleePos).above();

            brain.setMemory(ModMemoryTypes.LAST_FLEE_POS.get(), fleePos);
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set LAST_FLEE_POS -> {}",
                    this.getClass().getSimpleName(), entity.getUUID(), fleePos);

            brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(fleePos, baseSpeed, 1));
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set WALK_TARGET -> {}", this.getClass().getSimpleName(), entity.getUUID(), fleePos)
            ;
            brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(lookPos));
            LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] set LOOK_TARGET -> {}", this.getClass().getSimpleName(), entity.getUUID(), lookPos);
        }
    }

    @Override
    protected void stop(E entity) {
        LOGGER.debug("[YAH:R] [BEHAVIOR:{}][{}] stopped",
                this.getClass().getSimpleName(), entity.getUUID());

        entity.setShiftKeyDown(false);
        entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }
}