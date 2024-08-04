package com.freezedown.metallurgica.content.machines.rotary_kiln.heater_segment;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.model.BlockModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.CutoutRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class HeaterSegmentInstance extends CutoutRotatingInstance<HeaterSegmentBlockEntity> {
    protected final HeaterSegmentModelKey key;
    
    public HeaterSegmentInstance(MaterialManager materialManager, HeaterSegmentBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        key = new HeaterSegmentModelKey(getRenderedBlockState(), blockEntity.material);
    }
    
    @Override
    public boolean shouldReset() {
        return super.shouldReset() || key.material() != blockEntity.material;
    }
    
    @Override
    protected Instancer<RotatingData> getModel() {
        return getRotatingMaterial().model(key, () -> {
            BakedModel model = HeaterSegmentRenderer.generateModel(key);
            BlockState state = key.state();
            Direction dir = state.getValue(HeaterSegmentBlock.HORIZONTAL_FACING);
            PoseStack transform = CachedBufferer.rotateToFaceVertical(dir).get();
            return BlockModel.of(model, Blocks.AIR.defaultBlockState(), transform);
        });
    }
}
