package net.notccg.yahresurrected.util.ModSensors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneTorchBlock;

public class GetRedstoneTorches {

    BlockPos GetNearbyRedStoneTorches(Block pBlock, BlockPos pBlockPos, int pDistance) {
        return this.getNearbyRedstoneTorch(pBlock, pBlockPos, pDistance);
    }

    public boolean isRedstoneTorch(Block pBlock, BlockPos pPos) {
        return pBlock instanceof RedstoneTorchBlock;
    }

    public boolean isValidBlock(Block pBlock, BlockPos pPos) {
        return isRedstoneTorch(pBlock, pPos);
    }


    BlockPos getNearbyRedstoneTorch(Block pBlock, BlockPos pos, int pDistance) {
        for (int x = -pDistance; x <= pDistance; x++) {
            for (int y = -pDistance; y <= pDistance; y++) {
                for (int z = -pDistance; z <= pDistance; z++) {
                    BlockPos blockPos = pos.offset(x, y, z);
                    if (isValidBlock(pBlock, blockPos)) {
                        return pos;
                    }
                }
            }
        }
        return null;
    }
}
