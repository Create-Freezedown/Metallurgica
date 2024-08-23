package com.freezedown.metallurgica.content.primitive.log_pile;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public class LogPileGenerator extends SpecialBlockStateGen {
    public LogPileGenerator() {
    }
    
    
    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }
    
    @Override
    protected int getYRotation(BlockState state) {
        return 0;
    }
    
    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return null;
    }
    
    public <T extends Block> ModelFile createModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, int layers) {
        ResourceLocation log = prov.modLoc("block/" + ctx.getName());
        ResourceLocation logSide = prov.modLoc("block/" + ctx.getName() + "_side");
        return prov.models()
                .withExistingParent(ctx.getName() + "_" + layers, prov.modLoc("block/log_pile/" + layers))
                .texture("log", log)
                .texture("side", logSide);
    }
    
    public final <T extends Block> void generateCustom(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov) {
        prov.getVariantBuilder(ctx.getEntry()).partialState().with(LogPileBlock.LAYERS, 1).modelForState().modelFile(createModel(ctx, prov, 1)).addModel().partialState().with(LogPileBlock.LAYERS, 2).modelForState().modelFile(createModel(ctx, prov, 2)).addModel().partialState().with(LogPileBlock.LAYERS, 3).modelForState().modelFile(createModel(ctx, prov, 3)).addModel().partialState().with(LogPileBlock.LAYERS, 4).modelForState().modelFile(createModel(ctx, prov, 4)).addModel();
    }
}
