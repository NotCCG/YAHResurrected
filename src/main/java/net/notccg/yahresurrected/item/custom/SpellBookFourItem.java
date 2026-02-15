package net.notccg.yahresurrected.item.custom;

//Teleportation

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.notccg.yahresurrected.util.MovementUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

import static net.notccg.yahresurrected.util.MovementUtils.getBlockPositionInDirection;

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

        ItemStack $$3 = pPlayer.getItemInHand(pUsedHand);
        pPlayer.getCooldowns().addCooldown(this, 10);
        Direction playerDirection = pPlayer.getDirection();
        BlockPos blockPos = MovementUtils.getBlockPositionInDirection(pLevel, pPlayer, playerDirection.getNormal(), 10);
        double posX = blockPos.getX();
        double posY = blockPos.getY();
        double posZ = blockPos.getZ();

        pPlayer.teleportTo(posX , posY, posZ);
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
        return InteractionResultHolder.sidedSuccess($$3, pLevel.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.youareherobrineresurrected.spell_book_iv.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
