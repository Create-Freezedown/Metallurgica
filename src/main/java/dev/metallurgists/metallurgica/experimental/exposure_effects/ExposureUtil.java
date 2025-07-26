package dev.metallurgists.metallurgica.experimental.exposure_effects;

import dev.metallurgists.metallurgica.foundation.config.MetallurgicaConfigs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class ExposureUtil {
    
    public static float getDigSpeed(LivingEntity pEntity) {
        if (pEntity.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            return 0.3f;
        }
        return 1;
    }
    
    public static boolean searchForExposer(LivingEntity player, ExposureMinerals mineral) {
        AABB aabb = player.getBoundingBox().inflate(getRange(mineral));
        for (BlockState state : player.level().getBlockStates(aabb).toList()) {
            if (state.is(mineral.getBlockTag())) {
                return true;
            }
        }
        for (ItemEntity itemEntity : player.level().getEntitiesOfClass(ItemEntity.class, aabb)) {
            if (itemEntity.getItem().is(mineral.getItemTag())) {
                return true;
            }
        }
        if (player instanceof Player pPlayer) {
            for (ItemStack stack : pPlayer.getInventory().items) {
                if (stack.is(mineral.getItemTag())) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public static int getExposureTimer(LivingEntity player, ExposureMinerals mineral) {
        CompoundTag tag = player.getPersistentData();
        if (!tag.contains(mineral.getTimerName())) {
            tag.putInt(mineral.getTimerName(), 0);
        }
        return tag.getInt(mineral.getTimerName());
    }
    
    public static boolean incrementExposureTimer(LivingEntity player, ExposureMinerals mineral) {
        CompoundTag tag = player.getPersistentData();
        if (!tag.contains(mineral.getTimerName())) {
            tag.putInt(mineral.getTimerName(), 0);
        }
        int timer = tag.getInt(mineral.getTimerName());
        if (timer < getStageFourMin(mineral)) {
            tag.putInt(mineral.getTimerName(), timer + 1);
            return false;
        }
        return true;
    }
    
    public static float getRange(ExposureMinerals mineral) {
        return switch (mineral) {
            case LEAD -> MetallurgicaConfigs.common().experiments.leadExposure.leadExposureRange.getF();
        };
    }
    
    public static float getStageOneMin(ExposureMinerals mineral) {
        return switch (mineral) {
            case LEAD -> MetallurgicaConfigs.common().experiments.leadExposure.stageOneMin.get();
        };
    }
    
    public static float getStageTwoMin(ExposureMinerals mineral) {
        return switch (mineral) {
            case LEAD -> MetallurgicaConfigs.common().experiments.leadExposure.stageTwoMin.get();
        };
    }
    
    public static float getStageThreeMin(ExposureMinerals mineral) {
        return switch (mineral) {
            case LEAD -> MetallurgicaConfigs.common().experiments.leadExposure.stageThreeMin.get();
        };
    }
    
    public static float getStageFourMin(ExposureMinerals mineral) {
        return switch (mineral) {
            case LEAD -> MetallurgicaConfigs.common().experiments.leadExposure.stageFourMin.get();
        };
    }
    
    public static boolean isEnabled(ExposureMinerals mineral) {
        return switch (mineral) {
            case LEAD -> MetallurgicaConfigs.common().experiments.leadExposure.enableLeadExposure.get();
        };
    }
    
    public static MobEffectInstance getEffect(ExposureMinerals mineral, int stage) {
        return switch (mineral) {
            case LEAD -> new MobEffectInstance(MobEffects.POISON, 1, stage - 1, true, false, false);
        };
    }
}
