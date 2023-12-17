package com.freezedown.metallurgica.content.fluids.faucet;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class FaucetGenerator extends SpecialBlockStateGen {
    
    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }
    
    @Override
    protected int getYRotation(BlockState state) {
        return horizontalAngle(state.getValue(FaucetBlock.FACING).getOpposite());
    }
    
    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        if (state.getValue(FaucetBlock.FACING).getAxis().isVertical())
            return AssetLookup.partialBaseModel(ctx, prov);
        return AssetLookup.partialBaseModel(ctx, prov, "directional");
    }
}
