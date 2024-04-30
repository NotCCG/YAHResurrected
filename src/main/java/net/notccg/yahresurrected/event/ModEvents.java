package net.notccg.yahresurrected.event;



import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.item.custom.SpellBookTwoItem;


public class ModEvents {
    @Mod.EventBusSubscriber(modid = YouAreHerobrineResurrected.MOD_ID)
    public static class ForgeEvents {
        private static final int MAX_HEIGHT_ABOVE_PLAYER = 32;

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

            Player player = event.player;
            Level world = player.level();
            if (event.side == LogicalSide.SERVER) {
                if (world.isDay() && !hasSpecificItem(player)) {
                    int playerX = (int) player.getX();
                    int playerY = (int) player.getY();
                    int playerZ = (int) player.getZ();
                    for (int y = playerY + 1; y < Math.min(playerY + MAX_HEIGHT_ABOVE_PLAYER, world.getMaxBuildHeight()); y++) {
                        BlockPos blockPos = new BlockPos(playerX, y, playerZ);
                        Block blockAbove = world.getBlockState(blockPos).getBlock();
                        if (!blockAbove.equals(Blocks.AIR) && !blockAbove.equals(Blocks.WATER) && !blockAbove.equals(Blocks.LAVA)) {
                            return;
                        }
                    }
                    player.setSecondsOnFire(3);
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

