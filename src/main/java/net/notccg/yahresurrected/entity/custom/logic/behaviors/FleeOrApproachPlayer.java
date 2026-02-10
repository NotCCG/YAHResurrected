package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
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

import java.util.List;
import java.util.UUID;

public class FleeOrApproachPlayer<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private final float baseSpeed;
    private final int baseFleeDist;
    private final int maxFleeDist;
    private final int baseApproachDist;
    private final int maxApproachDist;
    private final long decisionCooldown;

    private long nextDecisionTick = 0;

    public FleeOrApproachPlayer(float baseSpeed,
                                int basefleeDist,
                                int maxFleeDist,
                                int baseApproachDist,
                                int maxApproachDist,
                                long decisionCooldown) {
        this.baseSpeed = baseSpeed;
        this.baseFleeDist = basefleeDist;
        this.maxFleeDist = maxFleeDist;
        this.baseApproachDist = baseApproachDist;
        this.maxApproachDist = maxApproachDist;
        this.decisionCooldown = decisionCooldown;

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
        this.nextDecisionTick = gameTime;
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        Player player = entity.getBrain().getMemory(ModMemoryTypes.SPOTTED_PLAYER.get()).orElse(null);
        if (player == null) {
            UUID uuid = entity.getBrain().getMemory(ModMemoryTypes.LAST_PLAYER_SEEN.get()).orElse(null);
            if (uuid != null) {
                player = level.getPlayerByUUID(uuid);
            }
        }
        if (player == null) return;

        var brain = entity.getBrain();

        Long lookBackUntil = brain.getMemory(ModMemoryTypes.LOOK_BACK_UNTIL.get()).orElse(null);
        if (lookBackUntil != null) {
            if (gameTime <= lookBackUntil) {
                brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(player.getEyePosition()));
                return;
            }
            brain.eraseMemory(ModMemoryTypes.LOOK_BACK_UNTIL.get());
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
                System.out.println("[YAH:R STEVE-DEBUG] Steve is FLEEING");
                fleeOrApproach = FleeOrApproach.FLEE;
            } else if ((playerDistSqr >= maxApproachDistSqr) && (playerDistSqr > closeEnough) && approach) {
                System.out.println("[YAH:R STEVE-DEBUG] Steve is APPROACHING");
                fleeOrApproach = FleeOrApproach.APPROACH;
            } else {
                System.out.println("[YAH:R STEVE-DEBUG] Steve is FREEZING");
                fleeOrApproach = FleeOrApproach.FREEZE;
            }

            long cooldown = decisionCooldown;
            if (fleeOrApproach == FleeOrApproach.APPROACH || fleeOrApproach == FleeOrApproach.FLEE) {
                cooldown = decisionCooldown * 2;
            }

            brain.setMemory(ModMemoryTypes.FLEE_OR_APPROACH.get(), fleeOrApproach);
            nextDecisionTick = gameTime + cooldown;
        }

        boolean hasBeenHurtByPlayer = brain.hasMemoryValue(ModMemoryTypes.PLAYER_HURT.get());
        double hurtMultiplier = hasBeenHurtByPlayer ? 1.5 : 1.0;



        double fleeToDist =
                (4 + 32 * fearParanoiaMean) * hurtMultiplier;

        FleeOrApproach fleeOrApproach = brain.getMemory(ModMemoryTypes.FLEE_OR_APPROACH.get()).orElse(FleeOrApproach.FREEZE);
        if (fleeOrApproach == FleeOrApproach.FREEZE) {
            Vec3 playerEyePos = player.getEyePosition();
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
            brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(playerEyePos));
        }
        if (fleeOrApproach == FleeOrApproach.APPROACH) {
            if (sneakApproach) {
                entity.setPose(Pose.CROUCHING);
            } else {
                entity.setPose(Pose.STANDING);
            }

            brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(player, baseSpeed, 1));

            Vec3 playerEyePos = player.getEyePosition();
            brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(playerEyePos));
        }
        if (fleeOrApproach == FleeOrApproach.FLEE) {
            Vec3 lastFleePos = brain.getMemory(ModMemoryTypes.LAST_FLEE_POS.get()).orElse(null);
            if (lastFleePos != null) {
                double arriveDist = 1.5;
                double arriveDistSqr = arriveDist * arriveDist;
                if (entity.position().distanceToSqr(lastFleePos) <= arriveDistSqr) {
                    brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                    brain.setMemory(ModMemoryTypes.LOOK_BACK_UNTIL.get(), 40L);

                    brain.eraseMemory(ModMemoryTypes.FLEE_OR_APPROACH.get());
                }
            }

            Vec3 away = entity.position().subtract(player.position()).normalize();
            Vec3 fleePos = entity.position().add(away.scale(fleeToDist));
            BlockPos lookPos = BlockPos.containing(fleePos).above();

            brain.setMemory(ModMemoryTypes.LAST_FLEE_POS.get(), fleePos);
            brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(fleePos, baseSpeed, 1));
            brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(lookPos));
        }
    }

    @Override
    protected void stop(E entity) {
        entity.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }
}
