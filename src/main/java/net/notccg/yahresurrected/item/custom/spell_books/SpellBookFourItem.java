package net.notccg.yahresurrected.item.custom.spell_books;

//Teleportation

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
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
import net.notccg.yahresurrected.util.ModDebugUtils;
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

        ModDebugUtils.debugItem(this, "use()",
                "shiftMode", shiftMode ? "ACTIVE" : "INACTIVE",
                "player", serverPlayer.getGameProfile().getName(),
                "origin", ray.origin(),
                "dir", ray.direction());

        Optional<Vec3> destOpt = shiftMode
                ? findPhaseTeleportPos(serverLevel, serverPlayer, ray)
                : findFarthestTeleportPosition(serverLevel, serverPlayer, ray);

        if (destOpt.isEmpty()) {
            ModDebugUtils.debugItemFail(this, "use()", "NO VALID DESTINATION",
                    "playerPos", serverPlayer.position());
            return InteractionResultHolder.fail(stack);
        }

        Vec3 dest = destOpt.get();

        debugTeleportationValidation(this, serverLevel, dest, serverPlayer);

        if (!isValidTeleportPos(serverLevel, dest, serverPlayer)) {
            InteractionResultHolder.fail(stack);
        }

        serverLevel.sendParticles(ParticleTypes.PORTAL,
                serverPlayer.getX() + 0.5,
                serverPlayer.getY() + 1.0,
                serverPlayer.getZ() + 0.5,
                20,
                0.25D, 0.25D, 0.25D,
                0.01D);

        serverPlayer.teleportTo(serverLevel, dest.x, dest.y, dest.z, serverPlayer.getYRot(), serverPlayer.getXRot());
        serverPlayer.fallDistance = 0.0F;

        serverLevel.sendParticles(ParticleTypes.PORTAL,
                dest.x + 0.5,
                dest.y + 1.0,
                dest.z + 0.5,
                20,
                0.25D, 0.25D, 0.25D,
                0.01D);

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
                                                               AimRay ray) {
        Vec3 dir = ray.direction().normalize();
        Vec3 origin = ray.origin();

        Vec3 bestTeleportPos = null;

        for (double d = 1.0; d <= (double) 32; d += 0.25) {
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
                                                       AimRay aimRay) {
        Vec3 origin = aimRay.origin();
        Vec3 dir = aimRay.direction().normalize();
        Vec3 end = origin.add(dir.scale(32));

        BlockHitResult hit = level.clip(new ClipContext(
                origin, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player
        ));

        if (hit.getType() == HitResult.Type.MISS) return Optional.empty();

        double hitDist = hit.getLocation().distanceTo(origin);

        boolean insideWall = false;
        double wallEntryDist = hitDist;

        for (double d = Math.max(1.0, hitDist - 0.25); d <= (double) 32; d += 0.25) {
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
            if (thickness > (double) 3) {
                return Optional.empty();
            }

            if (!colliding) {
                double maxBehindSearch = 2.0;

                for (double d2 = d; d2 <= d + maxBehindSearch; d2 += 0.25) {
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

    // Debug specific to this class

    private static void debugTeleportationValidation(Item item,
                                                     ServerLevel level,
                                                     Vec3 candidatePos,
                                                     ServerPlayer player) {
        BlockPos candidateBlockPos = BlockPos.containing(candidatePos);

        if (candidateBlockPos.getY() < level.getMinBuildHeight()) {
            ModDebugUtils.debugItem(item,
                    "validateTeleportPos",
                    "result", "FAIL",
                    "reason", "below_minimum_build_limit",
                    "candidatePos", candidatePos,
                    "candidateBlockPos", candidateBlockPos,
                    "minY", level.getMinBuildHeight());
            return;
        }
        if ((candidateBlockPos.getY() + 1) >= level.getMaxBuildHeight()) {
            ModDebugUtils.debugItem(item,
                    "validateTeleportPos",
                    "result", "FAIL",
                    "reason", "above_build_limit",
                    "candidatePos", candidatePos,
                    "candidateBlockPos", candidateBlockPos,
                    "maxY", level.getMaxBuildHeight());
        }
        var moveBox = player.getBoundingBox().move(
                candidatePos.x - player.getX(),
                candidatePos.y - player.getY(),
                candidatePos.z - player.getZ()
        );

        if (!level.noCollision(player, moveBox)) {
            ModDebugUtils.debugItem(item,
                    "validateTeleportPos",
                    "result", "FAIL",
                    "reason", "above_build_limit",
                    "candidatePos", candidatePos,
                    "aabb", moveBox);
            return;
        }

        var feetState = level.getBlockState(candidateBlockPos);
        var headState = level.getBlockState(candidateBlockPos.above());

        if (!feetState.getFluidState().isEmpty()) {
            ModDebugUtils.debugItem(item,
                    "validateTeleportPos()",
                    "result", "FAIL",
                    "reason", "feet_in_fluid",
                    "candidatePos", candidatePos,
                    "feetBlock", candidateBlockPos,
                    "feetState", feetState
            );
            return;
        }

        if (!headState.getFluidState().isEmpty()) {
            ModDebugUtils.debugItem(item,
                    "validateTeleportPos()",
                    "result", "FAIL",
                    "reason", "head_in_fluid",
                    "candidatePos", candidatePos,
                    "headBlock", candidateBlockPos.above(),
                    "headState", headState
            );
            return;
        }

        BlockPos below = candidateBlockPos.below();
        var belowState = level.getBlockState(below);

        if (belowState.is(ModTags.Blocks.INVALID_TELEPORT_BLOCKS)) {
            ModDebugUtils.debugItem(item,
                    "validateTeleportPos()",
                    "result", "FAIL",
                    "reason", "ground_block_invalid (blacklisted_blocks)",
                    "candidatePos", candidatePos,
                    "belowBlock", below,
                    "belowState", belowState);
            return;
        }

        if (!belowState.getFluidState().isEmpty()) {
            ModDebugUtils.debugItem(item,
                    "validateTeleportPos()",
                    "result", "FAIL",
                    "reason", "ground_block_invalid (fluid)",
                    "candidatePos", candidatePos,
                    "belowBlock", below,
                    "belowState", belowState);
            return;
        }

        if (!belowState.isFaceSturdy(level, below, Direction.UP)) {
            ModDebugUtils.debugItem(item,
                    "validateTeleportPos()",
                    "result", "FAIL",
                    "reason", "ground_block_invalid (not_solid)",
                    "candidatePos", candidatePos,
                    "belowBlock", below,
                    "belowState", belowState);
            return;
        }
        ModDebugUtils.debugItem(item, "validateTeleportPos()",
                "result", "PASS",
                "candidatePos", candidatePos);
    }
}
