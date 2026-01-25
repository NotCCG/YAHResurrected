package net.notccg.yahresurrected.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.notccg.yahresurrected.util.ModStructures;
import net.notccg.yahresurrected.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModStructureTagGenerator extends StructureTagsProvider {
    public ModStructureTagGenerator(PackOutput p_256522_, CompletableFuture<HolderLookup.Provider> p_256661_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_256522_, p_256661_, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ModTags.Structures.STEVE_HOUSES)
                .add(ModStructures.STEVE_HOUSE_PLAINS);
    }
}
