package net.notccg.yahresurrected.entity.client.steve;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Mob;

public class AbstractSteveModel<T extends Mob> extends HumanoidModel<T> {
    public AbstractSteveModel(ModelPart pRoot) {
        super(pRoot);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        AnimationUtils.bobArms(this.rightArm, this.leftArm, pAgeInTicks);
    }
}
