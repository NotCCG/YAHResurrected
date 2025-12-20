package net.notccg.yahresurrected.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;

public class ModSpawnRules {
    private ModSpawnRules() {}

    public static boolean canSpawnAnyTime(
            EntityType<? extends Mob> type,
            ServerLevelAccessor level,
            MobSpawnType reason,
            BlockPos pos,
            RandomSource random
    ) {
        if (reason != MobSpawnType.NATURAL) return false;
        if (level.getDifficulty() == Difficulty.PEACEFUL) return false;

        BlockPos below = pos.below();
        if (!level.getBlockState(below).isValidSpawn(level, below, type)) return false;

        return level.noCollision(type.getAABB(
                pos.getX() + 0.5,
                pos.getY(),
                pos.getZ() + 0.5
        ));
    }

    public static boolean canSpawnMostlyInDay(
            EntityType<? extends Mob> type,
            ServerLevelAccessor level,
            MobSpawnType reason,
            BlockPos pos,
            RandomSource random
    ) {
        if (!canSpawnAnyTime(type, level, reason, pos, random)) return false;

        if (level.getLevel().isDay() && level.getBrightness(LightLayer.SKY, pos) < 8) return false;

        return true;
    }
}
