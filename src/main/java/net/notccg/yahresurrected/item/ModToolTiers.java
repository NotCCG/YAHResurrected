package net.notccg.yahresurrected.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.util.ModTags;

import java.util.List;

public class ModToolTiers {
    public static final Tier EVIL_DIAMOND = TierSortingRegistry.registerTier(
            new ForgeTier(5, 8000, 30f, 4f, 30,
                    ModTags.Blocks.NEEDS_EVIL_DIAMOND_TOOL, () -> Ingredient.of(ModItems.EVILDIAMOND.get())),
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "evil_diamond"), List.of(Tiers.NETHERITE), List.of());

    public static final  Tier BEDROCK = TierSortingRegistry.registerTier(
            new ForgeTier(6, 30, 80, 1, 1,
            ModTags.Blocks.NEEDS_BEDROCK_TOOL, () -> Ingredient.of(Blocks.BEDROCK)),
            new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "bedrock"), List.of(ModToolTiers.EVIL_DIAMOND), List.of());

}
