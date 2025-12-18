package net.notccg.yahresurrected.entity.custom.logic.SteveAI;

import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.item.ItemStack;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModTags;
import java.util.Random;

public class SteveLogic {

    private static final Random RANDOM = new Random();

    String[] STEVE_NAMES = {
            "Player",
            "Steve",
            "DanTDM",
            "NotCCG"
    };

    public String getSteveName() {
        int index = RANDOM.nextInt(STEVE_NAMES.length);
        return STEVE_NAMES[index];
    }


    public static boolean isLovedItem(ItemStack pItemsTack) {
        return pItemsTack.is(ModTags.Items.STEVE_LOVED);
    }

    public static double clampEmotion(double v) {
        return Math.max(0.0, Math.min(2.0, v));
    }

    public static double getFear(Brain<?> brain) {
        return brain.getMemory(ModMemoryTypes.FEAR_LEVEL.get()).orElse(0.0);
    }

    public static void addFear(Brain<?> brain, double amount) {
        double next = clampEmotion(getFear(brain) + amount);
        brain.setMemory(ModMemoryTypes.FEAR_LEVEL.get(), next);
    }

    public static void reduceFear(Brain<?> brain, double amount) {
        double next = clampEmotion(getFear(brain) - amount);
        brain.setMemory(ModMemoryTypes.FEAR_LEVEL.get(), next);
    }

    public static double getCuriosity(Brain<?> brain) {
        return brain.getMemory(ModMemoryTypes.CURIOSITY_LEVEL.get()).orElse(0.0);
    }

    public static void addCuriosity(Brain<?> brain, double amount) {
        double next = clampEmotion(getCuriosity(brain) + amount);
        brain.setMemory(ModMemoryTypes.CURIOSITY_LEVEL.get(), next);
    }

    public static void reduceCuriosity(Brain<?> brain, double amount) {
        double next = clampEmotion(getCuriosity(brain) - amount);
        brain.setMemory(ModMemoryTypes.CURIOSITY_LEVEL.get(), next);
    }

public static class FearState {
    private double fearLevel;

    public double getFearLevel() {
        return fearLevel;
    }

    public double increaseFear(double amount) {
        fearLevel = clamp(fearLevel + amount);
        return fearLevel;
    }

    public double decreaseFear(double amount) {
        fearLevel = clamp(fearLevel - amount);
        return fearLevel;
    }

    private double clamp(double v) {
        return Math.max(0.0, Math.min(2.0, v));
    }

    public boolean isCalm() {
        return fearLevel == 0.0;
    }

    public boolean isSpooked() {
        return fearLevel > 0.0 && fearLevel < 1.0;
    }

    public boolean isUneasy() {
        return fearLevel >= 1.0 && fearLevel < 1.5;
    }

    public boolean isScared() {
        return fearLevel >= 1.5 && fearLevel < 2.0;
    }

    public boolean isTerrified() {
        return fearLevel >= 2.0;
    }
}

    public static class CuriosityLevel {
        private double curiosityLevel;

        public double getCuriosityLevel() {
            return curiosityLevel;
        }

        public double increaseCuriosity(double amount) {
            curiosityLevel = clamp(curiosityLevel + amount);
            return curiosityLevel;
        }

        public double decreaseCuriosity(double amount) {
            curiosityLevel = clamp(curiosityLevel - amount);
            return curiosityLevel;
        }

        private double clamp(double v) {
            return Math.max(0.0, Math.min(2.0, v));
        }

        public boolean isIntrigued() {
            return curiosityLevel > 0.0 && curiosityLevel < 1.0;
        }

        public boolean isCurious() {
            return curiosityLevel >= 1.0 && curiosityLevel < 2.0;
        }

        public boolean isVeryCurious() {
            return curiosityLevel >= 2.0;
        }
    }
}
