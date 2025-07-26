package dev.metallurgists.metallurgica.content.mineral.drill.drill_activator;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class DrillActivatorRenderer extends KineticBlockEntityRenderer<DrillActivatorBlockEntity> {
    
    public DrillActivatorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    protected SuperByteBuffer getRotatedModel(DrillActivatorBlockEntity be, BlockState state) {
        return CachedBuffers.partial(AllPartialModels.SHAFTLESS_COGWHEEL, state);
    }
}
