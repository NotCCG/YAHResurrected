package net.notccg.yahresurrected.item.custom.spell_books;

//Teleportation

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.util.ModTags;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

public class SpellBookFourItem extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();

    public SpellBookFourItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (pLevel.isClientSide()) return InteractionResultHolder.success(stack);
        if (!(pPlayer instanceof ServerPlayer serverPlayer)) return  InteractionResultHolder.pass(stack);

        ServerLevel serverLevel = (ServerLevel) pLevel;
        boolean shiftMode = serverPlayer.isShiftKeyDown();
        AimRay ray = getAimRay(serverPlayer);

        LOGGER.debug("[YAH:R] [ITEM:{}] use() PARAMETERS:[shiftMode={}, player={}, origin={}, dir={}]",
                this.getClass().getSimpleName(),
                (shiftMode ? "ACTIVE" : "INACTIVE"),
                serverPlayer.getGameProfile().getName(),
                ray.origin(),
                ray.direction());

        Optional<Vec3> destOpt = shiftMode
                ? findPhaseTeleportPos(serverLevel, serverPlayer, ray, 32, 0.25, 3)
                : findFarthestTeleportPosition(serverLevel, serverPlayer, ray, 32, 0.25);

        if (destOpt.isEmpty()) {
            LOGGER.debug("[YAH:R] [ITEM:{}] NO VALID DESTINATION | PARAMETERS:[shiftMode={}, pos={}]",
                    this.getClass().getSimpleName(),
                    (shiftMode ? "ACTIVE" : "INACTIVE"),
                    serverPlayer.position());
            return InteractionResultHolder.fail(stack);
        }

        Vec3 dest = destOpt.get();

        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(ModItems.createToolTip("spell_book_iv"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public record AimRay(Vec3 origin, Vec3 direction) {
    }

    private static AimRay getAimRay(ServerPlayer player) {
        Vec3 eyePosition = player.getEyePosition();
        Vec3 aimVector = player.getViewVector(1.0f);
        Vec3 aimVectorNormalized = aimVector.normalize();

        return new AimRay(eyePosition, aimVectorNormalized);
    }

    private static boolean isValidTeleportPos(ServerLevel level, Vec3 candidatePos, ServerPlayer player) {
        BlockPos candidateBlockPos = BlockPos.containing(candidatePos);

        if (candidateBlockPos.getY() < level.getMinBuildHeight()) return false;
        if ((candidateBlockPos.getY() + 1) >= level.getMaxBuildHeight()) return false;

        AABB movedBox = player.getBoundingBox().move(
                candidatePos.x - player.getX(),
                candidatePos.y - player.getY(),
                candidatePos.z - player.getZ()
        );

        if (!level.noCollision(player, movedBox)) return false;

        BlockState candidateState = level.getBlockState(candidateBlockPos);
        BlockPos above = candidateBlockPos.above();
        BlockState aboveState = level.getBlockState(above);
        boolean feetClear = candidateState.getFluidState().isEmpty();
        boolean headClear = aboveState.getFluidState().isEmpty();

        BlockPos below = candidateBlockPos.below();
        BlockState belowState = level.getBlockState(below);
        return feetClear && headClear && validGround(level, below, belowState);
    }

    private static boolean validGround(ServerLevel level, BlockPos pos, BlockState state) {
        boolean validBlock = !state.is(ModTags.Blocks.INVALID_TELEPORT_BLOCKS);
        boolean notLiquid = state.getFluidState().isEmpty();
        boolean isFaceSolid = state.isFaceSturdy(level, pos, Direction.UP);
        return validBlock && notLiquid && isFaceSolid;
    }

    private static Optional<Vec3> findFarthestTeleportPosition(ServerLevel level,
                                                               ServerPlayer player,
                                                               AimRay ray,
                                                               double maxBlockDist,
                                                               double stepBlocks) {
        Vec3 dir = ray.direction().normalize();
        Vec3 origin = ray.origin();

        Vec3 bestTeleportPos = null;

        for (double d = 1.0; d <= maxBlockDist; d += stepBlocks) {
            Vec3 sampleEyePos = origin.add(dir.scale(d));
            Vec3 candidatePos = eyeSampleTeleportCandidate(player, sampleEyePos);

            Optional<Vec3> snappedPos = snapToStandableSurface(level, player, candidatePos);
            if (snappedPos.isPresent()) {
                bestTeleportPos = snappedPos.get();
            }
        }
        return Optional.ofNullable(bestTeleportPos);
    }

    private static Optional<Vec3> snapToStandableSurface(ServerLevel level, ServerPlayer player, Vec3 approxFeetPos) {
        BlockPos column = BlockPos.containing(approxFeetPos);
        double x = column.getX() + 0.5;
        double z = column.getZ() + 0.5;

        CollisionContext context = CollisionContext.of(player);
        int baseY = Mth.floor(approxFeetPos.y);

        Vec3 bestPos = null;

        for (int y = baseY + 1; y >= baseY -3; y--) {
            BlockPos floorPos = new BlockPos(column.getX(), y, column.getZ());
            BlockState floorState = level.getBlockState(floorPos);

            if (!floorState.getFluidState().isEmpty()) continue;
            if (floorState.is(ModTags.Blocks.INVALID_TELEPORT_BLOCKS)) continue;

            Optional<Double> floorTopY = getFloorTopYAtPoint(level, floorPos, floorState, x, z, context);
            if (floorTopY.isEmpty()) continue;

            Vec3 candidateFeet = new Vec3(x, floorTopY.get(), z);

            if (isValidTeleportPos(level, candidateFeet, player)) {
                bestPos = candidateFeet;
                break;
            }
        }
        return Optional.ofNullable(bestPos);
    }

    private static Optional<Double> getFloorTopYAtPoint(ServerLevel level,
                                                        BlockPos pos,
                                                        BlockState state,
                                                        double worldX,
                                                        double worldZ,
                                                        CollisionContext context) {
        VoxelShape shape = state.getCollisionShape(level, pos, context);
        if (shape.isEmpty()) return Optional.empty();

        double localX = worldX - pos.getX();
        double localZ = worldZ - pos.getZ();

        double bestLocalY = Double.NEGATIVE_INFINITY;

        for (AABB box : shape.toAabbs()) {
            if (localX >= box.minX && localX < box.maxX &&
                localZ >= box.minZ && localZ < box.maxZ) {
                bestLocalY = Math.max(bestLocalY, box.maxY);
            }
        }
        if (bestLocalY == Double.NEGATIVE_INFINITY) return Optional.empty();

        return Optional.of(pos.getY() + bestLocalY);
    }

    private static Vec3 eyeSampleTeleportCandidate(ServerPlayer player, Vec3 sampleEyePos) {
        double eyeHeight = player.getEyeHeight();
        return sampleEyePos.subtract(0.0, eyeHeight, 0.0);
    }

    private static boolean wouldCollideAt(ServerLevel level, ServerPlayer player, Vec3 candidatePos) {
        AABB movedBox = player.getBoundingBox().move(
                candidatePos.x - player.getX(),
                candidatePos.y - player.getY(),
                candidatePos.z - player.getZ()
        );
        return !level.noCollision(player, movedBox);
    }

    private static Optional<Vec3> findPhaseTeleportPos(ServerLevel level,
                                                       ServerPlayer player,
                                                       AimRay aimRay,
                                                       double maxBlockDist,
                                                       double stepBlocks,
                                                       double maxWallThickness) {
        Vec3 origin = aimRay.origin();
        Vec3 dir = aimRay.direction().normalize();
        Vec3 end = origin.add(dir.scale(maxBlockDist));

        BlockHitResult hit = level.clip(new ClipContext(
                origin, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player
        ));

        if (hit.getType() == HitResult.Type.MISS) return Optional.empty();

        double hitDist = hit.getLocation().distanceTo(origin);

        boolean insideWall = false;
        double wallEntryDist = hitDist;

        for (double d = Math.max(1.0, hitDist - stepBlocks); d <= maxBlockDist; d += stepBlocks) {
            Vec3 sampleEyePos = origin.add(dir.scale(d));
            Vec3 approxFeetPos = eyeSampleTeleportCandidate(player, sampleEyePos);

            boolean colliding = wouldCollideAt(level, player, approxFeetPos);

            if (!insideWall) {
                if (colliding) {
                    insideWall = true;
                    wallEntryDist = d;
                }
                continue;
            }

            double thickness = d - wallEntryDist;
            if (thickness > maxWallThickness) {
                return Optional.empty();
            }

            if (!colliding) {
                double maxBehindSearch = 2.0;

                for (double d2 = d; d2 <= d + maxBehindSearch; d2 += stepBlocks) {
                    Vec3 sampleEyePos2 = origin.add(dir.scale(d2));
                    Vec3 approxFeetPos2 = eyeSampleTeleportCandidate(player, sampleEyePos2);

                    Optional<Vec3> snappedPos = snapToStandableSurface(level, player, approxFeetPos2);
                    if (snappedPos.isPresent()) {
                        return snappedPos;
                    }
                }
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}
