package net.notccg.yahresurrected.entity.custom.boss;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.notccg.yahresurrected.sound.ModSounds;
import org.jetbrains.annotations.Nullable;

public class JebEntity extends PathfinderMob implements Enemy {
    private ServerBossEvent bossEvent;
    public JebEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.bossEvent = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setCreateWorldFog(true);
        this.setHealth(this.getMaxHealth());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 999)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected boolean canRide(Entity pVehicle) {
        return false;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.JEB_DEFEATED.get();
    }
}
