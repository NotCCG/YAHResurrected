package net.notccg.yahresurrected.item.custom;

//Invisibility

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

public class SpellBookOneItem extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();
    public SpellBookOneItem(Properties pProperties) {
        super(pProperties);
    }

    public static final String KEY = YouAreHerobrineResurrected.MOD_ID + "_spell_book_one_activated";

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.youareherobrineresurrected.spell_book_i.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        LOGGER.debug("[YAH:R] [ITEM:{}] use", this.getClass().getSimpleName());

        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);

        if (!(pPlayer instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide);
        }

        CompoundTag compoundTag = serverPlayer.getPersistentData();
        boolean newState = !compoundTag.getBoolean(KEY);

        compoundTag.putBoolean(KEY, newState);

        serverPlayer.displayClientMessage(
                Component.literal("Player Cloak " + (newState ? "ON" : "OFF")),
                true
        );

        LOGGER.debug("[YAH:R] [ITEM:{}] {}", this.getClass().getSimpleName(), newState);

        return InteractionResultHolder.success(itemStack);
    }

    public static boolean isCloakActivated(Player player) {
        return player.getPersistentData().getBoolean(KEY);
    }
}
