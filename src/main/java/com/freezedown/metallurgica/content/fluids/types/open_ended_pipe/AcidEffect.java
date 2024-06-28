package com.freezedown.metallurgica.content.fluids.types.open_ended_pipe;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.fluids.FluidDamageSource;
import com.freezedown.metallurgica.content.fluids.types.Acid;
import com.simibubi.create.content.fluids.OpenEndedPipe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class AcidEffect implements OpenEndedPipe.IEffectHandler {
    
    
    protected final Acid acidType;
    
    public AcidEffect(Acid acidType) {
        this.acidType = acidType;
    }
    
    @Override
    public boolean canApplyEffects(OpenEndedPipe pipe, FluidStack fluid) {
        return fluid.getFluid().isSame(acidType);
    }
    
    @Override
    public void applyEffects(OpenEndedPipe pipe, FluidStack fluid) {
        Level world = pipe.getWorld();
        if (world.getGameTime() % 5 != 0) return;
        Metallurgica.LOGGER.info("Applying acid effects from fluid: {}", Component.translatable(acidType.getFluidType().getDescriptionId()).getString());
        Metallurgica.LOGGER.info("{} has an acidity of {}", Component.translatable(acidType.getFluidType().getDescriptionId()).getString(), acidType.getAcidity());
        float acidity = acidType.getAcidity();
        if (acidType.isNeutral()) {
            neutralEffect(pipe, acidType, world, acidity);
        } else if (acidType.isBase()) {
            baseEffect(pipe, acidType, world, acidity);
        } else if (acidType.isAcid()) {
            acidEffect(pipe, acidType, world, acidity);
        }
    }
    
    private void neutralEffect(OpenEndedPipe pipe, Acid acid, Level level, float acidity) {
        List<Entity> entities = level.getEntities((Entity) null, pipe.getAOE(), entity -> !entity.fireImmune());
        for (Entity entity : entities) {
            entity.clearFire();
            BlockPos.betweenClosedStream(pipe.getAOE()).forEach(pos -> dowseFire(level, pos));
        }
    }
    
    private void baseEffect(OpenEndedPipe pipe, Acid acid, Level level, float acidity) {
        if (acid.dousesFire()) {
            List<Entity> entities = level.getEntities((Entity) null, pipe.getAOE(), entity -> !entity.fireImmune());
            for (Entity entity : entities) {
                entity.clearFire();
                BlockPos.betweenClosedStream(pipe.getAOE()).forEach(pos -> dowseFire(level, pos));
            }
        }
    }
    
    private void acidEffect(OpenEndedPipe pipe, Acid acid, Level level, float acidity) {
        Vec3 sourcePosition = new Vec3(pipe.getOutputPos().getX(), pipe.getOutputPos().getY(), pipe.getOutputPos().getZ());
        DamageSource acidDamage = new FluidDamageSource("acid", acid, sourcePosition).bypassMagic();
        int fireSeconds = acid.isAcid() ? (int) (7 - acidity) * 2 : 3;
        boolean causeBlindness = acidity < 3;
        List<LivingEntity> mobs = level.getEntitiesOfClass(LivingEntity.class, pipe.getAOE());
        for (LivingEntity mob : mobs) {
            if (causeBlindness) {
                MobEffectInstance blindness = new MobEffectInstance(MobEffects.BLINDNESS, 40, 4, false, false);
                mob.addEffect(blindness);
            }
            float hurtAmount = mob.fireImmune() ? 0.5F : 1.0F;
            mob.hurt(acidDamage, hurtAmount * fireSeconds);
        }
        BlockPos.betweenClosedStream(pipe.getAOE()).forEach(pos -> corrodeCopper(level, pos));
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
}
