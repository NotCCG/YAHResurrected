package net.notccg.yahresurrected.entity.custom.logic.steve_ai;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.notccg.yahresurrected.entity.custom.Steve;
import net.notccg.yahresurrected.util.ModMemoryTypes;
import net.notccg.yahresurrected.util.ModTags;

import java.util.List;
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

    // Emotional Logic
    public static double clampEmotion(double v) {
        return Math.max(0.0, Math.min(2.0, v));
    }

    // Fear Logic
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
    public boolean isCalm(Brain<?> brain) {
        return getFear(brain) == 0.0;
    }
    public boolean isSpooked(Brain<?> brain) {
        return getFear(brain) > 0.0 && getFear(brain) < 1.0;
    }
    public boolean isUneasy(Brain<?> brain) {
        return getFear(brain) >= 1.0 && getFear(brain) < 1.5;
    }
    public boolean isScared(Brain<?> brain) {
        return getFear(brain) >= 1.5 && getFear(brain) < 2.0;
    }
    public boolean isTerrified(Brain<?> brain) {
        return getFear(brain) >= 1.0;
    }



    // Curiosity Logic
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
    public boolean isIntrigued(Brain<?> brain) {
        return getCuriosity(brain) > 0.0 && getCuriosity(brain) < 1.0;
    }
    public boolean isCurious(Brain<?> brain) {
        return getCuriosity(brain) >= 1.0 && getCuriosity(brain) < 2.0;
    }
    public boolean isVeryCurious(Brain<?> brain) {
        return getCuriosity(brain) >= 2.0;
    }

    // Interests Logic And Definitions
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
            Blocks.CRIMSON_SIGN,
            Blocks.CRIMSON_WALL_SIGN,
            Blocks.DARK_OAK_SIGN,
            Blocks.DARK_OAK_WALL_SIGN,
            Blocks.DIAMOND_BLOCK,
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
            Blocks.WARPED_SIGN,
            Blocks.WARPED_WALL_SIGN
    );

    public static boolean isSteveLovedItem(ItemStack pItemsTack) {
        return pItemsTack.is(ModTags.Items.STEVE_LOVED);
    }

    public static boolean isUnoccupiedBed(Level level, BlockPos pos) {
        BlockState pBlockstate = level.getBlockState(pos);
        if (!(pBlockstate.getBlock() instanceof BedBlock)) return false;
        return !pBlockstate.getValue(BedBlock.OCCUPIED);
    }
}
