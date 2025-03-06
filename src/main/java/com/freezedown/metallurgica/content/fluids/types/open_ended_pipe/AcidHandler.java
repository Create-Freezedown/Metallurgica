package com.freezedown.metallurgica.content.fluids.types.open_ended_pipe;

import com.freezedown.metallurgica.content.fluids.types.Acid;
import com.freezedown.metallurgica.registry.misc.MetallurgicaDamageSources;
import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class AcidHandler implements OpenPipeEffectHandler {

    public AcidHandler() {
    }
    
    private void neutralEffect(AABB area, Acid acid, Level level, float acidity) {
        List<Entity> entities = level.getEntities((Entity) null, area, entity -> !entity.fireImmune());
        for (Entity entity : entities) {
            entity.clearFire();
            BlockPos.betweenClosedStream(area).forEach(pos -> dowseFire(level, pos));
        }
    }
    
    private void baseEffect(AABB area, Acid acid, Level level, float acidity) {
        if (acid.dousesFire()) {
            List<Entity> entities = level.getEntities((Entity) null, area, entity -> !entity.fireImmune());
            for (Entity entity : entities) {
                entity.clearFire();
                BlockPos.betweenClosedStream(area).forEach(pos -> dowseFire(level, pos));
            }
        }
    }
    
    private void acidEffect(AABB area, Acid acid, Level level, float acidity) {
        int fireSeconds = acid.isAcid() ? (int) (7 - acidity) * 2 : 3;
        boolean causeBlindness = acidity < 3;
        List<LivingEntity> mobs = level.getEntitiesOfClass(LivingEntity.class, area);
        for (LivingEntity mob : mobs) {
            if (causeBlindness) {
                MobEffectInstance blindness = new MobEffectInstance(MobEffects.BLINDNESS, 40, 4, false, false);
                mob.addEffect(blindness);
            }
            float hurtAmount = mob.fireImmune() ? 0.5F : 1.0F;
            mob.hurt(MetallurgicaDamageSources.acidBurn(level, mob, acid), hurtAmount * fireSeconds);
        }
        BlockPos.betweenClosedStream(area).forEach(pos -> corrodeCopper(level, pos));
    }
    
    private static void dowseFire(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.is(BlockTags.FIRE)) {
            level.removeBlock(pos, false);
        } else if (AbstractCandleBlock.isLit(state)) {
            AbstractCandleBlock.extinguish(null, state, level, pos);
        } else if (CampfireBlock.isLitCampfire(state)) {
            level.levelEvent(null, 1009, pos, 0);
            CampfireBlock.dowse(null, level, pos, state);
            level.setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, false));
        }
    }
    
    private void corrodeCopper(Level level, BlockPos pos) {
        if (level.random.nextFloat() >= 0.057f) return;
        
        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();
        if (block instanceof WeatheringCopper weatheringCopper && WeatheringCopper.getNext(block).isPresent()) {
            weatheringCopper.getNext(blockState).ifPresent(state -> level.setBlockAndUpdate(pos, state));
        }
    }

    @Override
    public void apply(Level level, AABB area, FluidStack fluidStack) {
        if (level.getGameTime() % 5 != 0) return;
        Fluid fluid = fluidStack.getFluid();
        float acidity;
        if (fluid instanceof Acid acid) {
            acidity = acid.getAcidity();
            if (acid.isNeutral()) {
                neutralEffect(area, acid, level, acidity);
            } else if (acid.isBase()) {
                baseEffect(area, acid, level, acidity);
            } else if (acid.isAcid()) {
                acidEffect(area, acid, level, acidity);
            }
        }

    }
}
