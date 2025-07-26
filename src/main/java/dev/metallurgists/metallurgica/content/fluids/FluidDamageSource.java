package dev.metallurgists.metallurgica.content.fluids;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;

public class FluidDamageSource extends DamageSource {
    protected final Fluid fluid;

    public FluidDamageSource(Holder<DamageType> type, Fluid pFluid, @Nullable Entity entity) {
        super(type, entity);
        this.fluid = pFluid;
    }
    
    public Component getLocalizedDeathMessage(LivingEntity pLivingEntity) {
        return Component.translatable("death.fluid." + this.type().msgId(), pLivingEntity.getDisplayName(), Component.translatable(fluid.getFluidType().getDescriptionId()));
    }
    
    public String toString() {
        return "FluidDamageSource (" + this.fluid + ")";
    }
}
