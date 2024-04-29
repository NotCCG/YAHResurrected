package net.notccg.yahresurrected.event;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.item.custom.SpellBookTwoItem;

@Mod.EventBusSubscriber(modid = YouAreHerobrineResurrected.MOD_ID)
public class ModEvents {

    public static class ForgeEvents {
        @SubscribeEvent
        public static void onPlaverTick(TickEvent.PlayerTickEvent event) {
            Player player = event.player;
            Level world = player.level();
            if (event.side == LogicalSide.SERVER) {
                if (world.getSkyDarken() <4 && !hasSpecificItem(player)) {
                    player.sendSystemMessage(Component.literal("You are in the daylight"));

                }
            }

        }
    }
    private static boolean hasSpecificItem(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof SpellBookTwoItem) {
                return true;
            }
        }
        return false;
    }
}

