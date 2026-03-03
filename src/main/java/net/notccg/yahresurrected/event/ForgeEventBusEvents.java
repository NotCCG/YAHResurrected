package net.notccg.yahresurrected.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;

@Mod.EventBusSubscriber(modid = YouAreHerobrineResurrected.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusEvents {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide()) return;

        Player player = event.getEntity();
        BlockPos clickedPos = event.getPos();
        ItemStack held = event.getItemStack();

        boolean isFlintAndSteel = held.is(Items.FLINT_AND_STEEL);
        boolean isFireCharge = held.is(Items.FIRE_CHARGE);

        if (!isFlintAndSteel && !isFireCharge) return;
        if (!level.getBlockState(clickedPos).is(Tags.Blocks.NETHERRACK)) return;

        BlockPos firePos = clickedPos.above();
        if (!level.getBlockState(firePos).canBeReplaced()) return;

    }
}
