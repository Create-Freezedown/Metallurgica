package com.freezedown.metallurgica.content.machines.electolizer;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class ElectrolyzerRenderer extends KineticBlockEntityRenderer<ElectrolyzerBlockEntity> {
    
    public ElectrolyzerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public boolean shouldRenderOffScreen(ElectrolyzerBlockEntity be) {
        return true;
    }
    
    @Override
    protected BlockState getRenderedBlockState(ElectrolyzerBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }
}
