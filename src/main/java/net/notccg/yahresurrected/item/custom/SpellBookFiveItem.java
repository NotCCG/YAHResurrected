package net.notccg.yahresurrected.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpellBookFiveItem extends Item {
    //Flight

    public SpellBookFiveItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.youareherobrineresurrected.spell_book_v.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (pPlayer.isCreative() || pPlayer.isSpectator()) {
            return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
        }

        if (!pLevel.isClientSide) {
            boolean newMayFly = !pPlayer.getAbilities().mayfly;
            pPlayer.getAbilities().mayfly = newMayFly;

            if (!newMayFly) {
                pPlayer.getAbilities().flying = false;
                pPlayer.onUpdateAbilities();

                pPlayer.displayClientMessage(
                        Component.literal("Flight " + (newMayFly ? "enabled" : "disbled")),
                                true
                );
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
    }
}
