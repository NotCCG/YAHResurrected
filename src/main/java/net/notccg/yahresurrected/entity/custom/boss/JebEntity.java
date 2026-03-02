package net.notccg.yahresurrected.entity.custom.boss;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class JebEntity extends Mob implements Enemy {
    protected JebEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
