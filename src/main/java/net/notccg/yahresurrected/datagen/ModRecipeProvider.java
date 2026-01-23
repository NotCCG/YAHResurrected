package net.notccg.yahresurrected.datagen;

import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.util.ModTags;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> EVIL_DIAMONDS = List.of(ModItems.EVILDIAMONDMATTER.get());
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    protected static CriterionTriggerInstance hasAny(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(
                ItemPredicate.Builder.item().of(tag).build()
        );
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        oreBlasting(pWriter, EVIL_DIAMONDS, RecipeCategory.MISC, ModItems.EVILDIAMOND.get(), 1f, 100, "evil_diamond");
        oreSmelting(pWriter, EVIL_DIAMONDS, RecipeCategory.MISC, ModItems.EVILDIAMOND.get(), 1f, 100, "evil_diamond");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VILLAGERHEART.get())
                .pattern("IHI")
                .pattern("HGH")
                .pattern("MHN")
                .define('I', ModItems.INVISIBLEDUST.get())
                .define('H', ModItems.ILLAGERHEART.get())
                .define('G', Items.GHAST_TEAR)
                .define('M', Items.GLISTERING_MELON_SLICE)
                .define('N', Items.NETHER_WART)
                .unlockedBy("has_a_heart", hasAny(ModTags.Items.HEARTS))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SKINBOOK.get())
                .pattern(" LP")
                .pattern("LPL")
                .pattern("PL ")
                .define('P', Items.PAPER)
                .define('L', Items.LEATHER)
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SPELLBOOKI.get())
                .pattern("   ")
                .pattern("SBD")
                .pattern("   ")
                .define('S', ModItems.STEVESOUL.get())
                .define('B', ModItems.SKINBOOK.get())
                .define('D', ModItems.INVISIBLEDUST.get())
                .unlockedBy(getHasName(ModItems.SKINBOOK.get()), has(ModItems.SKINBOOK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SPELLBOOKII.get())
                .pattern(" I ")
                .pattern("SBD")
                .pattern("   ")
                .define('S', ModItems.STEVESOUL.get())
                .define('B', ModItems.SKINBOOK.get())
                .define('D', ModItems.INVISIBLEDUST.get())
                .define('I', ModItems.ICERUBY.get())
                .unlockedBy(getHasName(ModItems.SKINBOOK.get()), has(ModItems.SKINBOOK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SPELLBOOKIII.get())
                .pattern(" P ")
                .pattern("SBD")
                .pattern("   ")
                .define('S', ModItems.STEVESOUL.get())
                .define('B', ModItems.SKINBOOK.get())
                .define('D', ModItems.INVISIBLEDUST.get())
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy(getHasName(ModItems.SKINBOOK.get()), has(ModItems.SKINBOOK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SPELLBOOKIV.get())
                .pattern(" E ")
                .pattern("SBD")
                .pattern("   ")
                .define('S', ModItems.STEVESOUL.get())
                .define('B', ModItems.SKINBOOK.get())
                .define('D', ModItems.INVISIBLEDUST.get())
                .define('E', Items.ENDER_EYE)
                .unlockedBy(getHasName(ModItems.SKINBOOK.get()), has(ModItems.SKINBOOK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SPELLBOOKV.get())
                .pattern("EFE")
                .pattern("SBD")
                .pattern("NAN")
                .define('S', ModItems.STEVESOUL.get())
                .define('B', ModItems.SKINBOOK.get())
                .define('D', ModItems.INVISIBLEDUST.get())
                .define('F', Items.FEATHER)
                .define('E', ModItems.EVILDIAMONDMATTER.get())
                .define('N', Items.NETHERITE_INGOT)
                .define('A', Items.ANCIENT_DEBRIS)
                .unlockedBy(getHasName(ModItems.SKINBOOK.get()), has(ModItems.SKINBOOK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SPELLBOOKVI.get())
                .pattern("TMT")
                .pattern("SBD")
                .pattern("GGG")
                .define('T', Items.TNT)
                .define('M', Items.MUSIC_DISC_11)
                .define('S', ModItems.STEVESOUL.get())
                .define('B', ModItems.SKINBOOK.get())
                .define('D', ModItems.INVISIBLEDUST.get())
                .define('G', Items.GUNPOWDER)
                .unlockedBy(getHasName(Items.MUSIC_DISC_11), has(Items.MUSIC_DISC_13))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SPELLBOOKVII.get())
                .pattern("EIE")
                .pattern("SBD")
                .pattern("CCC")
                .define('E', Items.END_CRYSTAL)
                .define('I', Items.ENDER_EYE)
                .define('S', ModItems.STEVESOUL.get())
                .define('B', ModItems.SKINBOOK.get())
                .define('D', ModItems.INVISIBLEDUST.get())
                .define('C', Items.CHORUS_FRUIT)
                .unlockedBy(getHasName(Items.CHORUS_FRUIT), has(Items.CHORUS_FRUIT))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SPELLBOOKVIII.get())
                .pattern("DNE")
                .pattern("dSd")
                .pattern("eIe")
                .define('D', Items.DIAMOND_PICKAXE)
                .define('N', Items.NETHERITE_PICKAXE)
                .define('E', ModItems.EVILDIAMONDPICKAXE.get())
                .define('d', ModItems.EVILDIAMONDMATTER.get())
                .define('S', ModItems.SKINBOOK.get())
                .define('e', Items.ENDER_EYE)
                .define('I', ModItems.INVISIBLEDUST.get())
                .unlockedBy(getHasName(ModItems.SKINBOOK.get()), has(ModItems.SKINBOOK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CASTCREEPERBOOK.get())
                .pattern("GCG")
                .pattern("DBD")
                .pattern("GTG")
                .define('G', Items.GUNPOWDER)
                .define('C', Items.CREEPER_HEAD)
                .define('B', ModItems.SKINBOOK.get())
                .define('D', ModItems.INVISIBLEDUST.get())
                .define('T', Items.TNT)
                .unlockedBy(getHasName(ModItems.SKINBOOK.get()), has(ModItems.SKINBOOK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CASTSKELETONBOOK.get())
                .pattern("BHB")
                .pattern("DSD")
                .pattern("BBB")
                .define('B', Items.BONE)
                .define('H', Items.SKELETON_SKULL)
                .define('D', ModItems.INVISIBLEDUST.get())
                .define('S', ModItems.SKINBOOK.get())
                .unlockedBy(getHasName(ModItems.SKINBOOK.get()), has(ModItems.SKINBOOK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CASTZOMBIEBOOK.get())
                .pattern("RZR")
                .pattern("DSD")
                .pattern("RRR")
                .define('R', Items.ROTTEN_FLESH)
                .define('Z', Items.ZOMBIE_HEAD)
                .define('D', ModItems.INVISIBLEDUST.get())
                .define('S', ModItems.SKINBOOK.get())
                .unlockedBy(getHasName(ModItems.SKINBOOK.get()), has(ModItems.SKINBOOK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FLESHSTICK.get())
                .pattern(" V ")
                .pattern(" V ")
                .pattern(" V ")
                .define('V', Ingredient.of(ModTags.Items.HEARTS))
                .unlockedBy("has_a_heart", hasAny(ModTags.Items.HEARTS))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EVILDIAMONDMATTER.get())
                .pattern("AVA")
                .pattern("VDV")
                .pattern("AVA")
                .define('V', Ingredient.of(ModTags.Items.HEARTS))
                .define('D', Items.DIAMOND)
                .define('A', Items.ANCIENT_DEBRIS)
                .unlockedBy(getHasName(ModItems.VILLAGERHEART.get()), has(ModItems.VILLAGERHEART.get()))
                .save(pWriter, new ResourceLocation("youareherobrineresurrected", "evil_diamond_matter"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EVILDIAMONDMATTER.get())
                .pattern("VAV")
                .pattern("ADA")
                .pattern("VAV")
                .define('V', ModItems.VILLAGERHEART.get())
                .define('D', Items.DIAMOND)
                .define('A', Items.ANCIENT_DEBRIS)
                .unlockedBy("has_a_heart",
                        InventoryChangeTrigger.TriggerInstance.hasItems(
                                ItemPredicate.Builder.item()
                                        .of(ModTags.Items.HEARTS)
                                        .build()
                        ))
                .save(pWriter, new ResourceLocation("youareherobrineresurrected", "evil_diamond_matter_alt"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.EVILDIAMONDSWORD.get())
                .pattern(" E ")
                .pattern(" E ")
                .pattern(" F ")
                .define('E', ModItems.EVILDIAMOND.get())
                .define('F', ModItems.FLESHSTICK.get())
                .unlockedBy(getHasName(ModItems.EVILDIAMOND.get()), has(ModItems.EVILDIAMOND.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.EVILDIAMONDSHOVEL.get())
                .pattern(" E ")
                .pattern(" F ")
                .pattern(" F ")
                .define('E', ModItems.EVILDIAMOND.get())
                .define('F', ModItems.FLESHSTICK.get())
                .unlockedBy(getHasName(ModItems.EVILDIAMOND.get()), has(ModItems.EVILDIAMOND.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.EVILDIAMONDHOE.get())
                .pattern(" EE")
                .pattern(" F ")
                .pattern(" F ")
                .define('E', ModItems.EVILDIAMOND.get())
                .define('F', ModItems.FLESHSTICK.get())
                .unlockedBy(getHasName(ModItems.EVILDIAMOND.get()), has(ModItems.EVILDIAMOND.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.EVILDIAMONDAXE.get())
                .pattern("EE ")
                .pattern("EF ")
                .pattern(" F ")
                .define('E', ModItems.EVILDIAMOND.get())
                .define('F', ModItems.FLESHSTICK.get())
                .unlockedBy(getHasName(ModItems.EVILDIAMOND.get()), has(ModItems.EVILDIAMOND.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.EVILDIAMONDPICKAXE.get())
                .pattern("EEE")
                .pattern(" F ")
                .pattern(" F ")
                .define('E', ModItems.EVILDIAMOND.get())
                .define('F', ModItems.FLESHSTICK.get())
                .unlockedBy(getHasName(ModItems.EVILDIAMOND.get()), has(ModItems.EVILDIAMOND.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BEDROCKPIXAXE.get())
                .pattern("BBB")
                .pattern(" S ")
                .pattern(" S ")
                .define('B', Items.BEDROCK)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.BEDROCKPIXAXE.get()), has(ModItems.BEDROCKPIXAXE.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Items.BEDROCK)
                .pattern("EEE")
                .pattern("ESE")
                .pattern("EEE")
                .define('E', ModItems.EVILDIAMOND.get())
                .define('S', Items.STONE)
                .unlockedBy(getHasName(Items.BEDROCK), has(Items.BEDROCK))
                .save(pWriter);
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        Iterator var9 = pIngredients.iterator();

        for (ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult,
                            pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, YouAreHerobrineResurrected.MOD_ID + ":" + (pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
