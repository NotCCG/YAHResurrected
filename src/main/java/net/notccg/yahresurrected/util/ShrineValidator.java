package net.notccg.yahresurrected.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

public class ShrineValidator {
    private ShrineValidator() {}

    private static final BlockPos[] TORCH_OFFSETS = {
            new BlockPos( 0, 0, -1), // north
            new BlockPos( 0, 0,  1), // south
            new BlockPos( 1, 0,  0), // east
            new BlockPos(-1, 0,  0)  // west
    };

    private static final BlockPos[] CORNER_OFFSETS = {
            new BlockPos( 1, 0, -1), // north-east
            new BlockPos(-1, 0, -1), // north-west
            new BlockPos( 1, 0,  1), // south-east
            new BlockPos(-1, 0,  1)  // south-west
    };

    public static boolean isValidUnlit(Level level, BlockPos center) {
        if (!level.getBlockState(center).is(Tags.Blocks.NETHERRACK)) return false;
        return checkCross(level, center) && checkBase(level, center);
    }

    public static boolean isValidLit(Level level, BlockPos center) {
        BlockPos above = center.above();
        if (level.getBlockState(above).is(Blocks.FIRE)) {
            return isValidUnlit(level, center);
        } else return false;
    }

    private static boolean isValidTorch(BlockState state) {
        return state.is(Blocks.REDSTONE_TORCH);
    }

    private static boolean checkCross(Level level, BlockPos center) {
        for (BlockPos off : TORCH_OFFSETS) {
            if (!isValidTorch(level.getBlockState(center.offset(off)))) return false;
        }
        for (BlockPos off : CORNER_OFFSETS) {
            if (!level.getBlockState(center.offset(off)).canBeReplaced()) return false;
        }
        return true;
    }

    private static boolean checkBase(Level level, BlockPos center) {
        BlockPos below = center.below();
        if (!level.getBlockState(below).is(Blocks.MOSSY_COBBLESTONE)) return false;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue;
                if (level.getBlockState(below.offset(dx, 0, dz)).is(Blocks.GOLD_BLOCK)) return false;
            }
        }
        return true;
    }
}
