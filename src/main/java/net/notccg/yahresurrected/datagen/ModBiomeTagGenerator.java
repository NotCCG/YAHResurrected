package net.notccg.yahresurrected.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.notccg.yahresurrected.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagGenerator extends BiomeTagsProvider {

    public ModBiomeTagGenerator(PackOutput p_255800_, CompletableFuture<HolderLookup.Provider> p_256205_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_255800_, p_256205_, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ModTags.Biomes.ICE_RUBY_BIOMES)
                .add(Biomes.DEEP_DARK)
                .add(Biomes.DRIPSTONE_CAVES)
                .add(Biomes.FROZEN_RIVER)
                .add(Biomes.FROZEN_PEAKS)
                .add(Biomes.GROVE)
                .add(Biomes.ICE_SPIKES)
                .add(Biomes.JAGGED_PEAKS)
                .add(Biomes.SNOWY_BEACH)
                .add(Biomes.SNOWY_PLAINS)
                .add(Biomes.SNOWY_TAIGA)
                .add(Biomes.SNOWY_SLOPES);
    }
}
