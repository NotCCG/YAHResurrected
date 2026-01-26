package net.notccg.yahresurrected.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.notccg.yahresurrected.util.ModTags;

public class SpellBookEightItem extends Item {
    public SpellBookEightItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 10;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.SILK_TOUCH;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, book) > 0;
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
        ItemStack stack = pContext.getItemInHand();

        boolean hasSilkTouch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;

        if (clickedBlockState.is(ModTags.Blocks.NECESSARY_BLOCKS)) {
            player.displayClientMessage(Component.literal("You need this to beat the game idiot."), true);
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide) {
            if (hasSilkTouch && clickedBlockState.is()) {

            }
            level.setBlock(clickedBlockPos, Blocks.AIR.defaultBlockState(), 3);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
