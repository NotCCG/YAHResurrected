package net.notccg.yahresurrected;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.notccg.yahresurrected.block.ModBlocks;
import net.notccg.yahresurrected.entity.ModEntities;
import net.notccg.yahresurrected.entity.client.renderer.HunterRenderer;
import net.notccg.yahresurrected.entity.client.renderer.SlayerRenderer;
import net.notccg.yahresurrected.entity.client.renderer.SteveRenderer;
import net.notccg.yahresurrected.item.ModCreativeModeTabs;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.loot.ModLootModifiers;
import net.notccg.yahresurrected.util.*;

@Mod(YouAreHerobrineResurrected.MOD_ID)
public class YouAreHerobrineResurrected {
    public static final String MOD_ID = "youareherobrineresurrected";

    public YouAreHerobrineResurrected() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModMemoryTypes.MEMORY_MODULE_TYPES.register(modEventBus);
        ModSensorTypes.SENSOR_TYPES.register(modEventBus);

        ModLootModifiers.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigCommon.SPEC, YouAreHerobrineResurrected.MOD_ID + "-client-config.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ModConfigServer.SPEC, YouAreHerobrineResurrected.MOD_ID + "-server-config.toml");
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
        if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS) {
            event.accept(ModItems.STEVE_DEBUG_TOOL);
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
        }
    }
}



//You aren't a good person, you know that, right?
//Good people don't end up here
