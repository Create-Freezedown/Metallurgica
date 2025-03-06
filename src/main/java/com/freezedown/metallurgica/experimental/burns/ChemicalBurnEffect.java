package com.freezedown.metallurgica.experimental.burns;


import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.freezedown.metallurgica.registry.MetallurgicaEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ChemicalBurnEffect extends MobEffect {
    //public static final DamageSource CHEMICAL_BURN = (new DamageSource("chemicalBurn")).bypassArmor().bypassEnchantments().bypassMagic().setScalesWithDifficulty();
    public ChemicalBurnEffect(int pColor) {
        super(MobEffectCategory.HARMFUL, pColor);
    }
//    public float healthReduction = MetallurgicaConfigs.common().experiments.chemicalBurns.chemicalBurnHealthReduction.getF(); //FSR THIS CRASHES IN LOADING SO I USE THE UGLY WAY

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
//        if (pLivingEntity.getHealth() > pLivingEntity.getMaxHealth() - pAmplifier * MetallurgicaConfigs.common().experiments.chemicalBurns.chemicalBurnHealthReduction.getF()) {
//            pLivingEntity.setHealth(pLivingEntity.getMaxHealth() - pAmplifier * MetallurgicaConfigs.common().experiments.chemicalBurns.chemicalBurnHealthReduction.getF());
//        }
            pLivingEntity.getAttributes().getInstance(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("CHEMICAL_BURN_HEALTH_MODIFIER", 0 - MetallurgicaConfigs.common().experiments.chemicalBurns.chemicalBurnHealthReduction.getF(), AttributeModifier.Operation.ADDITION));
        if (pLivingEntity.hasEffect(MobEffects.REGENERATION)){
            pLivingEntity.removeEffect(MetallurgicaEffects.CHEMICAL_BURN_EFFECT.get());
        }
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}

