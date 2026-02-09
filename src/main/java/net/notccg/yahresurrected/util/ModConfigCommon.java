package net.notccg.yahresurrected.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfigCommon {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> OVERRIDE_SKIN_CONFIG;

    static {
        BUILDER.push("YAH:R Config");

        OVERRIDE_SKIN_CONFIG = BUILDER.comment("Force Herobrine Skin").define("Skin Override", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
