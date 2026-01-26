package net.notccg.yahresurrected.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.entity.ModEntities;
import net.notccg.yahresurrected.item.ModItems;
import net.notccg.yahresurrected.util.ModStructures;
import net.notccg.yahresurrected.util.ModTags;

import java.util.function.Consumer;

public class ModAdvancements implements ForgeAdvancementProvider.AdvancementGenerator {

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement root = Advancement.Builder.advancement()
                .display(
                        ModItems.ADVANCEMENT_ROOT_ICON.get(),
                        Component.translatable(langTitle("root")),
                        Component.translatable(langDescription("root")),
                        new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
                        FrameType.TASK,
                        false,
                        false,
                        false
                )
                .addCriterion("impossible", new ImpossibleTrigger.TriggerInstance())
                .save(saver, id("root"));

        Advancement shrine_located = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        Items.SOUL_CAMPFIRE,
                        Component.translatable(langTitle("where_it_starts")),
                        Component.translatable(langDescription("where_it_starts")),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        true
                )
                .addCriterion("shrine_located",
                        locationDiscovered(ModStructures.SHRINE)
                )
                .save(saver, id("shrine_located"));

        Advancement sunburn = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        Items.COOKED_BEEF,
                        Component.translatable(langTitle("sunburn")),
                        Component.translatable(langDescription("sunburn")),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("sunburn_trigger", new ImpossibleTrigger.TriggerInstance())
                .save(saver, id("sunburn"));

        Advancement soul_obtained = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        ModItems.STEVESOUL.get(),
                        Component.translatable(langTitle("soul_harvest")),
                        Component.translatable(langDescription("soul_harvest")),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("Killed_steve",
                        KilledTrigger.TriggerInstance.playerKilledEntity(
                                EntityPredicate.Builder
                                        .entity().of(ModEntities.STEVE.get())
                        )
                )
                .addCriterion("got_steve_soul",
                        hasItem(ModItems.STEVESOUL.get())
                )
                .save(saver, id("soul_obtained"));

        Advancement ice_ruby_get = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        ModItems.ICERUBY.get(),
                        Component.translatable(langTitle("absolute_zero")),
                        Component.translatable(langDescription("absolute_zero")),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("get_ice_ruby",
                        hasItem(ModItems.ICERUBY.get())
                )
                .rewards(AdvancementRewards.Builder.experience(15))
                .save(saver, id("ice_ruby_get"));

        Advancement invisible_dust_get = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        ModItems.SKINBOOK.get(),
                        Component.translatable(langTitle("keen_eye")),
                        Component.translatable(langDescription("keen_eye")),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("picked_up_invisible_dust",
                        hasItem(ModItems.INVISIBLEDUST.get())
                )
                .rewards(AdvancementRewards.Builder.experience(50))
                .save(saver, id("invisible_dust_get"));

        Advancement redstone_torch_get = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        Items.REDSTONE_TORCH,
                        Component.translatable(langTitle("redstone_fascination")),
                        Component.translatable(langDescription("redstone_fascination")),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("made_redstone_torch",
                        hasItem(Items.REDSTONE_TORCH)
                )
                .save(saver, id("redstone_torch_get"));

        Advancement skin_book = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        ModItems.SKINBOOK.get(),
                        Component.translatable(langTitle("squishy")),
                        Component.translatable(langDescription("squishy")),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_skin_book",
                        hasItem(ModItems.SKINBOOK.get())
                )
                .rewards(AdvancementRewards.Builder.experience(20))
                .save(saver, id("skin_book"));

        Advancement spell_book_get = Advancement.Builder.advancement()
                .parent(skin_book)
                .display(
                        ModItems.SPELLBOOKI.get(),
                        Component.translatable(langTitle("my_first_spell")),
                        Component.translatable(langDescription("my_first_spell")),
                        null,
                        FrameType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("has_spell_book",
                        hasItemWithTag(ModTags.Items.SPELL_BOOKS)
                )
                .rewards(AdvancementRewards.Builder.experience(25))
                .save(saver, id("spell_book_get"));


        Advancement sparkle = Advancement.Builder.advancement()
                .parent(sunburn)
                .display(
                        ModItems.SPELLBOOKII.get(),
                        Component.translatable(langTitle("sparkle")),
                        Component.translatable(langDescription("sparkle")),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("has_spellbook_ii",
                        hasItem(ModItems.SPELLBOOKII.get())
                )
                .rewards(AdvancementRewards.Builder.experience(250))
                .save(saver, id("has_speellbook_ii"));

        Advancement john_cena = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        ModItems.SPELLBOOKI.get(),
                        Component.translatable(langTitle("john_cena")),
                        Component.translatable(langDescription("john_cena")),
                        null,
                        FrameType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("has_spell_book_i",
                        hasItem(ModItems.SPELLBOOKI.get())
                )
                .rewards(AdvancementRewards.Builder.experience(20))
                .save(saver, id("has_spell_book_i"));


        Advancement villager_heart = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        ModItems.VILLAGERHEART.get(),
                        Component.translatable(langTitle("you_monster")),
                        Component.translatable(langDescription("you_monster")),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("killed_villager",
                        KilledTrigger.TriggerInstance.playerKilledEntity(
                                EntityPredicate.Builder.entity()
                                        .of(EntityType.VILLAGER).build()
                        )
                )
                .addCriterion("picked_up_heart",
                        hasItem(ModItems.VILLAGERHEART.get())
                )
                .rewards(AdvancementRewards.Builder.experience(10))
                .save(saver, id("villager_heart"));

        Advancement flesh_stick = Advancement.Builder.advancement()
                .parent(villager_heart)
                .display(
                        ModItems.FLESHSTICK.get(),
                        Component.translatable(langTitle("pulsates")),
                        Component.translatable(langDescription("pulsates")),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("crafted_flesh_stick",
                        hasItem(ModItems.FLESHSTICK.get())
                )
                .rewards(AdvancementRewards.Builder.experience(25))
                .save(saver, id("flesh_stick"));

        Advancement evil_diamond_matter = Advancement.Builder.advancement()
                .parent(villager_heart)
                .display(
                        ModItems.EVILDIAMONDMATTER.get(),
                        Component.translatable(langTitle("dark_aura")),
                        Component.translatable(langDescription("dark_aura")),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("crafted_evil_diamond_matter",
                        hasItem(ModItems.EVILDIAMONDMATTER.get())
                )
                .rewards(AdvancementRewards.Builder.experience(50))
                .save(saver, id("evil_diamond_matter"));

        Advancement evil_diamond = Advancement.Builder.advancement()
                .parent(evil_diamond_matter)
                .display(
                        ModItems.EVILDIAMOND.get(),
                        Component.translatable(langTitle("pure_evil")),
                        Component.translatable(langDescription("pure_evil")),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("smelted_evil_diamond",
                        hasItem(ModItems.EVILDIAMOND.get())
                )
                .rewards(AdvancementRewards.Builder.experience(120))
                .save(saver, id("evil_diamond"));

        Advancement evil_pickaxe = Advancement.Builder.advancement()
                .parent(evil_diamond)
                .display(
                        ModItems.EVILDIAMONDPICKAXE.get(),
                        Component.translatable(langTitle("evil_pickaxe")),
                        Component.translatable(langDescription("evil_pickaxe")),
                        null,
                        FrameType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("crafted_evil_diamond_pickaxe",
                        hasItem(ModItems.EVILDIAMONDPICKAXE.get())
                )
                .rewards(AdvancementRewards.Builder.experience(250))
                .save(saver, id("evil_pickaxe"));

        Advancement evil_sword = Advancement.Builder.advancement()
                .parent(evil_diamond)
                .display(
                        ModItems.EVILDIAMONDSWORD.get(),
                        Component.translatable(langTitle("evil_sword")),
                        Component.translatable(langDescription("evil_sword")),
                        null,
                        FrameType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("craft_evil_diamond_sword",
                        hasItem(ModItems.EVILDIAMONDSWORD.get())
                )
                .rewards(AdvancementRewards.Builder.experience(250))
                .save(saver, id("evil_sword"));

        Advancement evil_tools_collection = Advancement.Builder.advancement()
                .parent(evil_pickaxe)
                .display(
                        ModItems.EVILDIAMONDHOE.get(),
                        Component.translatable(langTitle("evil_tools_collection")),
                        Component.translatable(langDescription("evil_tools_collection")),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("get_evil_diamond_pickaxe",
                        hasItem(ModItems.EVILDIAMONDPICKAXE.get())
                )
                .addCriterion("get_evil_diamond_sword",
                        hasItem(ModItems.EVILDIAMONDSWORD.get())
                )
                .addCriterion("get_evil_diamond_axe",
                        hasItem(ModItems.EVILDIAMONDAXE.get())
                )
                .addCriterion("get_evil_diamond_shovel",
                        hasItem(ModItems.EVILDIAMONDSHOVEL.get())
                )
                .addCriterion("get_evil_diamond_hoe",
                        hasItem(ModItems.EVILDIAMONDHOE.get())
                )
                .rewards(AdvancementRewards.Builder.experience(500))
                .save(saver, id("evil_tools_collection"));

        Advancement bedrock_get = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        Items.BEDROCK,
                        Component.translatable(langTitle("bedrock_get")),
                        Component.translatable(langDescription("bedrock_get")),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("craft_bedrock",
                        hasItem(Items.BEDROCK)
                )
                .rewards(AdvancementRewards.Builder.experience(1000))
                .save(saver, id("bedrock_get"));

        Advancement nether_portal_get = Advancement.Builder.advancement()
                .parent(bedrock_get)
                .display(
                        ModItems.NETHERPORTALITEM.get(),
                        Component.translatable(langTitle("illegal_item_hell_portal")),
                        Component.translatable(langDescription("illegal_item_hell_portal")),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("get_nether_portal_block",
                        hasItem(ModItems.NETHERPORTALITEM.get())
                )
                .rewards(AdvancementRewards.Builder.experience(666))
                .save(saver, id("nether_portal_get"));

        Advancement monster_spawner_get = Advancement.Builder.advancement()
                .parent(bedrock_get)
                .display(
                        Items.SPAWNER,
                        Component.translatable(langTitle("illegal_item_spawner")),
                        Component.translatable(langDescription("illegal_item_spawner")),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .rewards(AdvancementRewards.Builder.experience(420))
                .save(saver, id("monster_spawner_get"));

        Advancement bedrock_pickaxe_get = Advancement.Builder.advancement()
                .parent(bedrock_get)
                .display(
                        ModItems.BEDROCKPIXAXE.get(),
                        Component.translatable(langTitle("bedrock_pickaxe_get")),
                        Component.translatable(langDescription("bedrock_pickaxe_get")),
                        null,
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true
                )
                .addCriterion("bedrock_pickaxe_get",
                        hasItem(ModItems.BEDROCKPIXAXE.get())
                )
                .rewards(AdvancementRewards.Builder.experience(9999))
                .save(saver, id("bedrock_pickaxe_get"));
    }

    private static String id(String path) {
        return YouAreHerobrineResurrected.MOD_ID + ":" + path;
    }
    private static String langTitle(String name) {
        return "advancements." + YouAreHerobrineResurrected.MOD_ID + "." + name + ".title";
    }
    private static String langDescription(String name) {
        return "advancements." + YouAreHerobrineResurrected.MOD_ID + "." + name + ".description";
    }

    private static InventoryChangeTrigger.TriggerInstance hasItem(Item item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(
                ItemPredicate.Builder.item().of(item).build()
        );
    }

    private static InventoryChangeTrigger.TriggerInstance hasItemWithTag(TagKey<Item> tagKey) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(
                ItemPredicate.Builder.item().of(tagKey).build()
        );
    }

    private static PlayerTrigger.TriggerInstance locationDiscovered(ResourceKey<Structure> pStructure) {
        return PlayerTrigger.TriggerInstance.located(
                LocationPredicate.Builder.location().setStructure(pStructure).build()
        );
    }

    @Override
    public AdvancementSubProvider toSubProvider(ExistingFileHelper existingFileHelper) {
        return ForgeAdvancementProvider.AdvancementGenerator.super.toSubProvider(existingFileHelper);
    }
}
