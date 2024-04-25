package net.notccg.yahresurrected.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.block.ModBlocks;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, YouAreHerobrineResurrected.MOD_ID);

    public static final RegistryObject<CreativeModeTab> YAHR = CREATIVE_MODE_TABS.register("yahr_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.STEVESOUL.get()))
                    .title(Component.translatable("creativetab.yahr_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.VILLAGERHEART.get());
                        output.accept(ModItems.STEVESOUL.get());
                        output.accept(ModItems.SKINBOOK.get());
                        output.accept(ModItems.INVISIBLEDUST.get());
                        output.accept(ModItems.FLESHSTICK.get());
                        output.accept(ModItems.ICERUBY.get());
                        output.accept(ModItems.EVILDIAMONDMATTER.get());
                        output.accept(ModItems.EVILDIAMOND.get());
                        output.accept(ModItems.SPELLBOOKI.get());
                        output.accept(ModItems.SPELLBOOKII.get());
                        output.accept(ModItems.SPELLBOOKIII.get());
                        output.accept(ModItems.SPELLBOOKIV.get());
                        output.accept(ModItems.SPELLBOOKV.get());
                        output.accept(ModItems.EVILDIAMONDPICKAXE.get());
                        output.accept(ModItems.EVILDIAMONDAXE.get());
                        output.accept(ModItems.EVILDIAMONDSHOVEL.get());
                        output.accept(ModItems.EVILDIAMONDHOE.get());
                        output.accept(ModItems.EVILDIAMONDSWORD.get());
                        output.accept(ModItems.BEDROCKPIXAXE.get());
                        output.accept(ModItems.STEVE_SPAWN_EGG.get());

                        output.accept(ModBlocks.ICE_RUBY_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_ICE_RUBY_ORE.get());
                        output.accept(ModBlocks.INVISIBLE_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_INVISIBLE_ORE.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
