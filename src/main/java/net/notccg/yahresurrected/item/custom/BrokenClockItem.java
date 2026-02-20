package net.notccg.yahresurrected.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.util.BrokenClockUseHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrokenClockItem extends Item {
    public BrokenClockItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(ModItems.createToolTip("broken_clock"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide());
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 60;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public boolean useOnRelease(ItemStack pStack) {
        return true;
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (pLevel.isClientSide() && !pLevel.isDay()) return;
        if (!(pLevel instanceof ServerLevel serverLevel)) return;
        if (!(pLivingEntity instanceof ServerPlayer player)) return;
        if (player.getCooldowns().isOnCooldown(this)) return;

        int usedTicks = this.getUseDuration(pStack) - pTimeCharged;
        if (usedTicks >= 50) {
            BrokenClockUseHandler.startSKip(serverLevel);
            if (!player.isCreative()) {
                player.getCooldowns().addCooldown(this, 800);
            }
        }
    }
}
