package net.notccg.yahresurrected.util.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfigCommon {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Boolean> WRATH_OF_JEB;

    static {
        BUILDER.push("YAH:R Common Config");
        WRATH_OF_JEB = BUILDER.comment("Enable/Disable the W̶̧̤͗Ṙ̸̝̋̿̒A̵̳̗͗͒̏͝T̵̖̗̙̜̤̈́̆͋H̶̡̠̻̭͊͗̇͗͝ ̵̢̫͍͎͆O̵̫̔F̴̛̲̋̃̈́͘ ̵̛̯͇̙͔̺̐̀̕J̶̞̇E̶̖̔̓B̵̮̎̚ (Default: F̷̱̣̞̈́̆A̷͎̼͍͗̓́̏ͅL̸̰͝S̷͓͔͓̬̬͘E̵͓̋̇̄̿͘").define("W̶̧̤͗Ṙ̸̝̋̿̒A̵̳̗͗͒̏͝T̵̖̗̙̜̤̈́̆͋H̶̡̠̻̭͊͗̇͗͝ ̵̢̫͍͎͆O̵̫̔F̴̛̲̋̃̈́͘ ̵̛̯͇̙͔̺̐̀̕J̶̞̇E̶̖̔̓B̵̮̎̚", false);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
