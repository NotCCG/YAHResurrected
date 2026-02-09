package net.notccg.yahresurrected.event;


import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.custom.AbstractHunter;
import net.notccg.yahresurrected.entity.custom.Steve;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.item.custom.SpellBookOneItem;


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
            if (event.side != LogicalSide.SERVER) return;
            Player player = event.player;
            Level level = player.level();
            if (!level.isDay()) return;
            if (player.isOnFire() || hasSpellBookII(player) || player.isCreative()) return;
            if (!level.canSeeSky(player.blockPosition())) return;
            player.setSecondsOnFire(3);
            if (player instanceof ServerPlayer sp) {
                ResourceLocation id = new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "sunburn"); // advancement id
                Advancement adv = sp.server.getAdvancements().getAdvancement(id);
                if (adv != null) {
                    AdvancementProgress progress = sp.getAdvancements().getOrStartProgress(adv);
                    if (!progress.isDone()) {
                        sp.getAdvancements().award(adv, "sunburn_trigger");
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onLivingChangeTargetEvent(LivingChangeTargetEvent event) {
            LivingEntity entity = event.getEntity();
            LivingEntity target = event.getNewTarget();

            if (!(target instanceof Player player)) return;
            if (!(entity instanceof Monster)) return;
            if (entity.getLastHurtByMob() instanceof Player) return;

            boolean allowedToTarget =
                    (entity instanceof Creeper && !hasSpellBookVI(player)) ||
                            (entity instanceof EnderMan && !hasSpellBookVII(player)) ||
                            (entity instanceof AbstractHunter && SpellBookOneItem.isCloakActivated(player));

            if (!allowedToTarget) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onLivingDeath(LivingDeathEvent event) {
            Entity entity = event.getEntity();
            if (entity instanceof Steve) {
                if (event.getSource().getEntity() instanceof Player player) {
                    player.sendSystemMessage(Component.literal(new SteveLogic().getSteveName() + " was slain by Herobrine"));
                }
            }
        }
    }

    private static boolean hasSpellBookII(Player player) {
        return player.getInventory().contains(
                new ItemStack(ModItems.SPELLBOOKII.get())
        );
    }

    private static boolean hasSpellBookVI(Player player) {
        return player.getInventory().contains(
                new ItemStack(ModItems.SPELLBOOKVI.get())
        );
    }

    private static boolean hasSpellBookVII(Player player) {
        return player.getInventory().contains(
                new ItemStack(ModItems.SPELLBOOKVII.get())
        );
    }
}