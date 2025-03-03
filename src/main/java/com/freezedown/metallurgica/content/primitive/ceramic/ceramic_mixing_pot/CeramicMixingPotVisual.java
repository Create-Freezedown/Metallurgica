package com.freezedown.metallurgica.content.primitive.ceramic.ceramic_mixing_pot;


import com.freezedown.metallurgica.registry.MetallurgicaPartialModels;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.Direction;

import java.util.function.Consumer;

public class CeramicMixingPotVisual extends AbstractBlockEntityVisual<CeramicMixingPotBlockEntity> implements SimpleDynamicVisual {
    private RotatingInstance stirrer;
    private final CeramicMixingPotBlockEntity mixer;

    public CeramicMixingPotVisual(VisualizationContext context, CeramicMixingPotBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        this.mixer = blockEntity;

        stirrer = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(MetallurgicaPartialModels.ceramicMixerStirrer))
                .createInstance();

        stirrer.setRotationAxis(Direction.Axis.Y);
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        animate(ctx.partialTick());
    }

    private void animate(float pt) {
        float renderedHeadOffset = mixer.getIndependentAngle(pt);

        transformStirrer(renderedHeadOffset);
    }

    private void transformStirrer(float renderedHeadOffset) {
        stirrer.setPosition(getVisualPosition())
                .nudge(0, -renderedHeadOffset, 0)
                .setRotationalSpeed(32 * 2 * RotatingInstance.SPEED_MULTIPLIER)
                .setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        relight(stirrer);
    }

    @Override
    protected void _delete() {
        stirrer.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(stirrer);
    }
}
