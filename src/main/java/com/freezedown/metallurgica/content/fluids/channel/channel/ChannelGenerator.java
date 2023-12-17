package com.freezedown.metallurgica.content.fluids.channel.channel;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class ChannelGenerator extends SpecialBlockStateGen {
    public ChannelGenerator() {
    }
    
    protected int getXRotation(BlockState state) {
        return 0;
    }
    
    protected int getYRotation(BlockState state) {
        return 0;
    }
    
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return AssetLookup.partialBaseModel(ctx, prov, "base");
    }
}
