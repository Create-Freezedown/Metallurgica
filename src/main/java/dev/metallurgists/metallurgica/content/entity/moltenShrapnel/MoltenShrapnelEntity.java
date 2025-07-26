package dev.metallurgists.metallurgica.content.entity.moltenShrapnel;


import dev.metallurgists.metallurgica.foundation.util.recipe.helper.TagItemOutput;
import dev.metallurgists.metallurgica.registry.MetallurgicaEntityTypes;
import dev.metallurgists.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.content.fluids.particle.FluidParticleData;
import com.simibubi.create.content.trains.CubeParticleData;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class MoltenShrapnelEntity extends ThrowableProjectile {
    private static final EntityDataAccessor<String> FLUID = SynchedEntityData.defineId(MoltenShrapnelEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> BUILT_UP_VELOCITY = SynchedEntityData.defineId(MoltenShrapnelEntity.class, EntityDataSerializers.FLOAT);

    public MoltenShrapnelEntity(EntityType<? extends MoltenShrapnelEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public MoltenShrapnelEntity(Level p_37399_, LivingEntity p_37400_) {
        super(MetallurgicaEntityTypes.MOLTEN_SHRAPNEL.get(), p_37400_, p_37399_);
    }

    public MoltenShrapnelEntity(Level p_37394_, double p_37395_, double p_37396_, double p_37397_) {
        super(MetallurgicaEntityTypes.MOLTEN_SHRAPNEL.get(), p_37395_, p_37396_, p_37397_, p_37394_);
    }

    public void setFluid(String fluid) {
        this.entityData.set(FLUID, fluid);
    }

    public void setFluid(ResourceLocation fluid) {
        this.setFluid(fluid.toString());
    }

    public void setBuiltUpVelocity(float builtUpVelocity) {
        this.entityData.set(BUILT_UP_VELOCITY, builtUpVelocity);
    }

    public String getFluid() {
        return this.entityData.get(FLUID);
    }

    public ResourceLocation getFluidRL() {
        return new ResourceLocation(this.getFluid());
    }

    public float getBuiltUpVelocity() {
        return this.entityData.get(BUILT_UP_VELOCITY);
    }

    public void incrementBuiltUpVelocity(float increment) {
        this.setBuiltUpVelocity(this.getBuiltUpVelocity() + increment);
    }

    protected float getGravity() {
        return 0.02F;
    }

    protected void defineSynchedData() {
        this.entityData.define(FLUID, "metallurgica:molten_iron");
        this.entityData.define(BUILT_UP_VELOCITY, 0.0F);
    }

    public void tick() {
        super.tick();
        if (getFluidRL() == null || getFluid() == null) {
            this.discard();
        }
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(getFluidRL());
        if (fluid == null) {
            this.discard();
            return;
        }
        String metalName = Objects.requireNonNull(ForgeRegistries.FLUID_TYPES.get().getKey(fluid.getFluidType())).getPath();
        if (!metalName.startsWith("molten_")) {
            this.discard();
        }
        metalName = metalName.substring(7);
        ItemStack nuggetOutput = TagItemOutput.fromTag(MetallurgicaTags.forgeItemTag("nuggets/" + metalName)).get();
        if (!nuggetOutput.isEmpty() && this.isInWaterRainOrBubble()) {
            if (this.tickCount < 20) {
                return;
            }
            this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), nuggetOutput));
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.FIRE_EXTINGUISH, this.getSoundSource(), 1.0F, 1.0F, false);
            this.discard();
        }
        if (this.level().isClientSide) {
            CubeParticleData data = new CubeParticleData(25, 46, 87, 0.0125F + 0.0625F * this.random.nextFloat(), 30, false);
            this.level().addParticle(data, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, -this.getDeltaMovement().y * 0.5, this.random.nextGaussian() * 0.05);
        }
        //If the shrapnel is going up, builtUpVelocity will reduce. If it's going down, it will increase.
        boolean goingUp = this.getDeltaMovement().y > 0;
        if (goingUp) {
            incrementBuiltUpVelocity(-0.05f);
        } else {
            incrementBuiltUpVelocity(0.05F);
        }
    }

    protected float getDamage() {
        float builtUpVelocity = this.getBuiltUpVelocity();
        if (builtUpVelocity < 0) {
            return 0.0F;
        }
        return 10.1F * builtUpVelocity;
    }

    private ParticleOptions getParticle() {
        return ParticleTypes.SMOKE;
    }

    public void handleEntityEvent(byte bytes) {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(getFluidRL());
        if (fluid == null) {
            return;
        }
        FluidStack fluidStack = new FluidStack(fluid, 1000);
        if (bytes == 3) {
            ParticleOptions particleoptions = new FluidParticleData(AllParticleTypes.FLUID_PARTICLE.get(), fluidStack);

            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity entity = result.getEntity();
            Entity entity1 = this.getOwner();
            int i = entity.getRemainingFireTicks();
            entity.setSecondsOnFire(10);
            //entity.hurt(DamageSources.generic(), getDamage());
        }
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        return builder.sized(0.25F, 0.25F);
    }
}
