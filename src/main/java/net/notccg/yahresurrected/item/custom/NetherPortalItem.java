package net.notccg.yahresurrected.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;

public class NetherPortalItem extends Item {
    public NetherPortalItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getPlayer() == null) return InteractionResult.PASS;

        if (pContext.getLevel().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        ServerLevel serverLevel = (ServerLevel) pContext.getLevel();
        BlockPos clickedPos = pContext.getClickedPos().relative(pContext.getClickedFace());
        Direction playerAxis = pContext.getPlayer().getDirection();

        Direction.Axis portalAxis =
                (playerAxis.getAxis() == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;

        Direction widthAxis =  (portalAxis == Direction.Axis.X) ? Direction.EAST : Direction.SOUTH;

        BlockState portalState = Blocks.NETHER_PORTAL.defaultBlockState()
                .setValue(NetherPortalBlock.AXIS, portalAxis);

        serverLevel.setBlock(clickedPos, portalState, 2);

        if (!pContext.getPlayer().getAbilities().instabuild) {
            pContext.getItemInHand().shrink(1);
        }

        return InteractionResult.CONSUME;
    }
}
