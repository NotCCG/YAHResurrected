package net.notccg.yahresurrected.event;



import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.custom.SteveEntity;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.item.custom.SpellBookTwoItem;


public class ModEvents {
    @Mod.EventBusSubscriber(modid = YouAreHerobrineResurrected.MOD_ID)
    public static class ForgeEvents {
        private static final int MAX_HEIGHT_ABOVE_PLAYER = 64;

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

        @SubscribeEvent
        public static void onLivingDeath(LivingDeathEvent event) {
            LivingEntity entity = event.getEntity();
            if (entity instanceof SteveEntity) {
                Level world = entity.level();
                BlockPos pos = entity.blockPosition();
                ItemStack itemStack = new ItemStack(ModItems.STEVESOUL.get());
                ItemDropper.dropItem(world, pos, itemStack);
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

    public class ItemDropper {

        public static void dropItem(Level world, BlockPos pos, ItemStack itemStack) {
            ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);

            world.addFreshEntity(itemEntity);
        }
    }
}

