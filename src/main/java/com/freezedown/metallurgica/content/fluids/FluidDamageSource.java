package com.freezedown.metallurgica.content.fluids;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class FluidDamageSource extends DamageSource {
    protected final Fluid fluid;
    protected final Vec3 sourcePosition;
    
    public FluidDamageSource(String pMessageId, Fluid pFluid, Vec3 pSourcePosition) {
        super(pMessageId);
        this.fluid = pFluid;
        this.sourcePosition = pSourcePosition;
    }
    
    public Component getLocalizedDeathMessage(LivingEntity pLivingEntity) {
        return Component.translatable("death.fluid_attack." + this.msgId, pLivingEntity.getDisplayName(), Component.translatable(fluid.getFluidType().getDescriptionId()));
    }
    
    @Nullable
    public Vec3 getSourcePosition() {
        return sourcePosition;
    }
    
    public String toString() {
        return "FluidDamageSource (" + this.fluid + ")";
    }
}
