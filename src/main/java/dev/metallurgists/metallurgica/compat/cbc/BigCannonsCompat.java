package dev.metallurgists.metallurgica.compat.cbc;

import dev.metallurgists.metallurgica.foundation.util.MMods;
//import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.DefaultFluidCompat;
//import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobBurst;
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
    
    
    //public static void acidHitEntity(FluidBlobEffectRegistry.OnHitEntity.Context context) {
    //    LivingEntity entity = (LivingEntity) context.result().getEntity();
    //    Acid acid = (Acid) context.fstack().fluid();
    //    float acidity = acid.getAcidity();
    //    DamageSource acidDamage = new FluidDamageSource("acid", acid, context.result().getLocation()).bypassMagic();
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
    
    //public static void acidHitBlock(FluidBlobEffectRegistry.OnHitBlock.Context context) {
    //    Acid acid = (Acid) context.fstack().fluid();
    //    BlockPos pos = context.result().getBlockPos().relative(context.result().getDirection());
    //
    //    if (acid.isAcid()) {
    //        if (!context.level().isClientSide)
    //            corrodeCopper(pos, context.burst(), context.level());
    //    } else {
    //        if (!context.level().isClientSide)
    //            dowseFire(pos, context.burst(), context.level());
    //    }
    //}
    
    //private static void dowseFire(BlockPos root, FluidBlobBurst blob, Level level) {
    //    float chance = FluidBlobBurst.getBlockAffectChance();
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
    
    //public static void corrodeCopper(BlockPos root, FluidBlobBurst blob, Level level) {
    //    float chance = FluidBlobBurst.getBlockAffectChance();
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
