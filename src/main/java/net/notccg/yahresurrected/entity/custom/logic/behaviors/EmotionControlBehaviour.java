package net.notccg.yahresurrected.entity.custom.logic.behaviors;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class EmotionControlBehaviour<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private final long emotionRundownTicks;
    private final float fearDecreaseMultiplier;
    private final float curiosityDecreaseMultiplier;
    private final float paranoiaDecreaseMultiplier;

    private long nextRundownInterval = 0;

    public EmotionControlBehaviour(long emotionRundownTicks, float fearDecreaseMultiplier, float curiosityDecreaseMultiplier, float paranoiaDecreaseMultiplier) {
        this.emotionRundownTicks = emotionRundownTicks;
        this.fearDecreaseMultiplier = fearDecreaseMultiplier;
        this.curiosityDecreaseMultiplier = curiosityDecreaseMultiplier;
        this.paranoiaDecreaseMultiplier = paranoiaDecreaseMultiplier;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return ObjectArrayList.of(
                Pair.of(ModMemoryTypes.FEAR_LEVEL.get(), MemoryStatus.VALUE_PRESENT),
                Pair.of(ModMemoryTypes.CURIOSITY_LEVEL.get(), MemoryStatus.VALUE_PRESENT),
                Pair.of(ModMemoryTypes.PARANOIA_LEVEL.get(), MemoryStatus.VALUE_PRESENT),
                Pair.of(ModMemoryTypes.EMOTION_UPDATED.get(), MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        long now = level.getGameTime();
        if (now < nextRundownInterval) return;
        nextRundownInterval = now + emotionRundownTicks;

        var brain = entity.getBrain();
        double fearLevel = SteveLogic.getFear(brain);
        double curiosityLevel = SteveLogic.getCuriosity(brain);
        double paranoiaLevel = SteveLogic.getParanoia(brain);

        if (fearLevel > 0.0) {
            double newFearLevel = fearLevel * fearDecreaseMultiplier;
            if (newFearLevel < 0.05) {
                newFearLevel = 0.0;
            }
            brain.setMemory(ModMemoryTypes.FEAR_LEVEL.get(), newFearLevel);
        }

        if (curiosityLevel > 0.0) {
            double newCuriosityLevel = curiosityLevel * curiosityDecreaseMultiplier;
            if (newCuriosityLevel < 0.5) {
                newCuriosityLevel = 0.0;
            };
            brain.setMemory(ModMemoryTypes.CURIOSITY_LEVEL.get(), newCuriosityLevel);
        }

        if (paranoiaLevel > 0.0) {
            double newParanoiaLevel = paranoiaLevel * paranoiaDecreaseMultiplier;
            if (newParanoiaLevel < 0.05) {
                newParanoiaLevel = 0.0;
            };
            brain.setMemory(ModMemoryTypes.PARANOIA_LEVEL.get(), newParanoiaLevel);
        }
    }
}
