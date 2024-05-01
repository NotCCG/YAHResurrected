package net.notccg.yahresurrected.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MovementUtils {
    public static BlockPos getBlockPositionInDirection(Level level, Player player, Vec3i direction, double maxDistance) {
        double startX = player.getX();
        double startY = player.getY();
        double startZ = player.getZ();
        double length = Math.sqrt(direction.getX() * direction.getX() + direction.getY() * direction.getY() + direction.getZ() * direction.getZ());
        double dirX = direction.getX() / length;
        double dirY = direction.getY() / length;
        double dirZ = direction.getZ() / length;
        Vec3 currentPos = new Vec3(startX, startY, startZ);
        Vec3 step = new Vec3(dirX, dirY, dirZ).scale(0.1);
        for (double distance = 0; distance < maxDistance; distance += 0.1) {
            currentPos = currentPos.add(step);
            BlockPos blockPos = new BlockPos((int) currentPos.x, (int) currentPos.y, (int) currentPos.z);
            if (level.getBlockState(blockPos).isCollisionShapeFullBlock(level, blockPos)) {
                for (int i = 1; i <= 3; i++) {
                    BlockPos newPos = blockPos.offset((int) new Vec3(direction.getX(), direction.getY(), direction.getZ()).normalize().x, (int) new Vec3(direction.getX(), direction.getY(), direction.getZ()).normalize().y, (int) new Vec3(direction.getX(), direction.getY(), direction.getZ()).normalize().z);
                    if (!level.getBlockState(newPos).isCollisionShapeFullBlock(level, newPos)) {
                        return newPos;
                    }
                }
                return blockPos;
            }
        }
        return new BlockPos((int) currentPos.x, (int) currentPos.y, (int) currentPos.z);
    }
}
