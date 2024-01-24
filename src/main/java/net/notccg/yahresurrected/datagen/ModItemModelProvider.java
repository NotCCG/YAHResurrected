package net.notccg.yahresurrected.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.notccg.yahresurrected.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, YouAreHerobrineResurrected.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.VILLAGERHEART);
        simpleItem(ModItems.SKINBOOK);
        simpleItem(ModItems.STEVESOUL);
        simpleItem(ModItems.INVISIBLEDUST);
        simpleItem(ModItems.ICERUBY);
        simpleItem(ModItems.EVILDIAMONDMATTER);
        simpleItem(ModItems.EVILDIAMOND);
        simpleItem(ModItems.FLESHSTICK);
        simpleItem(ModItems.SPELLBOOKI);
        simpleItem(ModItems.SPELLBOOKII);
        simpleItem(ModItems.SPELLBOOKIII);
        simpleItem(ModItems.SPELLBOOKIV);
        simpleItem(ModItems.SPELLBOOKV);

        handheldItem(ModItems.BEDROCKPIXAXE);
        handheldItem(ModItems.EVILDIAMONDAXE);
        handheldItem(ModItems.EVILDIAMONDPICKAXE);
        handheldItem(ModItems.EVILDIAMONDSHOVEL);
        handheldItem(ModItems.EVILDIAMONDHOE);
        handheldItem(ModItems.EVILDIAMONDSWORD);

    }
    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(YouAreHerobrineResurrected.MOD_ID,"item/" + item.getId().getPath()));
    }
}
