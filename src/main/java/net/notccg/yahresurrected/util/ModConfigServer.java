package net.notccg.yahresurrected.util;

import net.minecraftforge.common.ForgeConfigSpec;

import java.security.PublicKey;

public class ModConfigServer {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> STEVE_LOOKS_AT_PLAYER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> STEVE_FLEES_OR_APPROACHES_PLAYER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> STEVE_RUNS_FROM_CREEPERS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> STEVE_INSOMNIA;
    public static final ForgeConfigSpec.ConfigValue<Boolean> STEVE_LOOK_AT_HIT_DIRECTION;

    static {
        BUILDER.push("YAH:R Sever Config");

        STEVE_LOOKS_AT_PLAYER = BUILDER.comment("Enable/Disble Steve looking at the player (Default: true)")
                        .define("Steve Player Tracking", true);

        STEVE_FLEES_OR_APPROACHES_PLAYER = BUILDER.comment("Enable/Disble Steve fleeing or approaching the player (Default: true)")
                        .define("Steve Player Flee Or Run", true);

        STEVE_RUNS_FROM_CREEPERS = BUILDER.comment("Enable/Disable Steve fleeing from creepers (Default: true)")
                        .define("Steve creeper phobia", false); // Temporarily false for debugging.

        STEVE_INSOMNIA = BUILDER.comment("Enable/Disable Steve never going to sleep (Default: false)")
                        .define("Steve Insomnia", true); // Temporarily true for debugging.

        STEVE_LOOK_AT_HIT_DIRECTION = BUILDER.comment("Enable/Disable Steve looking at the direction he was hit from (Default: true)")
                        .define("Steve German main", false);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
