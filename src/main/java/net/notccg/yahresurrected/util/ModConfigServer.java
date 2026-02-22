package net.notccg.yahresurrected.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfigServer {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> STEVE_FLEES_OR_APPROACHES_PLAYER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> STEVE_PICKS_UP_WANTED_ITEMS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> STEVE_SOUND_INVESTIGATION;
    public static final ForgeConfigSpec.ConfigValue<Boolean> STEVE_BLOCK_INVESTIGATION;
    public static final ForgeConfigSpec.ConfigValue<Boolean> STEVE_RUNS_FROM_CREEPERS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> STEVE_INSOMNIA;
    public static final ForgeConfigSpec.ConfigValue<Boolean> STEVE_LOOK_AT_HIT_DIRECTION;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_PHANTOMS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> JOIN_MESSAGE_ENABLE;

    static {
        BUILDER.push("YAH:R Sever Config");

        STEVE_FLEES_OR_APPROACHES_PLAYER = BUILDER.comment("Enable/Disable Steve fleeing or approaching the player (Default: true)")
                .define("Steve Player Flee Or Run", true);

        STEVE_PICKS_UP_WANTED_ITEMS = BUILDER.comment("Enable/Disable Steve tracking and picking up items he wants (Default: true)")
                .define("Steve picks up wanted items", true);

        STEVE_SOUND_INVESTIGATION = BUILDER.comment("Enable/Disable Steve investigating sounds he hears (Default: true)")
                .define("Steve investigates sounds", true);

        STEVE_BLOCK_INVESTIGATION = BUILDER.comment("Enable/Disable Steve investigating new blocks")
                .define("Steve investigates blocks", true);

        STEVE_RUNS_FROM_CREEPERS = BUILDER.comment("Enable/Disable Steve fleeing from creepers (Default: true)")
                .define("Steve creeper phobia", true);

        STEVE_INSOMNIA = BUILDER.comment("Enable/Disable Steve never going to sleep (Default: false)")
                .define("Steve Insomnia", true); // Temporarily true for debugging.

        STEVE_LOOK_AT_HIT_DIRECTION = BUILDER.comment("Enable/Disable Steve looking at the direction he was hit from (Default: true)")
                .define("Steve surroundings awareness", true);

        DISABLE_PHANTOMS = BUILDER.comment("Enable/Disable phantoms spawning (Default: true)")
                .define("Disable Phantoms", true);

        JOIN_MESSAGE_ENABLE = BUILDER.comment("Enable/Disable a join message when you load the world (Default: true)")
                .define("Join message enable", true);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
