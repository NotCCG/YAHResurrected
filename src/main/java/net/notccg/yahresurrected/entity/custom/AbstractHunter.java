package net.notccg.yahresurrected.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class AbstractHunter extends Monster implements RangedAttackMob {

    //Default Hunter Mob Goal Configuration

    private final RangedBowAttackGoal<AbstractHunter> bowGoal = new RangedBowAttackGoal(this, 1.0, 20, 15.0F);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2, false) {
        public void stop() {
            super.stop();
            AbstractHunter.this.setAggressive(false);
        }

        public void start() {
            super.start();
            AbstractHunter.this.setAggressive(true);
        }
    };

    public AbstractHunter(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.reassessWeaponGoal();
    }


    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.PLAYER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal(this, Creeper.class, 6.0F, 1.0, 1.2));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this,
                Monster.class,
                true,
                (living) -> (living != null) && (living.getClass() != Hunter.class) && (living.getClass() != Slayer.class)));
    }

    public static AttributeSupplier.Builder createAttribute() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        RandomSource randomsource = pLevel.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        this.reassessWeaponGoal();
        this.setCanPickUpLoot(randomsource.nextFloat() < 0.55F * pDifficulty.getSpecialMultiplier());
        return pSpawnData;
    }


    public void reassessWeaponGoal() {
        if (this.level() != null && !this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, (item) -> item instanceof BowItem));
            if (itemstack.is(Items.BOW)) {
                int i = 20;
                if (this.level().getDifficulty() != Difficulty.HARD) {
                    i = 40;
                }

                this.bowGoal.setMinAttackInterval(i);
                this.goalSelector.addGoal(2, this.bowGoal);
            } else {
                this.goalSelector.addGoal(2, this.meleeGoal);
            }
        }

    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float pDistanceFactor) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, (item) -> item instanceof BowItem)));
        AbstractArrow abstractarrow = this.getArrow(itemstack, pDistanceFactor);
        if (this.getMainHandItem().getItem() instanceof BowItem) {
            abstractarrow = ((BowItem)this.getMainHandItem().getItem()).customArrow(abstractarrow);
        }

        double d0 = livingEntity.getX() - this.getX();
        double d1 = livingEntity.getY(0.3333333333333333) - abstractarrow.getY();
        double d2 = livingEntity.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractarrow.shoot(d0, d1 + d3 * 0.20000000298023224, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractarrow);
    }

    protected AbstractArrow getArrow(ItemStack pArrowStack, float pVelocity) {
        return ProjectileUtil.getMobArrow(this, pArrowStack, pVelocity);
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem pProjectileWeapon) {
        return pProjectileWeapon == Items.BOW;
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.reassessWeaponGoal();
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
        super.setItemSlot(pSlot, pStack);
        if (!this.level().isClientSide) {
            this.reassessWeaponGoal();
        }

    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 1.74F;
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return new ResourceLocation("youareherobrineresurrected", "entities/hunter_entity");
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pRandom, pDifficulty);
        float difficultyModifier = (this.level().getDifficulty() == Difficulty.HARD ? 0.11F : 0.01F);

        if (pRandom.nextFloat() < 0.74 + difficultyModifier) {
            rollArmorSlots(pRandom, pickArmorEquipmentTier(pRandom));
        }

        if (pRandom.nextFloat() < 0.24F + difficultyModifier) enchantArmorSlot(pRandom, pickArmorEquipmentTier(pRandom), EquipmentSlot.HEAD);
        if (pRandom.nextFloat() < 0.24F + difficultyModifier) enchantArmorSlot(pRandom, pickArmorEquipmentTier(pRandom), EquipmentSlot.CHEST);
        if (pRandom.nextFloat() < 0.24F + difficultyModifier) enchantArmorSlot(pRandom, pickArmorEquipmentTier(pRandom), EquipmentSlot.LEGS);
        if (pRandom.nextFloat() < 0.24F + difficultyModifier) enchantArmorSlot(pRandom, pickArmorEquipmentTier(pRandom), EquipmentSlot.FEET);
    }

    private int pickArmorEquipmentTier(RandomSource pRandom) {
        float difficultyModifier = (this.level().getDifficulty() == Difficulty.HARD ? 0.08F : 0.01F);

        float roll = pRandom.nextFloat() - difficultyModifier;

        if (roll < 0.05F) return 4;
        if (roll < 0.15F) return 3;
        if (roll < 0.35F) return 2;
        if (roll < 0.55F) return 1;
        return 0;
    }

    private void rollArmorSlots(RandomSource pRandom, int pEquipmentTier) {
        float difficultyModifier = (this.level().getDifficulty() == Difficulty.HARD ? 0.08F : 0.01F);

        float slotChance = switch (pEquipmentTier) {
            case 4 -> 0.72F + difficultyModifier;
            case 3 -> 0.52F + difficultyModifier;
            case 2 -> 0.32F + difficultyModifier;
            case 1 -> 0.12F + difficultyModifier;
            default -> 0.02F + difficultyModifier;
        };

        Item helmet = switch (pEquipmentTier) {
            case 4 -> Items.NETHERITE_HELMET;
            case 3 -> Items.DIAMOND_HELMET;
            case 2 -> Items.IRON_HELMET;
            case 1 -> Items.CHAINMAIL_HELMET;
            default -> Items.LEATHER_HELMET;
        };

        Item chestplate = switch (pEquipmentTier) {
            case 4 -> Items.NETHERITE_CHESTPLATE;
            case 3 -> Items.DIAMOND_CHESTPLATE;
            case 2 -> Items.IRON_CHESTPLATE;
            case 1 -> Items.CHAINMAIL_CHESTPLATE;
            default -> Items.LEATHER_CHESTPLATE;
        };

        Item leggings = switch (pEquipmentTier) {
            case 4 -> Items.NETHERITE_LEGGINGS;
            case 3 -> Items.DIAMOND_LEGGINGS;
            case 2 -> Items.IRON_LEGGINGS;
            case 1 -> Items.CHAINMAIL_LEGGINGS;
            default -> Items.LEATHER_LEGGINGS;
        };

        Item boots = switch (pEquipmentTier) {
            case 4 -> Items.NETHERITE_BOOTS;
            case 3 -> Items.DIAMOND_BOOTS;
            case 2 -> Items.IRON_BOOTS;
            case 1 -> Items.CHAINMAIL_BOOTS;
            default -> Items.LEATHER_BOOTS;
        };

        if (pRandom.nextFloat() < slotChance) {
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(helmet));
            this.setDropChance(EquipmentSlot.HEAD, 0.02f);
        }
        if (pRandom.nextFloat() < slotChance) {
            this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(chestplate));
            this.setDropChance(EquipmentSlot.CHEST, 0.02f);
        }
        if (pRandom.nextFloat() < slotChance) {
            this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(leggings));
            this.setDropChance(EquipmentSlot.LEGS, 0.02f);
        }
        if (pRandom.nextFloat() < slotChance) {
            this.setItemSlot(EquipmentSlot.FEET, new ItemStack(boots));
            this.setDropChance(EquipmentSlot.FEET, 0.02f);
        }
    }

    private void enchantArmorSlot(RandomSource pRandom, int pEnchantmentTier, EquipmentSlot pEquipmentSlot) {
        ItemStack stack = this.getItemBySlot(pEquipmentSlot);
        if (stack.isEmpty()) return;

        int difficultyModifier = (this.level().getDifficulty() == Difficulty.HARD ? 3 : 1);

        int tierEnchantLevel = switch (pEnchantmentTier) {
            case 4 -> 27 + difficultyModifier;
            case 3 -> 21 + difficultyModifier;
            case 2 -> 19 + difficultyModifier;
            case 1 -> 7 + difficultyModifier;
            default -> difficultyModifier;
        };
        ItemStack enchantedItemStack = EnchantmentHelper.enchantItem(pRandom, stack, tierEnchantLevel, false);
        this.setItemSlot(pEquipmentSlot, enchantedItemStack);
        this.setDropChance(pEquipmentSlot, 0.01F);
    }
}
