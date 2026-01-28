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
        simpleItem(ModItems.CASTCREEPERBOOK);
        simpleItem(ModItems.CASTSKELETONBOOK);
        simpleItem(ModItems.CASTZOMBIEBOOK);
        simpleItem(ModItems.EVILDIAMONDMATTER);
        simpleItem(ModItems.EVILDIAMOND);
        simpleItem(ModItems.FLESHSTICK);
        simpleItem(ModItems.ICERUBY);
        simpleItem(ModItems.INVISIBLEDUST);
        simpleItem(ModItems.ILLAGERHEART);
        simpleItem(ModItems.NETHERPORTALITEM);
        simpleItem(ModItems.SKINBOOK);
        simpleItem(ModItems.SPELLBOOKI);
        simpleItem(ModItems.SPELLBOOKII);
        simpleItem(ModItems.SPELLBOOKIII);
        simpleItem(ModItems.SPELLBOOKIV);
        simpleItem(ModItems.SPELLBOOKV);
        simpleItem(ModItems.SPELLBOOKVI);
        simpleItem(ModItems.SPELLBOOKVII);
        simpleItem(ModItems.SPELLBOOKVIII);
        simpleItem(ModItems.STEVESOUL);
        simpleItem(ModItems.VILLAGERHEART);

        handHeldItemSpecifyTexture(ModItems.BEDROCKPIXAXE, "deliberately_missing_texture.png");
        handheldItem(ModItems.EVILDIAMONDAXE);
        handheldItem(ModItems.EVILDIAMONDHOE);
        handheldItem(ModItems.EVILDIAMONDPICKAXE);
        handheldItem(ModItems.EVILDIAMONDSHOVEL);
        handheldItem(ModItems.EVILDIAMONDSWORD);

        withExistingParent(ModItems.HUNTER_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.STEVE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SLAYER_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));

        // Utility items, not for gameplay

        simpleItem(ModItems.ADVANCEMENT_ROOT_ICON);

    }
    private ItemModelBuilder handHeldItemSpecifyTexture(RegistryObject<Item> item, String texture) {
        return withExistingParent(item.getId().getPath(), mcLoc("item/handheld"))
                .texture("layer0", modLoc(texture));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(YouAreHerobrineResurrected.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleItemSpecifyTexture(RegistryObject<Item> item, String texture) {
        return withExistingParent(item.getId().getPath(), mcLoc("item/handheld"))
                .texture("layer0", modLoc(texture));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(YouAreHerobrineResurrected.MOD_ID,"item/" + item.getId().getPath()));
    }
}
