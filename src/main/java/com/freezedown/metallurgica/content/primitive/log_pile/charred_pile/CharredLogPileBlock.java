package com.freezedown.metallurgica.content.primitive.log_pile.charred_pile;

import com.freezedown.metallurgica.content.primitive.log_pile.LogPileBlock;
import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CharredLogPileBlock extends LogPileBlock implements IBE<CharredLogPileBlockEntity> {
    public CharredLogPileBlock(Properties pProperties) {
        super(pProperties);
    }
    
    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
        int layers = pState.getValue(LAYERS);
        float damageReduction = 0.25F * layers  * 0.75F;
        pEntity.causeFallDamage(pFallDistance, damageReduction, pLevel.damageSources().fall());
        if (pFallDistance > pEntity.getMaxFallDistance()) {
            if (layers > 1) {
                pLevel.setBlock(pPos, pState.setValue(LAYERS, layers-1), 3);
            } else {
                pLevel.destroyBlock(pPos, false);
            }
        }
    }
    
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextInt(24) == 0) {
            pLevel.playLocalSound((double) pPos.getX() + 0.5, (double) pPos.getY() + 0.5, (double) pPos.getZ() + 0.5, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + pRandom.nextFloat(), pRandom.nextFloat() * 0.7F + 0.3F, false);
        }
        
        int j1;
        double d7;
        double d12;
        double d17;
        for(j1 = 0; j1 < 3; ++j1) {
            d7 = (double)pPos.getX() + pRandom.nextDouble();
            d12 = (double)pPos.getY() + pRandom.nextDouble() * 0.5 + 1;
            d17 = (double)pPos.getZ() + pRandom.nextDouble();
            pLevel.addParticle(ParticleTypes.LARGE_SMOKE, d7, d12, d17, 0.0, 0.0, 0.0);
        }
    }
    
    @Override
    public Class<CharredLogPileBlockEntity> getBlockEntityClass() {
        return CharredLogPileBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends CharredLogPileBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.charredLogPile.get();
    }
}
