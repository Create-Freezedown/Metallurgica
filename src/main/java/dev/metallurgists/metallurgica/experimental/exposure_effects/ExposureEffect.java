package dev.metallurgists.metallurgica.experimental.exposure_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class ExposureEffect extends MobEffect {
    
    protected ExposureEffect(int pColor) {
        super(MobEffectCategory.HARMFUL, pColor);
    }
    
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
    
    public void applyEffectTick(LivingEntity entity, int amp) {
        switch (amp) {
            case 0:
                stageOneEffect(entity);
                break;
            case 1:
                stageOneEffect(entity);
                stageTwoEffect(entity);
                break;
            case 2:
                stageOneEffect(entity);
                stageTwoEffect(entity);
                stageThreeEffect(entity);
                break;
            case 3:
                stageOneEffect(entity);
                stageTwoEffect(entity);
                stageThreeEffect(entity);
                stageFourEffect(entity);
                break;
        }
    }
    
    public void stageOneEffect(LivingEntity entity) {
    }
    
    public void stageTwoEffect(LivingEntity entity) {
    
    }
    
    public void stageThreeEffect(LivingEntity entity) {
    
    }
    
    public void stageFourEffect(LivingEntity entity) {
    
    }
    
}
