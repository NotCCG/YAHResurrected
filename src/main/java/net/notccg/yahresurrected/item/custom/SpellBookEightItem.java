package net.notccg.yahresurrected.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpellBookEightItem extends Item {
    public SpellBookEightItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.youareherobrineresurrected.spell_book_viii.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
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
            ServerLevel serverLevel = (ServerLevel) level;

            if (hasSilkTouch && clickedBlockState.is(ModTags.Blocks.ILLEGAL_BLOCK_ITEMS)) {
                if (clickedBlockState.is(Blocks.NETHER_PORTAL)) {
                    ItemStack portalItem = new ItemStack(ModItems.NETHERPORTALITEM.get(), 1);

                    if (!player.getInventory().add(portalItem)) {
                        player.drop(portalItem, false);
                    }
                    serverLevel.setBlock(clickedBlockPos, Blocks.AIR.defaultBlockState(), 2);

                    return InteractionResult.SUCCESS;
                }
                Item asItem = clickedBlockState.getBlock().asItem();

                ItemStack illegalItem = new ItemStack(asItem, 1);
                if (!player.getInventory().add(illegalItem)) {
                    player.drop(illegalItem, false);
                }
                serverLevel.setBlock(clickedBlockPos, Blocks.AIR.defaultBlockState(), 2);
                return InteractionResult.SUCCESS;
            }
            serverLevel.setBlock(clickedBlockPos, Blocks.AIR.defaultBlockState(), 2);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
