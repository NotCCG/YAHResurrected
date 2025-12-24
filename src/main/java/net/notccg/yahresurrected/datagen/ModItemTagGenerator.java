package net.notccg.yahresurrected.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.notccg.yahresurrected.entity.custom.logic.steve_ai.SteveLogic;
import net.notccg.yahresurrected.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput packOutput,
                               CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> lookupCompletableFuture,
                               String modId,
                               @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, lookupCompletableFuture, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ModTags.Items.STEVE_LOVED)
                .add(Items.ANCIENT_DEBRIS)
                .add(Items.BEACON)
                .add(Items.DEEPSLATE_DIAMOND_ORE)
                .add(Items.DIAMOND_BLOCK)
                .add(Items.DIAMOND)
                .add(Items.DIAMOND_AXE)
                .add(Items.DIAMOND_BOOTS)
                .add(Items.DIAMOND_CHESTPLATE)
                .add(Items.DIAMOND_BLOCK)
                .add(Items.DIAMOND_HELMET)
                .add(Items.DIAMOND_HOE)
                .add(Items.DIAMOND_HORSE_ARMOR)
                .add(Items.DIAMOND_LEGGINGS)
                .add(Items.DIAMOND_ORE)
                .add(Items.DIAMOND_PICKAXE)
                .add(Items.DIAMOND_SHOVEL)
                .add(Items.DIAMOND_SWORD)
                .add(Items.ENCHANTED_BOOK)
                .add(Items.NETHER_STAR)
                .add(Items.NETHERITE_AXE)
                .add(Items.NETHERITE_BOOTS)
                .add(Items.NETHERITE_BLOCK)
                .add(Items.NETHERITE_CHESTPLATE)
                .add(Items.NETHERITE_HELMET)
                .add(Items.NETHERITE_HOE)
                .add(Items.NETHERITE_INGOT)
                .add(Items.NETHERITE_LEGGINGS)
                .add(Items.NETHERITE_PICKAXE)
                .add(Items.NETHERITE_SCRAP)
                .add(Items.NETHERITE_SHOVEL)
                .add(Items.NETHERITE_SWORD)
                .add(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
    }
}
