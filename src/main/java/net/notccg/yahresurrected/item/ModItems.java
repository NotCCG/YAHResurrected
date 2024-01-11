package net.notccg.yahresurrected.item;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.notccg.yahresurrected.item.custom.*;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, YouAreHerobrineResurrected.MOD_ID);

    public static final RegistryObject<Item> VILLAGERHEART = ITEMS.register("villager_heart",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STEVESOUL = ITEMS.register("steve_soul",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SKINBOOK = ITEMS.register("skin_book",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FLESHSTICK = ITEMS.register("flesh_stick",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ICERUBY = ITEMS.register("ice_ruby",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EVILDIAMOND = ITEMS.register("evil_diamond",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EVILDIAMONDMATTER = ITEMS.register("evil_diamond_matter",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SPELLBOOKI = ITEMS.register("spell_book_i",
            () -> new SpellBookOneItem(new Item.Properties()));

    public static final RegistryObject<Item> SPELLBOOKII = ITEMS.register("spell_book_ii",
            () -> new SpellBookTwoItem(new Item.Properties()));

    public static final RegistryObject<Item> SPELLBOOKIII = ITEMS.register("spell_book_iii",
            () -> new SpellBookTheeItem(new Item.Properties()));

    public static final RegistryObject<Item> SPELLBOOKIV = ITEMS.register("spell_book_iv",
            () -> new SpellBookFourItem(new Item.Properties()));

    public static final RegistryObject<Item> SPELLBOOKV = ITEMS.register("spell_book_v",
            () -> new SpellBookFiveItem(new Item.Properties()));

    public static final RegistryObject<Item> INVISIBLEDUST = ITEMS.register("invisible_dust",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EVILDIAMONDPICKAXE = ITEMS.register("evil_diamond_pickaxe",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EVILDIAMONDAXE = ITEMS.register("evil_diamond_axe",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EVILDIAMONDHOE = ITEMS.register("evil_diamond_hoe",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EVILDIAMONDSHOVEL = ITEMS.register("evil_diamond_shovel",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EVILDIAMONDSWORD = ITEMS.register("evil_diamond_sword",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
