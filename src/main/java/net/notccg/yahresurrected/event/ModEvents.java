package net.notccg.yahresurrected.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.server.ServerLifecycleEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.custom.HunterEntity;
import net.notccg.yahresurrected.entity.custom.SteveEntity;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.item.custom.SpellBookOneItem;
import net.notccg.yahresurrected.item.custom.SpellBookSevenItem;
import net.notccg.yahresurrected.item.custom.SpellBookSixItem;
import net.notccg.yahresurrected.item.custom.SpellBookTwoItem;


public class ModEvents {
    @Mod.EventBusSubscriber(modid = YouAreHerobrineResurrected.MOD_ID)
    public static class ForgeEvents {
        private static final int MAX_HEIGHT_ABOVE_PLAYER = 64;
        @SubscribeEvent
        public static void onServerStarted(ServerStartedEvent event) {
            ServerLevel overworld = event.getServer().getLevel(Level.OVERWORLD);
            LevelData levelData = overworld.getLevelData();
            long gameTime = levelData.getGameTime();
            if (gameTime < 1) {
                overworld.setDayTime(13000);
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

            Player player = event.player;
            Level world = player.level();
            if (event.side == LogicalSide.SERVER) {
                if (world.isDay() && !hasSpellBookII(player)) {
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
        public static void onLivingChangeTargetEvent(LivingChangeTargetEvent event) {
            LivingEntity entity = event.getEntity();
            LivingEntity targetEntity = event.getNewTarget();
            if (targetEntity instanceof Player player) {
                if (entity instanceof Monster) {
                    if (entity instanceof Creeper && !hasSpellBookVI(player)) {
                        return;
                    }
                    if (entity instanceof EnderMan && !hasSpellBookVII(player)) {
                        return;
                    }
                    if (entity instanceof HunterEntity && !hasSpellBookI(player)) {
                        return;
                    }
                    event.setCanceled(true);
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

    private static boolean hasSpellBookI(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof SpellBookOneItem) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasSpellBookII(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof SpellBookTwoItem) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasSpellBookVI(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof SpellBookSixItem) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasSpellBookVII(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof SpellBookSevenItem) {
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

