package com.freezedown.metallurgica.content.machines.shaking_table;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class ShakingTableGenerator extends SpecialBlockStateGen {
    
    public ShakingTableGenerator() {
    }
    
    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }
    
    
    protected int getYRotation(BlockState state) {
        short value;
        switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
            case NORTH:
                value = 0;
                break;
            case SOUTH:
                value = 180;
                break;
            case WEST:
                value = 270;
                break;
            case EAST:
                value = 90;
                break;
            
            default:
                throw new IncompatibleClassChangeError();
        }
        
        return value;
    }
    
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return AssetLookup.partialBaseModel(ctx, prov);
    }
}
