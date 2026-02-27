package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.slf4j.Logger;

import java.util.List;

public class ReactToSoundBehaviour<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final boolean isEnabled;

    private static final long MAX_SOUND_AGE = 180;
    private static final double CLOSE_ENOUGH = 2;
    private static final int FLEE_HORIZONTAL = 16;

    private static final float INVESTIGATE_SPEED = 1.0f;
    private static final float CAUTIOUS_SPEED = 0.75f;
    private static final float FLEE_SPEED = 1.25f;

    private enum InvestigateMode {FLEE, CAUTIOUSLY_INVESTIGATE, INVESTIGATE}

    private InvestigateMode investigateMode = InvestigateMode.INVESTIGATE;
    private BlockPos soundPos = null;

    public ReactToSoundBehaviour(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    private static boolean shouldFlee(Brain<?> brain, long gameTime) {
        return SteveLogic.isTerrified(brain, gameTime) ||
                SteveLogic.isParanoid(brain, gameTime) ||
                SteveLogic.isVeryParanoid(brain, gameTime) ||
                brain.hasMemoryValue(ModMemoryTypes.PLAYER_HURT.get());
    };

    private static boolean shouldCautiouslyInvestigate(Brain<?> brain, long gameTime) {
        return SteveLogic.isCautious(brain, gameTime);
    };

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(ModMemoryTypes.SPOTTED_PLAYER.get(), MemoryStatus.VALUE_ABSENT),
                Pair.of(ModMemoryTypes.INVESTIGATE_TARGET.get(), MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.HEARD_SOUND_POS.get(), MemoryStatus.VALUE_PRESENT),
                Pair.of(ModMemoryTypes.LAST_HEARD_TIME.get(), MemoryStatus.VALUE_PRESENT),
                Pair.of(ModMemoryTypes.CURIOSITY_LEVEL.get(), MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.FEAR_LEVEL.get(), MemoryStatus.REGISTERED),
                Pair.of(ModMemoryTypes.PARANOIA_LEVEL.get(), MemoryStatus.REGISTERED),
                Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
                Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED)
        );
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return isEnabled;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        super.start(level, entity, gameTime);
    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        Brain<?> brain = entity.getBrain();
        entity.setPose(Pose.STANDING);

        brain.eraseMemory(ModMemoryTypes.HEARD_SOUND_POS.get());
        brain.eraseMemory(ModMemoryTypes.INVESTIGATE_TARGET.get());
        brain.eraseMemory(ModMemoryTypes.LAST_HEARD_TIME.get());
    }
}
