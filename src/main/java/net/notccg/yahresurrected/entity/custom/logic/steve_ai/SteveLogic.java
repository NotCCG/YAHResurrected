package net.notccg.yahresurrected.entity.custom.logic.steve_ai;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModTags;

import java.util.List;
import java.util.Random;

public class SteveLogic {
    private static final Random RANDOM = new Random();

    private static final long fearHLTicks = 80L;
    private static final long curiosityHLTicks = 500L;
    private static final long paranoiaHLTicks = 1850L;

    private static final double fearBaseline = 0;
    private static final double curiosityBaseline = 0;
    private static final double paranoiaBaseline = 0;

    // Emotional Logic
    public static double emotionalDecay(double valueAtLastChange, double baseline, long ticksSinceChange, double halfLifeTicks) {
        if (halfLifeTicks <= 0.0) return baseline;
        if (ticksSinceChange <= 0) return valueAtLastChange;

        double k = Math.log(2.0) / halfLifeTicks;
        double factor = Math.exp(-k * (double)ticksSinceChange);

        return baseline + (valueAtLastChange - baseline) * factor;
    }

    public static double clampEmotion(double v) {
        return Math.max(0.0, Math.min(2.0, v));
    }

    private static double getDoubleMemory(Brain<?> pBrain, MemoryModuleType<Double> pMemoryModuleType) {
        return pBrain.getMemory(pMemoryModuleType).orElse(0.0);
    }

    private static long getLongMemory(Brain<?> pBrain, MemoryModuleType<Long> pMemoryModuleType) {
        return pBrain.getMemory(pMemoryModuleType).orElse(0L);
    }

    // Fear Logic
    public static double getFear(Brain<?> brain, long gameTime) {
        double anchor = getDoubleMemory(brain, ModMemoryTypes.FEAR_ANCHOR.get());
        long changedAt = getLongMemory(brain, ModMemoryTypes.FEAR_CHANGE.get());

        return emotionalDecay(anchor, fearBaseline, gameTime - changedAt, fearHLTicks);
    }

    public static boolean isUneasy(Brain<?> brain, long gameTime) {
        return getFear(brain, gameTime) >= 1.0 && getFear(brain, gameTime) < 1.5;
    }
    public static boolean isScared(Brain<?> brain, long gameTime) {
        return getFear(brain, gameTime) >= 1.5 && getFear(brain, gameTime) < 2.0;
    }
    public static boolean isTerrified(Brain<?> brain, long gameTime) {
        return getFear(brain, gameTime) >= 2.0;
    }

    public static void addFear(Brain<?> brain, long gameTime, double amount) {
        double current = getFear(brain, gameTime);
        double next = clampEmotion(current + amount);

        brain.setMemory(ModMemoryTypes.FEAR_ANCHOR.get(), next);
        brain.setMemory(ModMemoryTypes.FEAR_CHANGE.get(), gameTime);

        System.out.println("adding fear " + amount);
        brain.setMemory(ModMemoryTypes.FEAR_LEVEL.get(), next);
    }

    public static void reduceFear(Brain<?> brain, long gameTime, double amount) {
        addFear(brain, gameTime, -amount);
    }

    // Curiosity Logic
    public static double getCuriosity(Brain<?> brain, long gameTime) {
        double anchor = getDoubleMemory(brain, ModMemoryTypes.CURIOSITY_ANCHOR.get());
        long changedAt = getLongMemory(brain, ModMemoryTypes.CURIOSITY_CHANGE.get());

        return emotionalDecay(anchor, curiosityBaseline, gameTime - changedAt, curiosityHLTicks);
    }

    public static void addCuriosity(Brain<?> brain, long gameTime, double amount) {
        double current = getCuriosity(brain, gameTime);
        double next = clampEmotion(current + amount);

        brain.setMemory(ModMemoryTypes.CURIOSITY_ANCHOR.get(), next);
        brain.setMemory(ModMemoryTypes.CURIOSITY_CHANGE.get(), gameTime);

        System.out.println("adding curiosity " + amount);
        brain.setMemory(ModMemoryTypes.CURIOSITY_LEVEL.get(), next);
    }

    public static void reduceCuriosity(Brain<?> brain, long gameTime, double amount) {
        addCuriosity(brain, gameTime, -amount);
    }

    // Paranoia Logic
    public static double getParanoia(Brain<?> brain, long gameTime) {
        double anchor = getDoubleMemory(brain, ModMemoryTypes.PARANOIA_ANCHOR.get());
        long changedAt= getLongMemory(brain, ModMemoryTypes.PARANOIA_CHANGE.get());

        return emotionalDecay(anchor, paranoiaBaseline, gameTime - changedAt, paranoiaHLTicks);
    }

    public static void addParanoia(Brain<?> brain, long gameTime, double amount) {
        double current = getParanoia(brain, gameTime);
        double next = clampEmotion(current + amount);

        brain.setMemory(ModMemoryTypes.PARANOIA_ANCHOR.get(), next);
        brain.setMemory(ModMemoryTypes.PARANOIA_CHANGE.get(), gameTime);

        System.out.println("adding paranoia " + amount);
        brain.setMemory(ModMemoryTypes.PARANOIA_LEVEL.get(), next);
    }

    public static void decreaseParanoia(Brain<?> brain, long gameTime, double amount) {
        addParanoia(brain, gameTime, -amount);
    }


    public static boolean isParanoid(Brain<?> brain, long gameTime) {
        return getParanoia(brain, gameTime) >= 0.5 && getParanoia(brain, gameTime) < 0.75;
    }

    public static boolean isVeryParanoid(Brain<?> brain, long gameTime) {
        return getParanoia(brain, gameTime) >= 0.75;
    }

    // Mixed Emotion Logic

    public static boolean isCautious(Brain<?> brain, long gameTime) {
        return  getFear(brain, gameTime) > 0.0 && getCuriosity(brain, gameTime) > 0.0;
    }


    // Interests Logic
    public static List<Block> INTERESTED_BLOCKS = ObjectArrayList.of(
            Blocks.ACACIA_SIGN,
            Blocks.ACACIA_WALL_SIGN,
            Blocks.BAMBOO_SIGN,
            Blocks.BAMBOO_WALL_SIGN,
            Blocks.BEDROCK,
            Blocks.BIRCH_SIGN,
            Blocks.BIRCH_WALL_SIGN,
            Blocks.CHERRY_SIGN,
            Blocks.CHERRY_WALL_SIGN,
            Blocks.CHEST,
            Blocks.CRIMSON_SIGN,
            Blocks.CRIMSON_WALL_SIGN,
            Blocks.DARK_OAK_SIGN,
            Blocks.DARK_OAK_WALL_SIGN,
            Blocks.DIAMOND_BLOCK,
            Blocks.ENDER_CHEST,
            Blocks.IRON_DOOR,
            Blocks.JUNGLE_SIGN,
            Blocks.JUNGLE_WALL_SIGN,
            Blocks.MANGROVE_SIGN,
            Blocks.MANGROVE_WALL_SIGN,
            Blocks.NETHERITE_BLOCK,
            Blocks.OAK_SIGN,
            Blocks.OAK_WALL_SIGN,
            Blocks.REDSTONE_WIRE,
            Blocks.REDSTONE_TORCH,
            Blocks.SPRUCE_SIGN,
            Blocks.TRAPPED_CHEST,
            Blocks.WARPED_SIGN,
            Blocks.WARPED_WALL_SIGN
    );

    public static boolean isSteveLovedItem(ItemStack pItemsTack) {
        return pItemsTack.is(ModTags.Items.STEVE_LOVED);
    }

    // Other Logic And Definitions
    String[] STEVE_NAMES = {
            "Player",
            "Steve",
            "SlenderKinGamer",
            "NotCCG"
    };

    public String getSteveName() {
        int index = RANDOM.nextInt(STEVE_NAMES.length);
        return STEVE_NAMES[index];
    }

    public static boolean isUnoccupiedBed(Level level, BlockPos pos) {
        BlockState pBlockstate = level.getBlockState(pos);
        if (!(pBlockstate.getBlock() instanceof BedBlock)) return false;
        return !pBlockstate.getValue(BedBlock.OCCUPIED);
    }
}
