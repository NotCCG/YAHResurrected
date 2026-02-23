package net.notccg.yahresurrected.event;


import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.custom.AbstractHunter;
import net.notccg.yahresurrected.entity.custom.Hunter;
import net.notccg.yahresurrected.entity.custom.Slayer;
import net.notccg.yahresurrected.entity.custom.Steve;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.item.custom.spell_books.SpellBookOneItem;
import net.notccg.yahresurrected.util.BrokenClockUseHandler;
import net.notccg.yahresurrected.util.ModConfigServer;
import org.slf4j.Logger;


public class ModEvents {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String SUNBURN_DELAY_UNTIL = "yahr_sunburnDelayUntil";
    private static final String DEATH_NAME_OVERRIDE = "yahr_deathNameOverride";


    @Mod.EventBusSubscriber(modid = YouAreHerobrineResurrected.MOD_ID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
            if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
            serverPlayer.getPersistentData().putLong(SUNBURN_DELAY_UNTIL, serverPlayer.level().getGameTime() + 40L);

            if (!(ModConfigServer.JOIN_MESSAGE_ENABLE.get())) return;
            Component message = Component.literal("Herobrine joined the game")
                    .withStyle(ChatFormatting.YELLOW);

            serverPlayer.server.getPlayerList().broadcastSystemMessage(message, false);
        }

        @SubscribeEvent
        public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
            if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
            serverPlayer.getPersistentData().putLong(SUNBURN_DELAY_UNTIL, serverPlayer.level().getGameTime() + 40L);
        }

        @SubscribeEvent
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
            if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
            serverPlayer.getPersistentData().putLong(SUNBURN_DELAY_UNTIL, serverPlayer.level().getGameTime() + 40L);
        }

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
            if (event.phase != TickEvent.Phase.END) return;

            Player player = event.player;
            Level level = player.level();

            long until = player.getPersistentData().getLong(SUNBURN_DELAY_UNTIL);
            if (until != 0L && level.getGameTime() < until) return;

            if (!level.isDay()) return;
            if (level.dimension() != Level.OVERWORLD) return;
            if (level.isRaining() || level.isThundering()) return;
            if (player.isOnFire() || hasSpellBookII(player) || player.isCreative()) return;
            if (!level.canSeeSky(player.blockPosition())) return;
            player.setSecondsOnFire(3);
            LOGGER.debug("[YAH:R [onPlayerTick][{}] Setting player on fire | Conditions: isDay[{}], isOnFire[{}], hasSpellBookII[{}], isCreative[{}], canSeeSky[{}]",
                    player.getUUID(), level.isDay(), player.isOnFire(), hasSpellBookII(player), player.isCreative(), level.canSeeSky(player.blockPosition()));
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
        public static void onLevelTick(TickEvent.LevelTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;
            if (!(event.level instanceof ServerLevel level)) return;
            if (level.dimension() != Level.OVERWORLD) return;
            if (!BrokenClockUseHandler.isSkipping(level)) return;
            BrokenClockUseHandler.tick(level);
        }

        @SubscribeEvent
        public static void onLivingChangeTargetEvent(LivingChangeTargetEvent event) {
            LivingEntity entity = event.getEntity();
            LivingEntity target = event.getNewTarget();

            if (!(target instanceof Player player)) return;
            if (!(entity instanceof Monster)) return;
            if (entity.getLastHurtByMob() instanceof Player) return;

            boolean allowedToTarget =
                    (entity instanceof Creeper) && (!hasSpellBookVI(player)) ||
                            (entity instanceof EnderMan && !hasSpellBookVII(player)) ||
                            (entity instanceof AbstractHunter && SpellBookOneItem.isCloakActivated(player));

            if (!allowedToTarget) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onLivingDeath(LivingDeathEvent event) {
            Entity entity = event.getEntity();
            if (!(entity instanceof ServerPlayer serverPlayer)) return;
            long until = serverPlayer.level().getGameTime() + 5L;
            serverPlayer.getPersistentData().putLong(DEATH_NAME_OVERRIDE, until);
        }

        @SubscribeEvent
        public static void onServerStartup(ServerStartedEvent event) {
            MinecraftServer server = event.getServer();
            for (ServerLevel level : server.getAllLevels()) {
                applyPhanTomConfig(server, level);
            }
        }

        @SubscribeEvent
        public static void onLevelLoad(LevelEvent.Load event) {
            if (!(event.getLevel() instanceof ServerLevel level)) return;
            MinecraftServer server = level.getServer();
            if (server == null) return;
            applyPhanTomConfig(server, level);
        }

        @SubscribeEvent
        public static void onNameFormat(PlayerEvent.NameFormat event) {
            if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

            long until = serverPlayer.getPersistentData().getLong(DEATH_NAME_OVERRIDE);
            if (until == 0L) return;

            long now = serverPlayer.level().getGameTime();
            if (now > until) {
                serverPlayer.getPersistentData().remove(DEATH_NAME_OVERRIDE);
                return;
            }
            event.setDisplayname(Component.literal("Herobrine"));
        }

        @SubscribeEvent
        public static void onEntityJoin(EntityJoinLevelEvent event) {
            if (!(event.getEntity() instanceof Mob mob)) return;
            if (mob instanceof Enemy) {
                mob.targetSelector.addGoal(2,
                        new NearestAttackableTargetGoal<>(mob, Steve.class, true));
                mob.targetSelector.addGoal(2,
                        new NearestAttackableTargetGoal<>(mob, Hunter.class, true));
                mob.targetSelector.addGoal(2,
                        new NearestAttackableTargetGoal<>(mob, Slayer.class, true));
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

    private static void applyPhanTomConfig(MinecraftServer server, ServerLevel level) {
        boolean disable = ModConfigServer.DISABLE_PHANTOMS.get();
        level.getGameRules().getRule(GameRules.RULE_DOINSOMNIA).set(!disable, server);
    }
}