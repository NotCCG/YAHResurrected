package net.notccg.yahresurrected.item.custom;

//Fire Spell

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.notccg.yahresurrected.item.ModItems;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;


public class SpellBookTheeItem extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();
    public SpellBookTheeItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    // This is mostly Jen's code lol
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        BlockPos positionClicked = pContext.getClickedPos();
        BlockState state = level.getBlockState(positionClicked);

        LOGGER.debug("[YAH:R] [ITEM:{}] useOn [BLOCK:{}][BLOCK STATE:{}]",
                this.getClass().getSimpleName(), positionClicked, state);

        if(!CampfireBlock.canLight(state) && !CandleBlock.canLight(state) && !CandleCakeBlock.canLight(state)) {
            BlockPos ClickedFace = positionClicked.relative(pContext.getClickedFace());
            if(BaseFireBlock.canBePlacedAt(level, ClickedFace, pContext.getHorizontalDirection())) {
                level.playSound(player, ClickedFace, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.2F + 0.8F);
                BlockState fireBlock = BaseFireBlock.getState(level, ClickedFace);
                level.setBlock(ClickedFace, fireBlock, 11);
                level.gameEvent(player, GameEvent.BLOCK_PLACE, positionClicked);

            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(ModItems.createToolTip("spell_book_iii"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}