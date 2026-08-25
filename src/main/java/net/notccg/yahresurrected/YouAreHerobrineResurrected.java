package net.notccg.yahresurrected;

import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.notccg.yahresurrected.block.ModBlocks;
import net.notccg.yahresurrected.entity.ModEntities;
import net.notccg.yahresurrected.entity.client.renderer.HunterRenderer;
import net.notccg.yahresurrected.entity.client.renderer.JebRenderer;
import net.notccg.yahresurrected.entity.client.renderer.SlayerRenderer;
import net.notccg.yahresurrected.entity.client.renderer.SteveRenderer;
import net.notccg.yahresurrected.fluids.ModFluidTypes;
import net.notccg.yahresurrected.fluids.ModFluids;
import net.notccg.yahresurrected.util.config.ModConfigClent;
import net.notccg.yahresurrected.util.config.ModConfigCommon;
import net.notccg.yahresurrected.util.config.ModConfigServer;
import net.notccg.yahresurrected.util.init.ModMenus;
import net.notccg.yahresurrected.item.ModCreativeModeTabs;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.loot.ModLootModifiers;
import net.notccg.yahresurrected.potion.BetterBrewingRecipe;
import net.notccg.yahresurrected.potion.ModPotions;
import net.notccg.yahresurrected.world.sound.ModSounds;
import net.notccg.yahresurrected.util.*;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod(YouAreHerobrineResurrected.MOD_ID)
public class YouAreHerobrineResurrected {

    public static final String MOD_ID = "yahr";

    public YouAreHerobrineResurrected() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModEntities.register(modEventBus);

        ModFluidTypes.register(modEventBus);
        ModFluids.register(modEventBus);

        ModPotions.register(modEventBus);

        ModMemoryTypes.MEMORY_MODULE_TYPES.register(modEventBus);
        ModSensorTypes.SENSOR_TYPES.register(modEventBus);

        ModLootModifiers.register(modEventBus);
        ModSounds.register(modEventBus);

        ModMenus.REGISTRY.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigCommon.SPEC, YouAreHerobrineResurrected.MOD_ID + "-common-config.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ModConfigServer.SPEC, YouAreHerobrineResurrected.MOD_ID + "-server-config.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModConfigClent.SPEC, YouAreHerobrineResurrected.MOD_ID + "-client-config.toml");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SpawnPlacements.register(ModEntities.HUNTER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    ModSpawnRules::canSpawnMostlyInDay
            );

            SpawnPlacements.register(ModEntities.SLAYER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    ModSpawnRules::canSpawnMostlyInDay);

            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, Items.EXPERIENCE_BOTTLE, ModPotions.DILUTED_ENCHANTING.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(ModPotions.DILUTED_ENCHANTING.get(), ModItems.BLOOD_BOTTLE.get(), ModPotions.DEMONIC_POTION.get()));
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.HUNTER_SPAWN_EGG);
            event.accept(ModItems.SLAYER_SPAWN_EGG);
            event.accept(ModItems.STEVE_SPAWN_EGG);
        }
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModBlocks.ICE_RUBY_ORE);
            event.accept(ModBlocks.DEEPSLATE_ICE_RUBY_ORE);
            event.accept(ModBlocks.INVISIBLE_ORE);
            event.accept(ModBlocks.DEEPSLATE_INVISIBLE_ORE);
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.EVILDIAMONDSWORD);
            event.accept(ModItems.CASTCREEPERBOOK);
            event.accept(ModItems.CASTSKELETONBOOK);
            event.accept(ModItems.CASTZOMBIEBOOK);
            event.accept(ModItems.SPELLBOOKI);
            event.accept(ModItems.SPELLBOOKII);
            event.accept(ModItems.SPELLBOOKVI);
            event.accept(ModItems.SPELLBOOKVII);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.BROKEN_CLOCK);
            event.accept(ModItems.EVILDIAMONDAXE);
            event.accept(ModItems.EVILDIAMONDHOE);
            event.accept(ModItems.EVILDIAMONDPICKAXE);
            event.accept(ModItems.EVILDIAMONDSHOVEL);
            event.accept(ModItems.SPELLBOOKIII);
            event.accept(ModItems.SPELLBOOKVIII);
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.EVILDIAMONDMATTER);
            event.accept(ModItems.EVILDIAMOND);
            event.accept(ModItems.FLESHSTICK);
            event.accept(ModItems.ICERUBY);
            event.accept(ModItems.ILLAGERHEART);
            event.accept(ModItems.INVISIBLEDUST);
            event.accept(ModItems.SKINBOOK);
            event.accept(ModItems.STEVESOUL);
            event.accept(ModItems.VILLAGERHEART);
        }
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModItems.NETHERPORTALITEM);
        }
    }



    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.STEVE.get(), SteveRenderer::new);
            EntityRenderers.register(ModEntities.HUNTER.get(), HunterRenderer::new);
            EntityRenderers.register(ModEntities.SLAYER.get(), SlayerRenderer::new);
            EntityRenderers.register(ModEntities.JEB_.get(), JebRenderer::new);
        }
    }

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, MOD_ID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }

    private static final Queue<IntObjectPair<Runnable>> workToBeScheduled = new ConcurrentLinkedQueue<>();
    private static final PriorityQueue<TickTask> workQueue = new PriorityQueue<>(Comparator.comparingInt(TickTask::getTick));

    public static void queueServerWork(int delay, Runnable action) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            workToBeScheduled.add(new IntObjectImmutablePair<>(delay, action));
    }

    @SubscribeEvent
    public void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            int currentTick = event.getServer().getTickCount();
            IntObjectPair<Runnable> work;
            while ((work = workToBeScheduled.poll()) != null) {
                workQueue.add(new TickTask(currentTick + work.leftInt(), work.right()));
            }
            while (!workQueue.isEmpty() && currentTick >= workQueue.peek().getTick()) {
                workQueue.poll().run();
            }
        }
    }

}



//You aren't a good person, you know that, right?
//Good people don't end up here
