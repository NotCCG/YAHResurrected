package net.notccg.yahresurrected.item.custom.spell_books;

//Teleportation

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.util.ModTags;
import net.notccg.yahresurrected.util.MovementUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import oshi.util.tuples.Pair;

import java.util.List;

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
        LOGGER.debug("[YAH:R] [ITEM:{}] use", this.getClass().getSimpleName());
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (pLevel.isClientSide()) return InteractionResultHolder.success(stack);

        boolean shiftMode = pPlayer.isShiftKeyDown();

        if (pPlayer instanceof ServerPlayer serverPlayer) {
            if (shiftMode) {

            } else {

            }
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(ModItems.createToolTip("spell_book_iv"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public record AimRay(Vec3 origin, Vec3 direction) {}
    private static AimRay getAimRay(ServerPlayer player) {
        Vec3 eyePosition = player.getEyePosition();
        Vec3 aimVector = player.getViewVector(1.0f);
        Vec3 aimVectorNormalized = aimVector.normalize();

        return new AimRay(eyePosition, aimVectorNormalized);
    }

    private static boolean isValidTeleportPos(ServerLevel level, Vec3 candidatePos) {
        BlockPos canditateBlockPos = BlockPos.containing(candidatePos);
        BlockState candidateState = level.getBlockState(canditateBlockPos);
        BlockPos below = canditateBlockPos.below();
        BlockPos above = canditateBlockPos.above();
        BlockState belowState = level.getBlockState(below);
        BlockState aboveState = level.getBlockState(above);

        boolean feetClear = candidateState.getCollisionShape(level, canditateBlockPos).isEmpty() &&
                candidateState.getFluidState().isEmpty();
        boolean headClear = aboveState.getCollisionShape(level, above).isEmpty() &&
                aboveState.getFluidState().isEmpty();
        return feetClear && headClear && validGround(level, below, belowState);
    }

    private static boolean validGround(ServerLevel level, BlockPos pos, BlockState state) {
        boolean validBlock = !state.is(ModTags.Blocks.INVALID_TELEPORT_BLOCKS);
        boolean notLiquid = state.getFluidState().isEmpty();
        boolean isFaceSolid = state.isFaceSturdy(level, pos, Direction.UP);
        return validBlock && notLiquid && isFaceSolid;
    }
}
