package com.freezedown.metallurgica.content.machines.reverbaratory;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class CremationDamageSource extends DamageSource {
    protected final Vec3 sourcePosition;
    
    public CremationDamageSource(String pMessageId, Vec3 pSourcePosition) {
        super(pMessageId);
        this.sourcePosition = pSourcePosition;
    }
    
    public Component getLocalizedDeathMessage(LivingEntity pLivingEntity) {
        return Component.translatable("death.reverbaratory." + this.msgId, pLivingEntity.getDisplayName());
    }
    
    @Nullable
    public Vec3 getSourcePosition() {
        return sourcePosition;
    }
    
    public String toString() {
        return "CremationDamageSource";
    }
}
