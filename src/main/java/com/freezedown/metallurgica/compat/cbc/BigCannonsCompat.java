package com.freezedown.metallurgica.compat.cbc;

import com.freezedown.metallurgica.content.fluids.FluidDamageSource;
import com.freezedown.metallurgica.content.fluids.types.Acid;
import com.freezedown.metallurgica.foundation.util.MMods;
import com.freezedown.metallurgica.registry.MetallurgicaFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
//import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.EndFluidStack;
//import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlob;
//import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobEffectRegistry;

public class BigCannonsCompat {
    
    public static void register() {
        MMods.CREATEBIGCANNONS.executeIfInstalled(() -> BigCannonsCompat::registerWithDependency);
    }
    
    private static void registerWithDependency() {
        //FluidBlobEffectRegistry.registerHitEntity(MetallurgicaFluids.hydrochloricAcid.get(), BigCannonsCompat::acidHitEntity);
        //FluidBlobEffectRegistry.registerHitBlock(MetallurgicaFluids.hydrochloricAcid.get(), BigCannonsCompat::acidHitBlock);
        //FluidBlobEffectRegistry.registerFluidShellExplosionEffect(MetallurgicaFluids.hydrochloricAcid.get(), DefaultFluidCompat::waterFluidShellExplode);
        
        //FluidBlobEffectRegistry.registerHitEntity(MetallurgicaFluids.sulfuricAcid.get(), BigCannonsCompat::acidHitEntity);
        //FluidBlobEffectRegistry.registerHitBlock(MetallurgicaFluids.sulfuricAcid.get(), BigCannonsCompat::acidHitBlock);
        //FluidBlobEffectRegistry.registerFluidShellExplosionEffect(MetallurgicaFluids.sulfuricAcid.get(), DefaultFluidCompat::waterFluidShellExplode);
        
        //FluidBlobEffectRegistry.registerHitEntity(MetallurgicaFluids.sodiumHydroxide.get(), BigCannonsCompat::acidHitEntity);
        //FluidBlobEffectRegistry.registerHitBlock(MetallurgicaFluids.sodiumHydroxide.get(), BigCannonsCompat::acidHitBlock);
        //FluidBlobEffectRegistry.registerFluidShellExplosionEffect(MetallurgicaFluids.sodiumHydroxide.get(), DefaultFluidCompat::waterFluidShellExplode);
        
        //FluidBlobEffectRegistry.registerHitEntity(MetallurgicaFluids.sodiumHypochlorite.get(), BigCannonsCompat::acidHitEntity);
        //FluidBlobEffectRegistry.registerHitBlock(MetallurgicaFluids.sodiumHypochlorite.get(), BigCannonsCompat::acidHitBlock);
        //FluidBlobEffectRegistry.registerFluidShellExplosionEffect(MetallurgicaFluids.sodiumHypochlorite.get(), DefaultFluidCompat::waterFluidShellExplode);
    }
    
    
    //public static void acidHitEntity(EndFluidStack fstack, FluidBlob blob, Level level, EntityHitResult result) {
    //    LivingEntity entity = (LivingEntity) result.getEntity();
    //    Acid acid = (Acid) fstack.fluid();
    //    float acidity = acid.getAcidity();
    //    DamageSource acidDamage = new FluidDamageSource("acid", acid, result.getLocation()).bypassMagic();
    //    int fireSeconds = acid.isAcid() ? (int) (7 - acidity) * 2 : 3;
    //    boolean causeBlindness = acidity < 3;
    //    if (acid.isAcid()) {
    //        if (causeBlindness) {
    //            MobEffectInstance blindness = new MobEffectInstance(MobEffects.BLINDNESS, 40, 4, false, false);
    //            entity.addEffect(blindness);
    //        }
    //        float hurtAmount = entity.fireImmune() ? 0.5F : 1.0F;
    //        if (entity.hurt(acidDamage, hurtAmount * fireSeconds))
    //            entity.playSound(SoundEvents.FIRE_EXTINGUISH, 0.4F, 2.0F + entity.level.random.nextFloat() * 0.4F);
    //    }
    //}
    
    //public static void acidHitBlock(EndFluidStack fstack, FluidBlob blob, Level level, BlockHitResult result) {
    //    Acid acid = (Acid) fstack.fluid();
    //    BlockPos pos = result.getBlockPos().relative(result.getDirection());
    //
    //    if (acid.isAcid()) {
    //        if (!level.isClientSide)
    //            corrodeCopper(pos, blob, level);
    //    } else {
    //        if (!level.isClientSide)
    //            dowseFire(pos, blob, level);
    //    }
    //}
    
    //private static void dowseFire(BlockPos root, FluidBlob blob, Level level) {
    //    float chance = FluidBlob.getBlockAffectChance();
    //    if (chance == 0)
    //        return;
    //    AABB bounds = blob.getAreaOfEffect(root);
    //    BlockPos pos1 = new BlockPos(Math.floor(bounds.minX), Math.floor(bounds.minY), Math.floor(bounds.minZ));
    //    BlockPos pos2 = new BlockPos(Math.floor(bounds.maxX), Math.floor(bounds.maxY), Math.floor(bounds.maxZ));
    //    for (BlockPos pos : BlockPos.betweenClosed(pos1, pos2)) {
    //        if (level.getRandom().nextFloat() > chance)
    //            continue;
    //        BlockState state = level.getBlockState(pos);
    //        Block block = state.getBlock();
    //        if (block instanceof BaseFireBlock) {
    //            level.removeBlock(pos, false);
    //        } else if (AbstractCandleBlock.isLit(state)) {
    //            AbstractCandleBlock.extinguish(null, state, level, pos);
    //        } else if (CampfireBlock.isLitCampfire(state)) {
    //            level.levelEvent(null, 1009, pos, 0);
    //            CampfireBlock.dowse(null, level, pos, state);
    //            level.setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, false));
    //        }
    //    }
    //}
    
    //public static void corrodeCopper(BlockPos root, FluidBlob blob, Level level) {
    //    float chance = FluidBlob.getBlockAffectChance();
    //    if (chance == 0)
    //        return;
    //    AABB bounds = blob.getAreaOfEffect(root);
    //    BlockPos pos1 = new BlockPos(Math.floor(bounds.minX), Math.floor(bounds.minY), Math.floor(bounds.minZ));
    //    BlockPos pos2 = new BlockPos(Math.floor(bounds.maxX), Math.floor(bounds.maxY), Math.floor(bounds.maxZ));
    //    for (BlockPos pos : BlockPos.betweenClosed(pos1, pos2)) {
    //        if (level.getRandom().nextFloat() > chance)
    //            continue;
    //        BlockState state = level.getBlockState(pos);
    //        Block block = state.getBlock();
    //        if (block instanceof WeatheringCopper weatheringCopper && WeatheringCopper.getNext(block).isPresent()) {
    //            weatheringCopper.getNext(state).ifPresent(newState -> level.setBlockAndUpdate(pos, newState));
    //        }
    //    }
    //}
}
