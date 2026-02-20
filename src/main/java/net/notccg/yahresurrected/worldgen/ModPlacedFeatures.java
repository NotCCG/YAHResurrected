package net.notccg.yahresurrected.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> ICE_GEM_ORE_PLACED_KEY = registerKey("ice_ruby_ore_placed");
    public static final ResourceKey<PlacedFeature> INVISIBLE_ORE_PLACED_KEY = registerKey("invisible_ore_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, ICE_GEM_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_ICE_GEM_ORE_KEY),
                ModOrePlacement.commonOrePlacement(60,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(75))));

        register(context, INVISIBLE_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_INVISIBLE_ORE_KEY),
                ModOrePlacement.commonOrePlacement(45,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));

    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
