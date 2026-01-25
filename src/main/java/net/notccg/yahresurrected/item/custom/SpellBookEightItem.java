package net.notccg.yahresurrected.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.notccg.yahresurrected.util.ModTags;

import java.util.List;

public class SpellBookEightItem extends Item {
    public SpellBookEightItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        BlockPos clickedBlockPos = pContext.getClickedPos();
        BlockState clickedBlockState = level.getBlockState(clickedBlockPos);

        if (clickedBlockState.is(ModTags.Blocks.NECESSARY_BLOCKS)) {
            player.displayClientMessage(Component.literal("You need this to beat the game idiot."), true);
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide) {
            level.setBlock(clickedBlockPos, Blocks.AIR.defaultBlockState(), 3);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
