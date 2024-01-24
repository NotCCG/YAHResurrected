package net.notccg.yahresurrected.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, YouAreHerobrineResurrected.MOD_ID);
    }

    @Override
    protected void start() {

    }
}
