package net.notccg.yahresurrected;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.notccg.yahresurrected.block.ModBlocks;
import net.notccg.yahresurrected.entity.ModEntities;
import net.notccg.yahresurrected.entity.client.hunter.HunterRenderer;
import net.notccg.yahresurrected.entity.client.steve.SteveRenderer;
import net.notccg.yahresurrected.item.ModCreativeModeTabs;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.loot.ModLootModifiers;
import net.notccg.yahresurrected.util.ModStructures;

@Mod(YouAreHerobrineResurrected.MOD_ID)
public class YouAreHerobrineResurrected {
    public static final String MOD_ID = "youareherobrineresurrected";

    public YouAreHerobrineResurrected() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);

        ModLootModifiers.register(modEventBus);
        ModStructures.STRUCTURE_TYPE_DEFERRED_REGISTER.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }



    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.STEVE.get(), SteveRenderer::new);
            EntityRenderers.register(ModEntities.HUNTER.get(), HunterRenderer::new);
        }
    }
}



//You aren't a good person, you know that, right?
//Good people don't end up here
