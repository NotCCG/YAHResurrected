package net.notccg.yahresurrected.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class SpellBookFiveItem extends Item {
    //Flight

    private static final UUID REACH_UUID = UUID.fromString("2f3a2b6e-3a91-4b2a-9f26-3c6a6e1b8c01");
    private static final double REACH_BONUS = 2.0D;

    private static void applyReach(ServerPlayer sp) {
        AttributeModifier mod = new AttributeModifier(
                REACH_UUID,
                "Flight item reach bonus",
                REACH_BONUS,
                AttributeModifier.Operation.ADDITION
        );

        AttributeInstance blockReach = sp.getAttribute(ForgeMod.BLOCK_REACH.get());
        if (blockReach != null && !blockReach.hasModifier(mod)) {
            blockReach.addTransientModifier(mod);
        }

        AttributeInstance entityReach = sp.getAttribute(ForgeMod.ENTITY_REACH.get());
        if (entityReach != null && !entityReach.hasModifier(mod)) {
            entityReach.addTransientModifier(mod);
        }
    }

    private static void removeReach(ServerPlayer sp) {
        AttributeInstance blockReach = sp.getAttribute(ForgeMod.BLOCK_REACH.get());
        if (blockReach != null) {
            blockReach.removeModifier(REACH_UUID);
        }

        AttributeInstance entityReach = sp.getAttribute(ForgeMod.ENTITY_REACH.get());
        if (entityReach != null) {
            entityReach.removeModifier(REACH_UUID);
        }
    }

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

        if (!(pPlayer instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide());
        }

        if (serverPlayer.isCreative() || serverPlayer.isSpectator()) {
            serverPlayer.displayClientMessage(Component.literal("You can already fly lol"), true);
            return InteractionResultHolder.success(stack);
        }

        boolean enable = !serverPlayer.getAbilities().mayfly;

        serverPlayer.getAbilities().mayfly = enable;
        if (!enable) {
            serverPlayer.getAbilities().flying = false;
            serverPlayer.fallDistance = 0;
        }

        serverPlayer.onUpdateAbilities();

        if (enable) {
            applyReach(serverPlayer);
        } else {
            removeReach(serverPlayer);
        }

        serverPlayer.displayClientMessage(
                Component.literal("Flight " + (enable ? "enabled" : "disabled")),
                true
        );

        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
    }
}
