package com.freezedown.metallurgica.content.fluids.molten_metal.base;

import com.freezedown.metallurgica.Metallurgica;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import javax.annotation.Nullable;
import java.util.Optional;

public class MoltenMetal extends ForgeFlowingFluid {
    public MoltenMetal(Properties properties) {
        super(properties);
    }
    
    @Override
    public boolean isSource(FluidState fluidState) {
        return true;
    }
    
    @Override
    public int getAmount(FluidState fluidState) {
        return 8;
    }
    
    private static boolean hasFlammableNeighbours(LevelReader pLevel, BlockPos pPos) {
        Direction[] var3 = Direction.values();
        int var4 = var3.length;
        
        for(int var5 = 0; var5 < var4; ++var5) {
            Direction direction = var3[var5];
            if (isFlammable(pLevel, pPos.relative(direction), direction.getOpposite())) {
                return true;
            }
        }
        
        return false;
    }
    
    private static boolean isFlammable(LevelReader level, BlockPos pos, Direction face) {
        return (pos.getY() < level.getMinBuildHeight() || pos.getY() >= level.getMaxBuildHeight() || level.hasChunkAt(pos)) && level.getBlockState(pos).isFlammable(level, pos, face);
    }
    
    private static void fizz(LevelAccessor pLevel, BlockPos pPos) {
        pLevel.levelEvent(1501, pPos, 0);
    }
    
    
    public static class MoltenMetalFluidType extends FluidType {
        public final ResourceLocation still;
        public final ResourceLocation flow;
        
        public MoltenMetalFluidType(Properties properties, ResourceLocation still, ResourceLocation flow) {
            super(properties.canSwim(false).canDrown(false).pathType(BlockPathTypes.LAVA).adjacentPathType(null).lightLevel(15).density(3000).viscosity(6000).temperature(1300));
            this.still = still;
            this.flow = flow;
        }
        
        public void setItemMovement(ItemEntity entity) {
            Vec3 vec3 = entity.getDeltaMovement();
            entity.setDeltaMovement(vec3.x * 0.949999988079071, vec3.y + (double)(vec3.y < 0.05999999865889549 ? 5.0E-4F : 0.0F), vec3.z * 0.949999988079071);
        }
        
        public double motionScale(Entity entity) {
            return entity.level.dimensionType().ultraWarm() ? 0.007 : 0.0023333333333333335;
        }
        
        public boolean canPushEntity(Entity entity) {
            return true;
        }
    }
    public static class Source extends ForgeFlowingFluid {
        public Source(Properties properties) {
            super(properties);
        }
        
        public int getAmount(FluidState state) {
            return 8;
        }
        
        public boolean isSource(FluidState state) {
            return true;
        }
        
        public void animateTick(Level pLevel, BlockPos pPos, FluidState pState, RandomSource pRandom) {
            BlockPos blockpos = pPos.above();
            if (pLevel.getBlockState(blockpos).isAir() && !pLevel.getBlockState(blockpos).isSolidRender(pLevel, blockpos)) {
                if (pRandom.nextInt(100) == 0) {
                    double d0 = (double)pPos.getX() + pRandom.nextDouble();
                    double d1 = (double)pPos.getY() + 1.0;
                    double d2 = (double)pPos.getZ() + pRandom.nextDouble();
                    pLevel.addParticle(ParticleTypes.LAVA, d0, d1, d2, 0.0, 0.0, 0.0);
                    pLevel.playLocalSound(d0, d1, d2, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + pRandom.nextFloat() * 0.2F, 0.9F + pRandom.nextFloat() * 0.15F, false);
                }
                
                if (pRandom.nextInt(200) == 0) {
                    pLevel.playLocalSound((double)pPos.getX(), (double)pPos.getY(), (double)pPos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + pRandom.nextFloat() * 0.2F, 0.9F + pRandom.nextFloat() * 0.15F, false);
                }
            }
            
        }
        
        protected boolean canConvertToSource() {
            return false;
        }
        
        protected boolean isRandomlyTicking() {
            return true;
        }
        
        protected float getExplosionResistance() {
            return 100.0F;
        }
        
        public Optional<SoundEvent> getPickupSound() {
            return Optional.of(SoundEvents.BUCKET_FILL_LAVA);
        }
        
        @Nullable
        public ParticleOptions getDripParticle() {
            return ParticleTypes.DRIPPING_LAVA;
        }
        
        protected void beforeDestroyingBlock(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
            fizz(pLevel, pPos);
        }
        
        public int getSlopeFindDistance(LevelReader pLevel) {
            return pLevel.dimensionType().ultraWarm() ? 4 : 2;
        }
        
        public int getDropOff(LevelReader pLevel) {
            return pLevel.dimensionType().ultraWarm() ? 1 : 2;
        }
        
        public int getTickDelay(LevelReader pLevel) {
            return pLevel.dimensionType().ultraWarm() ? 10 : 30;
        }
        
        public int getSpreadDelay(Level pLevel, BlockPos pPos, FluidState p_76205_, FluidState p_76206_) {
            int i = this.getTickDelay(pLevel);
            if (!p_76205_.isEmpty() && !p_76206_.isEmpty() && !(Boolean)p_76205_.getValue(FALLING) && !(Boolean)p_76206_.getValue(FALLING) && p_76206_.getHeight(pLevel, pPos) > p_76205_.getHeight(pLevel, pPos) && pLevel.getRandom().nextInt(4) != 0) {
                i *= 4;
            }
            
            return i;
        }
        
        public void randomTick(Level pLevel, BlockPos pPos, FluidState pState, RandomSource pRandom) {
            if (pLevel.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
                int i = pRandom.nextInt(3);
                if (i > 0) {
                    BlockPos blockpos = pPos;
                    
                    for(int j = 0; j < i; ++j) {
                        blockpos = blockpos.offset(pRandom.nextInt(3) - 1, 1, pRandom.nextInt(3) - 1);
                        if (!pLevel.isLoaded(blockpos)) {
                            return;
                        }
                        
                        BlockState blockstate = pLevel.getBlockState(blockpos);
                        if (blockstate.isAir()) {
                            if (hasFlammableNeighbours(pLevel, blockpos)) {
                                pLevel.setBlockAndUpdate(blockpos, ForgeEventFactory.fireFluidPlaceBlockEvent(pLevel, blockpos, pPos, Blocks.FIRE.defaultBlockState()));
                                return;
                            }
                        } else if (blockstate.getMaterial().blocksMotion()) {
                            return;
                        }
                    }
                } else {
                    for(int k = 0; k < 3; ++k) {
                        BlockPos blockpos1 = pPos.offset(pRandom.nextInt(3) - 1, 0, pRandom.nextInt(3) - 1);
                        if (!pLevel.isLoaded(blockpos1)) {
                            return;
                        }
                        
                        if (pLevel.isEmptyBlock(blockpos1.above()) && isFlammable(pLevel, blockpos1, Direction.UP)) {
                            pLevel.setBlockAndUpdate(blockpos1.above(), ForgeEventFactory.fireFluidPlaceBlockEvent(pLevel, blockpos1.above(), pPos, Blocks.FIRE.defaultBlockState()));
                        }
                    }
                }
            }
            
        }
    }
    
    public static class Flowing extends ForgeFlowingFluid {
        public Flowing(Properties properties) {
            super(properties);
            this.registerDefaultState((FluidState)((FluidState)this.getStateDefinition().any()).setValue(LEVEL, 7));
        }
        
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(new Property[]{LEVEL});
        }
        
        public int getAmount(FluidState state) {
            return (Integer)state.getValue(LEVEL);
        }
        
        public boolean isSource(FluidState state) {
            return false;
        }
        
        public void animateTick(Level pLevel, BlockPos pPos, FluidState pState, RandomSource pRandom) {
            BlockPos blockpos = pPos.above();
            if (pLevel.getBlockState(blockpos).isAir() && !pLevel.getBlockState(blockpos).isSolidRender(pLevel, blockpos)) {
                if (pRandom.nextInt(100) == 0) {
                    double d0 = (double)pPos.getX() + pRandom.nextDouble();
                    double d1 = (double)pPos.getY() + 1.0;
                    double d2 = (double)pPos.getZ() + pRandom.nextDouble();
                    pLevel.addParticle(ParticleTypes.LAVA, d0, d1, d2, 0.0, 0.0, 0.0);
                    pLevel.playLocalSound(d0, d1, d2, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + pRandom.nextFloat() * 0.2F, 0.9F + pRandom.nextFloat() * 0.15F, false);
                }
                
                if (pRandom.nextInt(200) == 0) {
                    pLevel.playLocalSound((double)pPos.getX(), (double)pPos.getY(), (double)pPos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + pRandom.nextFloat() * 0.2F, 0.9F + pRandom.nextFloat() * 0.15F, false);
                }
            }
            
        }
        
        protected boolean canConvertToSource() {
            return false;
        }
        
        protected boolean isRandomlyTicking() {
            return true;
        }
        
        protected float getExplosionResistance() {
            return 100.0F;
        }
        
        public Optional<SoundEvent> getPickupSound() {
            return Optional.of(SoundEvents.BUCKET_FILL_LAVA);
        }
        
        @Nullable
        public ParticleOptions getDripParticle() {
            return ParticleTypes.DRIPPING_LAVA;
        }
        
        protected void beforeDestroyingBlock(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
            fizz(pLevel, pPos);
        }
        
        public int getSlopeFindDistance(LevelReader pLevel) {
            return pLevel.dimensionType().ultraWarm() ? 4 : 2;
        }
        
        public int getDropOff(LevelReader pLevel) {
            return pLevel.dimensionType().ultraWarm() ? 1 : 2;
        }
        
        public int getTickDelay(LevelReader pLevel) {
            return pLevel.dimensionType().ultraWarm() ? 10 : 30;
        }
        
        public int getSpreadDelay(Level pLevel, BlockPos pPos, FluidState p_76205_, FluidState p_76206_) {
            int i = this.getTickDelay(pLevel);
            if (!p_76205_.isEmpty() && !p_76206_.isEmpty() && !(Boolean)p_76205_.getValue(FALLING) && !(Boolean)p_76206_.getValue(FALLING) && p_76206_.getHeight(pLevel, pPos) > p_76205_.getHeight(pLevel, pPos) && pLevel.getRandom().nextInt(4) != 0) {
                i *= 4;
            }
            
            return i;
        }
        
        public void randomTick(Level pLevel, BlockPos pPos, FluidState pState, RandomSource pRandom) {
            if (pLevel.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
                int i = pRandom.nextInt(3);
                if (i > 0) {
                    BlockPos blockpos = pPos;
                    
                    for(int j = 0; j < i; ++j) {
                        blockpos = blockpos.offset(pRandom.nextInt(3) - 1, 1, pRandom.nextInt(3) - 1);
                        if (!pLevel.isLoaded(blockpos)) {
                            return;
                        }
                        
                        BlockState blockstate = pLevel.getBlockState(blockpos);
                        if (blockstate.isAir()) {
                            if (hasFlammableNeighbours(pLevel, blockpos)) {
                                pLevel.setBlockAndUpdate(blockpos, ForgeEventFactory.fireFluidPlaceBlockEvent(pLevel, blockpos, pPos, Blocks.FIRE.defaultBlockState()));
                                return;
                            }
                        } else if (blockstate.getMaterial().blocksMotion()) {
                            return;
                        }
                    }
                } else {
                    for(int k = 0; k < 3; ++k) {
                        BlockPos blockpos1 = pPos.offset(pRandom.nextInt(3) - 1, 0, pRandom.nextInt(3) - 1);
                        if (!pLevel.isLoaded(blockpos1)) {
                            return;
                        }
                        
                        if (pLevel.isEmptyBlock(blockpos1.above()) && isFlammable(pLevel, blockpos1, Direction.UP)) {
                            pLevel.setBlockAndUpdate(blockpos1.above(), ForgeEventFactory.fireFluidPlaceBlockEvent(pLevel, blockpos1.above(), pPos, Blocks.FIRE.defaultBlockState()));
                        }
                    }
                }
            }
            
        }
    }
}
