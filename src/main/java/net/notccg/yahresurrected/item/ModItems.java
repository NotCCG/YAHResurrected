package net.notccg.yahresurrected.item;

import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.notccg.yahresurrected.YouAreHerobrineResurrected;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.notccg.yahresurrected.entity.ModEntities;
import net.notccg.yahresurrected.item.custom.*;
import net.notccg.yahresurrected.item.custom.util.AdvancementRootIconItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, YouAreHerobrineResurrected.MOD_ID);

    public static final RegistryObject<Item> VILLAGERHEART = ITEMS.register("villager_heart",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> ILLAGERHEART = ITEMS.register("illager_heart",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> STEVESOUL = ITEMS.register("steve_soul",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> SKINBOOK = ITEMS.register("skin_book",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FLESHSTICK = ITEMS.register("flesh_stick",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ICERUBY = ITEMS.register("ice_ruby",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EVILDIAMOND = ITEMS.register("evil_diamond",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> EVILDIAMONDMATTER = ITEMS.register("evil_diamond_matter",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> SPELLBOOKI = ITEMS.register("spell_book_i",
            () -> new SpellBookOneItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1)));

    public static final RegistryObject<Item> SPELLBOOKII = ITEMS.register("spell_book_ii",
            () -> new SpellBookTwoItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1)));

    public static final RegistryObject<Item> SPELLBOOKIII = ITEMS.register("spell_book_iii",
            () -> new SpellBookTheeItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1)));

    public static final RegistryObject<Item> SPELLBOOKIV = ITEMS.register("spell_book_iv",
            () -> new SpellBookFourItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> SPELLBOOKV = ITEMS.register("spell_book_v",
            () -> new SpellBookFiveItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> SPELLBOOKVI = ITEMS.register("spell_book_vi",
            () -> new SpellBookSixItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> SPELLBOOKVII = ITEMS.register("spell_book_vii",
            () -> new SpellBookSevenItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> SPELLBOOKVIII = ITEMS.register("spell_book_viii",
            () -> new SpellBookEightItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> CASTCREEPERBOOK = ITEMS.register("casting_book_creeper",
            () -> new CastCreeperBookItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1).durability(32)));

    public static final RegistryObject<Item> CASTSKELETONBOOK = ITEMS.register("casting_book_skeleton",
            () -> new CastSkeletonBookItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1).durability(64)));

    public static final RegistryObject<Item> CASTZOMBIEBOOK = ITEMS.register("casting_book_zombie",
            () -> new CastZombieBookItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1).durability(64)));

    public static final RegistryObject<Item> INVISIBLEDUST = ITEMS.register("invisible_dust",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EVILDIAMONDPICKAXE = ITEMS.register("evil_diamond_pickaxe",
            () -> new PickaxeItem(ModToolTiers.EVIL_DIAMOND, 0, 0,
                    new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> EVILDIAMONDAXE = ITEMS.register("evil_diamond_axe",
            () -> new AxeItem(ModToolTiers.EVIL_DIAMOND, 0, 0,
                    new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> EVILDIAMONDHOE = ITEMS.register("evil_diamond_hoe",
            () -> new HoeItem(ModToolTiers.EVIL_DIAMOND, 0, 0,
                    new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> EVILDIAMONDSHOVEL = ITEMS.register("evil_diamond_shovel",
            () -> new ShovelItem(ModToolTiers.EVIL_DIAMOND, 0, 0,
                    new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> EVILDIAMONDSWORD = ITEMS.register("evil_diamond_sword",
            () -> new SwordItem(ModToolTiers.EVIL_DIAMOND, 25, 4,
                    new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> BEDROCKPIXAXE = ITEMS.register("bedrock_pickaxe",
            () -> new BedrockPickaxeItem(ModToolTiers.BEDROCK, 20, 0,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> STEVE_SPAWN_EGG = ITEMS.register("steve_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.STEVE, 0x5C69FF, 0xDDBF7D,
                    new Item.Properties()));

    public static final RegistryObject<Item> HUNTER_SPAWN_EGG = ITEMS.register("hunter_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.HUNTER, 0x454545, 0xD3A46F,
                    new Item.Properties()));

    public static final RegistryObject<Item> SLAYER_SPAWN_EGG = ITEMS.register("slayer_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.SLAYER, 0x454545, 0xABD36F,
                    new Item.Properties()));


    // Utility items, not for use in gameplay.

    public static final RegistryObject<Item> ADVANCEMENT_ROOT_ICON = ITEMS.register("advancement_root_icon",
            () -> new AdvancementRootIconItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
