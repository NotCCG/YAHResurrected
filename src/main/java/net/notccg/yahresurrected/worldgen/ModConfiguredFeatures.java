package net.notccg.yahresurrected.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.block.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ICE_RUBY_ORE_KEY = registerKey("iceruby_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_INVISIBLE_ORE_KEY = registerKey("invisible_ore");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> overworldIceRubyOres = List.of(OreConfiguration.target(stoneReplaceable,
                ModBlocks.ICE_RUBY_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceable, ModBlocks.DEEPSLATE_ICE_RUBY_ORE.get().defaultBlockState()));

        List<OreConfiguration.TargetBlockState> overworldInvisibleOres = List.of(OreConfiguration.target(stoneReplaceable,
                ModBlocks.INVISIBLE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceable, ModBlocks.DEEPSLATE_INVISIBLE_ORE.get().defaultBlockState()));

        register(context, OVERWORLD_ICE_RUBY_ORE_KEY, Feature.ORE, new OreConfiguration(overworldIceRubyOres, 1));
        register(context, OVERWORLD_INVISIBLE_ORE_KEY, Feature.ORE, new OreConfiguration(overworldInvisibleOres, 3));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
