package net.notccg.yahresurrected.entity.custom.boss;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class JebEntity extends PathfinderMob implements Enemy {
    public JebEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


}
