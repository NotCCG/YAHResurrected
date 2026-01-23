package net.notccg.yahresurrected.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.item.ModItems;

import java.util.function.Consumer;

public class ModAdvancements implements ForgeAdvancementProvider.AdvancementGenerator {
    private static String id(String path) {
        return YouAreHerobrineResurrected.MOD_ID + ":" +path;
    }

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement root = Advancement.Builder.advancement()
                .display(
                        ModItems.ADVANCEMENT_ROOT_ICON.get(),
                        Component.translatable("advancements." + YouAreHerobrineResurrected.MOD_ID + ".root.title"),
                        Component.translatable("advancements." + YouAreHerobrineResurrected.MOD_ID + "root.description"),
                        new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
                        FrameType.TASK,
                        false,
                        false,
                        false
                )
                .addCriterion("impossible", new ImpossibleTrigger.TriggerInstance())
                .save(saver, id("root"));

        Advancement sunburn = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        Items.COOKED_BEEF,
                        Component.translatable("advancements." + YouAreHerobrineResurrected.MOD_ID + ".sunburn.title"),
                        Component.translatable("advancements." + YouAreHerobrineResurrected.MOD_ID + ".sunburn.description"),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("sunburn", ModCriteriaTriggers.SUNBURN.instance())
                .save(saver, id("sunburn"));

        Advancement spellbook_two = Advancement.Builder.advancement()
                .parent(sunburn)
                .display(
                        ModItems.SPELLBOOKVII.get(),
                        Component.translatable("advancements." + YouAreHerobrineResurrected.MOD_ID + ".spellbook_two.title"),
                        Component.translatable("advancements." + YouAreHerobrineResurrected.MOD_ID + ".spellbook_two.description"),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("has_spellbook_ii",
                        InventoryChangeTrigger.TriggerInstance.hasItems(
                                ItemPredicate.Builder.item().of(ModItems.SPELLBOOKII.get()).build()
                        )
                )
                .rewards(AdvancementRewards.Builder.experience(250))
                .save(saver, id("has_speellbook_ii"));
    }

    @Override
    public AdvancementSubProvider toSubProvider(ExistingFileHelper existingFileHelper) {
        return ForgeAdvancementProvider.AdvancementGenerator.super.toSubProvider(existingFileHelper);
    }
}
