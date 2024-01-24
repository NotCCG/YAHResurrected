package net.notccg.yahresurrected.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.item.ModItems;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> EVIL_DIAMONDS = List.of(ModItems.EVILDIAMONDMATTER.get());
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        oreBlasting(pWriter, EVIL_DIAMONDS, RecipeCategory.MISC, ModItems.EVILDIAMOND.get(), 1f, 100, "evil_diamond");
        oreSmelting(pWriter, EVIL_DIAMONDS, RecipeCategory.MISC, ModItems.EVILDIAMOND.get(), 1f, 100, "evil_diamond");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SKINBOOK.get())
                .pattern(" PL")
                .pattern("LPL")
                .pattern("LP ")
                .define('P', Items.PAPER)
                .define('L', Items.LEATHER)
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SPELLBOOKI.get())
                .pattern("   ")
                .pattern("SBD")
                .pattern("   ")
                .define('S', ModItems.STEVESOUL.get())
                .define('B', ModItems.SKINBOOK.get())
                .define('D', ModItems.INVISIBLEDUST.get())
                .unlockedBy(getHasName(ModItems.SKINBOOK.get()), has(ModItems.SKINBOOK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SPELLBOOKII.get())
                .pattern(" I ")
                .pattern("SBD")
                .pattern("   ")
                .define('S', ModItems.STEVESOUL.get())
                .define('B', ModItems.SKINBOOK.get())
                .define('D', ModItems.INVISIBLEDUST.get())
                .define('I', ModItems.ICERUBY.get())
                .unlockedBy(getHasName(ModItems.SKINBOOK.get()), has(ModItems.SKINBOOK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SPELLBOOKIII.get())
                .pattern(" P ")
                .pattern("SBD")
                .pattern("   ")
                .define('S', ModItems.STEVESOUL.get())
                .define('B', ModItems.SKINBOOK.get())
                .define('D', ModItems.INVISIBLEDUST.get())
                .define('P', Items.BLAZE_POWDER)
                .unlockedBy(getHasName(ModItems.SKINBOOK.get()), has(ModItems.SKINBOOK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SPELLBOOKIV.get())
                .pattern(" E ")
                .pattern("SBD")
                .pattern("   ")
                .define('S', ModItems.STEVESOUL.get())
                .define('B', ModItems.SKINBOOK.get())
                .define('D', ModItems.INVISIBLEDUST.get())
                .define('E', Items.ENDER_EYE)
                .unlockedBy(getHasName(ModItems.SKINBOOK.get()), has(ModItems.SKINBOOK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SPELLBOOKV.get())
                .pattern(" F ")
                .pattern("SBD")
                .pattern("   ")
                .define('S', ModItems.STEVESOUL.get())
                .define('B', ModItems.SKINBOOK.get())
                .define('D', ModItems.INVISIBLEDUST.get())
                .define('F', Items.FEATHER)
                .unlockedBy(getHasName(ModItems.SKINBOOK.get()), has(ModItems.SKINBOOK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FLESHSTICK.get())
                .pattern(" V ")
                .pattern(" V ")
                .pattern(" V ")
                .define('V', ModItems.VILLAGERHEART.get())
                .unlockedBy(getHasName(ModItems.VILLAGERHEART.get()), has(ModItems.VILLAGERHEART.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EVILDIAMONDMATTER.get())
                .pattern("VVV")
                .pattern("VDV")
                .pattern("VVV")
                .define('V', ModItems.VILLAGERHEART.get())
                .define('D', Items.DIAMOND)
                .unlockedBy(getHasName(ModItems.VILLAGERHEART.get()), has(ModItems.VILLAGERHEART.get()))
                .save(pWriter);

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
                .unlockedBy(getHasName(Items.BEDROCK), has(Items.BEDROCK))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Items.BEDROCK)
                .pattern("HHH")
                .pattern("HSH")
                .pattern("HHH")
                .define('H', ModItems.VILLAGERHEART.get())
                .define('S', Items.STONE)
                .unlockedBy(getHasName(ModItems.VILLAGERHEART.get()), has(ModItems.VILLAGERHEART.get()))
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
