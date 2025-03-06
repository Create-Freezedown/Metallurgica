package com.freezedown.metallurgica.content.fluids.types;

import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.FlowingGas;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.registries.ForgeRegistries;

public class ReactiveGas extends FlowingGas {
    
    public ReactiveGas(Properties properties) {
        super(properties);
    }
    
    @Override
    public void tick(Level worldIn, BlockPos pos, FluidState state) {
        super.tick(worldIn, pos, state);
        
        TagKey<Block> blockTag = MetallurgicaTags.modBlockTag("block_reactive/" + ForgeRegistries.FLUID_TYPES.get().getKey(this.getFluidType()).getPath());
        TagKey<Fluid> fluidTag = MetallurgicaTags.modFluidTag("fluid_reactive/" + ForgeRegistries.FLUID_TYPES.get().getKey(this.getFluidType()).getPath());
        
        for (int i = -1; i < 2; i++) {
            if (worldIn.getFluidState(pos.offset(i, i, i)).is(fluidTag)) {
                explode(worldIn, pos);
            }
            if (worldIn.getBlockState(pos.offset(i, i, i)).is(blockTag)) {
                explode(worldIn, pos);
            }
        }
    }
    
    private void explode(Level worldIn, BlockPos pos) {
        worldIn.explode(null, pos.getX(), pos.getY(), pos.getZ(), 1.0F, true, Level.ExplosionInteraction.BLOCK);
        worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }
    
    @Override
    protected boolean isSource() {
        return false;
    }
    
    public static class Flowing extends ReactiveGas {
        public Flowing(Properties properties) {
            super(properties);
        }
        
        @Override
        public boolean isSource() {
            return false;
        }
    }
    
    
    public static class Source extends ReactiveGas {
        public Source(Properties properties) {
            super(properties);
        }
        
        @Override
        public boolean isSource() {
            return true;
        }
    }
    
    //@Override
    //public List<IFluidBehavior> getUncheckedBehaviors() {
        //super.getUncheckedBehaviors().add(new Interaction
    //}
}
