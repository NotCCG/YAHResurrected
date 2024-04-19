package net.notccg.yahresurrected.entity.custom;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class SteveEntity extends Monster {

    protected SteveEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new PanicGoal(this, 2));
    }

    public static AttributeSupplier.Builder createAttribute() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20D);
    }
}


