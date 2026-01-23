package net.notccg.yahresurrected.datagen;

import net.minecraft.advancements.CriteriaTriggers;
import net.notccg.yahresurrected.event.triggers.SunburnTrigger;

public final class ModCriteriaTriggers {
    private ModCriteriaTriggers() {}

    public static final SunburnTrigger SUNBURN = new SunburnTrigger();

    public static void register() {
        CriteriaTriggers.register(SUNBURN);
    }
}
