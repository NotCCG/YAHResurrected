package net.notccg.yahresurrected.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.notccg.yahresurrected.block.ModBlocks;
import net.notccg.yahresurrected.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.INVISIBLE_ORE.get(),
                        ModBlocks.DEEPSLATE_INVISIBLE_ORE.get());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.ICE_RUBY_ORE.get(),
                        ModBlocks.DEEPSLATE_ICE_RUBY_ORE.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.INVISIBLE_ORE.get(),
                        ModBlocks.DEEPSLATE_INVISIBLE_ORE.get(),
                        ModBlocks.ICE_RUBY_ORE.get(),
                        ModBlocks.DEEPSLATE_ICE_RUBY_ORE.get());

        this.tag(ModTags.Blocks.CONTAINER_BLOCK)
                .add(Blocks.BARREL,
                        Blocks.CHEST,
                        Blocks.ENDER_CHEST,
                        Blocks.SHULKER_BOX,
                        Blocks.TRAPPED_CHEST);

        this.tag(ModTags.Blocks.NECESSARY_BLOCKS)
                .add(Blocks.END_PORTAL_FRAME)
                .add(Blocks.END_PORTAL)
                .add(Blocks.END_GATEWAY)
                .add(Blocks.DRAGON_EGG);

        this.tag(ModTags.Blocks.ILLEGAL_BLOCK_ITEMS)
                .add(Blocks.NETHER_PORTAL)
                .add(Blocks.SPAWNER)

        this.tag(ModTags.Blocks.NEEDS_BEDROCK_TOOL);

        this.tag(ModTags.Blocks.NEEDS_EVIL_DIAMOND_TOOL);
    }
}
