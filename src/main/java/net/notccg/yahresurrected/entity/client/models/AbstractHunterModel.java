package net.notccg.yahresurrected.entity.client.models;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;

public class AbstractHunterModel<T extends Mob & RangedAttackMob> extends HumanoidModel {
    public AbstractHunterModel(ModelPart pRoot) {
        super(pRoot);
    }
}
